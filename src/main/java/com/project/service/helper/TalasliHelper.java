package com.project.service.helper;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.domain.concretes.business.process.polisajamiri.PolisajImalat;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.domain.enums.OrderType;
import com.project.domain.enums.StatusType;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.business.OrderRequest;
import com.project.payload.request.business.UpdateOrderRequest;
import com.project.repository.business.process.PolisajImalatRepository;
import com.project.repository.business.process.ProductionProcessRepository;
import com.project.repository.business.process.TalasliImalatRepository;
import com.project.service.business.OrderService;
import com.project.service.business.OrderStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TalasliHelper {

    private final TalasliImalatRepository talasliImalatRepository;
    private final MethodHelper methodHelper;
    private final OrderStatusService orderStatusService;
    private final ProductionProcessRepository productionProcessRepository;
    private final PolisajImalatRepository polisajImalatRepository;

    @Transactional
    public Order updateOrderStatus(Order order) {
        Long productionId = order.getProductionProcess().getId();
        ProductionProcess productionProcess = findProductionProcessById(productionId);
        TalasliImalat operation ;
        if (order.getOrderType().equals(OrderType.BLOKLIFT)){
            operation  = findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);
        }else{
            operation = findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_KOPARMA);
        }
        TalasliImalat boruKesme = findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.BORU_KESME_HAVSA);

        if (order.getOrderStatus().equals(orderStatusService.getOrderStatus(StatusType.ISLENMEYI_BEKLIYOR))) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.ISLENMEKTE));
            if (operation.getStartDate() == null) {
                operation.startOperation();
            }

            if (boruKesme.getStartDate() == null) {
                boruKesme.startOperation();
            }

            if (productionProcess.getStartDate() == null) {
                productionProcess.startOperation(); // This will set the startDate for the production process
            }
        } else if (order.getOrderStatus().equals(orderStatusService.getOrderStatus(StatusType.ISLENMEKTE))) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.BEKLEMEDE));
        } else if (order.getOrderStatus().equals(orderStatusService.getOrderStatus(StatusType.BEKLEMEDE))) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.ISLENMEKTE));
        }
        Order updatedOrder = methodHelper.saveOrderWithReturn(order);
        productionProcessRepository.save(productionProcess);
        talasliImalatRepository.save(operation);
        talasliImalatRepository.save(boruKesme);
        return updatedOrder;
    }

    public ProductionProcess findProductionProcessById(Long id) {
        return productionProcessRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessages.PRODUCTION_PROCESS_NOT_FOUND, id)));
    }

    public TalasliImalat findTalasliImalatByProductionProcess(ProductionProcess productionProcess, TalasliOperationType operationType) {
        return productionProcess.getTalasliOperations()
                .stream()
                .filter(t -> t.getOperationType().equals(operationType))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.TALASLI_IMALAT_NOT_FOUND, operationType)));

    }

    public List<TalasliImalat> talasliOperations (ProductionProcess productionProcess) {
        return productionProcess.getTalasliOperations();
    }

    public TalasliImalat findTalasliImalatByOperationType(TalasliOperationType operationType) {
        return talasliImalatRepository.findFirstByOperationType(operationType)
                .orElseThrow(() -> new RuntimeException(String.format(ErrorMessages.TALASLI_IMALAT_OPERATION_TYPE_NOT_FOUND,operationType)));
    }

    public TalasliImalat findOperationById(Long id) {
        return talasliImalatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.TALASLI_IMALAT_NOT_FOUND, id)));
    }

    public PolisajImalat findPolisajImalatByProductionProcess(ProductionProcess productionProcess) {
        return polisajImalatRepository.findPolisajImalatByProductionProcess(productionProcess)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.POLISAJ_IMALAT_NOT_FOUND)));


    }

    public TalasliImalat saveTalasliImalatWithReturn(TalasliImalat talasliImalat) {
        return talasliImalatRepository.save(talasliImalat);
    }

    public void saveTalasliImalatWithoutReturn(TalasliImalat talasliImalat) {
        talasliImalatRepository.save(talasliImalat);
    }

    public void updateOperation(TalasliImalat talasliImalat, Order order) {

            if (talasliImalat.getOperationType().equals(TalasliOperationType.BORU_KESME_HAVSA)){
                talasliImalat.setRemainingQuantity(order.getOrderQuantity()-talasliImalat.getCompletedQuantity());
                saveTalasliImalatWithoutReturn(talasliImalat);
            }else{
                talasliImalat.setRemainingQuantity(order.getOrderQuantity()-talasliImalat.getCompletedQuantity()-order.getReadyMilCount());
                saveTalasliImalatWithoutReturn(talasliImalat);
            }
    }


}

