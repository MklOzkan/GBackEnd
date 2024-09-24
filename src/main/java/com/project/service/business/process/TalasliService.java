package com.project.service.business.process;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process._enums.BlokLiftOperationType;
import com.project.domain.concretes.business.process._enums.LiftMontajOperationTye;
import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
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
import com.project.service.helper.MontajHelper;
import com.project.service.helper.TalasliHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TalasliService {

    private final MethodHelper methodHelper;
    private final OrderStatusService orderStatusService;
    private final OrderRepository orderRepository;
    private final TalasliHelper talasliHelper;
    private final TalasliImalatRepository talasliImalatRepository;
    private final TalasliMapper talasliMapper;
    private final MontajHelper montajHelper;


    public ResponseMessage<String> startStop(Long id) {

        Order order = methodHelper.findOrderById(id);
        Order updatedOrder = talasliHelper.updateOrderStatus(order);
        orderRepository.save(updatedOrder);

        return ResponseMessage.<String>builder()
                .httpStatus(HttpStatus.OK)
                .build();



    }


    @Transactional
    public MultipleResponses<TalasliImalatResponse, TalasliImalatResponse, Void> milkoparma(int uretilenMilkoparmaSayisi, Long oprateionId) {

        ProductionProcess productionProcess = talasliHelper.findProductionProcessById(oprateionId);
        TalasliImalat milkoparma = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_KOPARMA);

        if(milkoparma.getCompletedQuantity() == null){
            milkoparma.setCompletedQuantity(0);
        }
        if (milkoparma.getRemainingQuantity() == null) {
            milkoparma.setRemainingQuantity(0);
        }

        milkoparma.completeOperation(uretilenMilkoparmaSayisi);

        TalasliImalat milTornalama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TORNALAMA);


        if (milTornalama.getRemainingQuantity() == null) {
            milTornalama.setRemainingQuantity(0);
        }

        if(milTornalama.getCompletedQuantity() == null){
            milTornalama.setCompletedQuantity(0);
        }

        milTornalama.updateNextOperation(uretilenMilkoparmaSayisi);

        talasliImalatRepository.save(milkoparma);
        talasliImalatRepository.save(milTornalama);


        TalasliImalatResponse milkoparmaResponse = talasliMapper.mapTalasliToResponse(milkoparma);
        TalasliImalatResponse milTornalamaResponse = talasliMapper.mapTalasliToResponse(milTornalama);

        return MultipleResponses.<TalasliImalatResponse, TalasliImalatResponse, Void>builder()
                .returnBody(milkoparmaResponse)
                .returnBody2(milTornalamaResponse)
                .message(SuccessMessages.MILKOPARMA_COMPLETED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

//    @Transactional
//    public  ResponseMessage<TalasliImalatResponse>  borukesme(TalasliImalatRequest request, Long id) {
//
//        Order order = methodHelper.findOrderById(id);
//        ProductionProcess productionProcess= talasliHelper.findProductionProcessById(order.getProductionProcess().getId());
//        TalasliImalat boruKesme=talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.BORU_KESME_HAVSA);
//        boruKesme.completeOperation(request.getCompletedQuantity());
//
//        ResponseMessage<TalasliImalatResponse>rs;
//        if(!(order.getOrderType().equals(OrderType.BLOKLIFT))){
//
//            LiftMontaj liftMontaj =montajHelper.findLiftMontajByProductionProcess(productionProcess, LiftMontajOperationTye.BORU_KAPAMA);
//            liftMontaj.completeOperation(request.getCompletedQuantity());
//
//
//            return ResponseMessage.<TalasliImalatResponse>builder()
//                    .message(SuccessMessages.ORDER_STARTED)
//                    .returnBody(talasliMapper.mapTalasliToResponse(boruKesme))
//                    .httpStatus(HttpStatus.OK)
//                    .build();
//
//        }else if(order.getOrderType().equals(OrderType.BLOKLIFT)){
//            BlokLiftMontaj blokLiftMontaj =  montajHelper.findBlokLiftMontajByOperationType( BlokLiftOperationType.BORU_KAPAMA);
//            blokLiftMontaj.completeOperation(request.getCompletedQuantity());
//
//
//            return MultipleResponses.<TalasliImalatResponse,TalasliImalatResponse,Void>builder()
//                    .returnBody(talasliMapper.mapTalasliToResponse(blokLiftMontaj))
//                    .returnBody2(talasliMapper.mapTalasliToResponse(boruKesme))
//                    .message(SuccessMessages.ORDER_STARTED)
//                    .httpStatus(HttpStatus.OK)
//                    .build();
//
//        }
//
//    }
}
