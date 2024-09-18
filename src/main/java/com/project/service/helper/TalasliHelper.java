package com.project.service.helper;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.domain.enums.StatusType;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.repository.business.process.ProductionProcessRepository;
import com.project.repository.business.process.TalasliImalatRepository;
import com.project.service.business.OrderService;
import com.project.service.business.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TalasliHelper {

    private final TalasliImalatRepository talasliImalatRepository;
    private final OrderService orderService;
    private final MethodHelper methodHelper;
    private final OrderStatusService orderStatusService;
    private final ProductionProcessRepository productionProcessRepository;

    public Order updateOrderStatus(Order order) {
        Long productionId = order.getProductionProcess().getId();
        ProductionProcess productionProcess = findProductionProcessById(productionId);
        TalasliImalat talasliImalat = findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_KOPARMA);

        if (order.getOrderStatus().equals(orderStatusService.getOrderStatus(StatusType.ISLENMEYI_BEKLIYOR))) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.ISLENMEKTE));
            if (talasliImalat.getStartDate() == null) {
                talasliImalat.startOperation(); // This will set the startDate if it's null
            }

            if (productionProcess.getStartDate() == null) {
                productionProcess.startOperation(); // This will set the startDate for the production process
            }
        } else if (order.getOrderStatus().equals(orderStatusService.getOrderStatus(StatusType.ISLENMEKTE))) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.BEKLEMEDE));
        } else if (order.getOrderStatus().equals(orderStatusService.getOrderStatus(StatusType.BEKLEMEDE))) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.ISLENMEKTE));
        }

        productionProcessRepository.save(productionProcess);
        talasliImalatRepository.save(talasliImalat);
        return order;
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
}

