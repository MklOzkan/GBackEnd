package com.project.service.business.process;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.enums.OrderType;
import com.project.payload.mappers.TalasliMapper;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.process.TalasliImalatRequest;
import com.project.payload.response.business.MultipleResponses;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.process.TalasliImalatResponse;
import com.project.repository.business.OrderRepository;
import com.project.repository.business.process.TalasliImalatRepository;
import com.project.service.business.OrderStatusService;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.TalasliHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TalasliService {

    private final MethodHelper methodHelper;
    private final OrderStatusService orderStatusService;
    private final OrderRepository orderRepository;
    private final TalasliHelper talasliHelper;
    private final TalasliImalatRepository talasliImalatRepository;
    private final TalasliMapper talasliMapper;



    public ResponseMessage<String> startStop(Long id) {

        Order order = methodHelper.findOrderById(id);
        Order updatedOrder = talasliHelper.updateOrderStatus(order);
        orderRepository.save(updatedOrder);

        return ResponseMessage.<String>builder()
                .httpStatus(HttpStatus.OK)
                .build();
    }


    @Transactional
    public ResponseMessage<String> milkoparma(TalasliImalatRequest request, Long operationId) {

        ProductionProcess productionProcess = talasliHelper.findProductionProcessById(request.getProductionProcessId());
        TalasliImalat milkoparma = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_KOPARMA);

        if(milkoparma.getCompletedQuantity() == null){
            milkoparma.setCompletedQuantity(0);
        }
        if (milkoparma.getRemainingQuantity() == null) {
            milkoparma.setRemainingQuantity(0);
        }

        milkoparma.completeOperation(request.getCompletedQuantity());

        TalasliImalat milTornalama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TORNALAMA);

        if (milTornalama.getRemainingQuantity() == null) {
            milTornalama.setRemainingQuantity(0);
        }

        if(milTornalama.getCompletedQuantity() == null){
            milTornalama.setCompletedQuantity(0);
        }

        milTornalama.updateNextOperation(request.getCompletedQuantity());

        talasliImalatRepository.save(milkoparma);
        talasliImalatRepository.save(milTornalama);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.MILKOPARMA_COMPLETED)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    public ResponseMessage<String> borukesme(Integer quantity, Long id) {

        Order order = methodHelper.findOrderById(id);
        ProductionProcess productionProcessId= talasliHelper.findProductionProcessById(order.getProductionProcess().getId());
        TalasliImalat boruKesme=talasliHelper.findTalasliImalatByProductionProcess(productionProcessId, TalasliOperationType.BORU_KESME_HAVSA);
        boruKesme.completeOperation(quantity);
        if(order.getOrderType().equals(OrderType.LIFT)){

        }


        return ResponseMessage.<String>builder()
                .httpStatus(HttpStatus.OK)
                .build();


    }
}
