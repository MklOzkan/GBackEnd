package com.project.service.business.process;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process._enums.BlokLiftOperationType;
import com.project.domain.concretes.business.process._enums.KaliteKontrolStage;
import com.project.domain.concretes.business.process._enums.LiftMontajOperationTye;
import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.domain.concretes.business.process.polisajamiri.PolisajImalat;
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
import com.project.repository.business.process.KaliteKontrolRepository;
import com.project.repository.business.process.PolisajImalatRepository;
import com.project.repository.business.process.TalasliImalatRepository;
import com.project.service.business.OrderStatusService;
import com.project.service.helper.KaliteKontrolHelper;
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
    private final PolisajImalatRepository polisajImalatRepository;
    private final KaliteKontrolRepository kaliteKontrolRepository;
    private final KaliteKontrolHelper kaliteKontrolHelper;
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

    @Transactional
    public ResponseMessage<String> milTornalama(TalasliImalatRequest request, Long operationId) {

        ProductionProcess productionProcess = talasliHelper.findProductionProcessById(request.getProductionProcessId());
        TalasliImalat miltornalama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TORNALAMA);

        if(miltornalama.getCompletedQuantity() == null){
            miltornalama.setCompletedQuantity(0);
        }
        if (miltornalama.getRemainingQuantity() == null) {
            miltornalama.setRemainingQuantity(0);
        }

        miltornalama.completeOperation(request.getCompletedQuantity());

        TalasliImalat miltaslama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);

        if (miltaslama.getRemainingQuantity() == null) {
            miltaslama.setRemainingQuantity(0);
        }

        if(miltornalama.getCompletedQuantity() == null){
            miltaslama.setCompletedQuantity(0);
        }

        miltaslama.updateNextOperation(request.getCompletedQuantity());

        talasliImalatRepository.save(miltornalama);
        talasliImalatRepository.save(miltaslama);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.MILTORNALAMA_COMPLETED)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    @Transactional
    public ResponseMessage<String> milTaslama(TalasliImalatRequest request, Long operationId) {

        ProductionProcess productionProcess = talasliHelper.findProductionProcessById(request.getProductionProcessId());
        OrderType orderType = productionProcess.getOrder().getOrderType();
        TalasliImalat miltaslama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);

        if(miltaslama.getCompletedQuantity() == null){
            miltaslama.setCompletedQuantity(0);
        }
        if (miltaslama.getRemainingQuantity() == null) {
            miltaslama.setRemainingQuantity(0);
        }

        miltaslama.completeOperation(request.getCompletedQuantity());

        TalasliImalat isilIslem;
        KaliteKontrol afterMiltaslama;

        if (orderType.equals(OrderType.PASLANMAZ)){
            afterMiltaslama = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MIL_TASLAMA);
            if (afterMiltaslama.getMilCount() == null) {
                afterMiltaslama.setMilCount(0);
                afterMiltaslama.setStartDate(LocalDateTime.now());
            }

            afterMiltaslama.setMilCount(request.getCompletedQuantity());
            kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterMiltaslama);
        } else {
            isilIslem = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.ISIL_ISLEM);
            if (isilIslem.getRemainingQuantity() == null) {
                isilIslem.setRemainingQuantity(0);
            }

            if(isilIslem.getCompletedQuantity() == null){
                isilIslem.setCompletedQuantity(0);
            }

            isilIslem.updateNextOperation(request.getCompletedQuantity());
            talasliImalatRepository.save(isilIslem);
        }

        talasliImalatRepository.save(miltaslama);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.MILTASLAMA_COMPLETED)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    @Transactional
    public ResponseMessage<String> isilIslem(TalasliImalatRequest request, Long operationId) {

        ProductionProcess productionProcess = talasliHelper.findProductionProcessById(request.getProductionProcessId());
        TalasliImalat isilislem = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.ISIL_ISLEM);

        if(isilislem.getCompletedQuantity() == null){
            isilislem.setCompletedQuantity(0);
        }
        if (isilislem.getRemainingQuantity() == null) {
            isilislem.setRemainingQuantity(0);
        }

        isilislem.completeOperation(request.getCompletedQuantity());

        PolisajImalat polisajImalat = talasliHelper.findPolisajImalatByProductionProcess(productionProcess);

        if (polisajImalat.getRemainingQuantity() == null) {
            polisajImalat.setRemainingQuantity(0);
        }

        if(polisajImalat.getCompletedQuantity() == null){
            polisajImalat.setCompletedQuantity(0);
        }

        polisajImalat.updateNextOperation(request.getCompletedQuantity());

        talasliImalatRepository.save(isilislem);
        polisajImalatRepository.save(polisajImalat);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.ISILISLEM_COMPLETED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Transactional
    public ResponseMessage<String> ezme(TalasliImalatRequest request, Long operationId) {

        ProductionProcess productionProcess = talasliHelper.findProductionProcessById(request.getProductionProcessId());
        TalasliImalat ezme = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.EZME);

        if(ezme.getCompletedQuantity() == null){
            ezme.setCompletedQuantity(0);
        }
        if (ezme.getRemainingQuantity() == null) {
            ezme.setRemainingQuantity(0);
        }

        ezme.completeOperation(request.getCompletedQuantity());

        KaliteKontrol afterEzme= kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MIL_TASLAMA);

        if (afterEzme.getMilCount() == null) {
            afterEzme.setMilCount(0);
        }
        afterEzme.setMilCount(request.getCompletedQuantity());

        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterEzme);
        talasliImalatRepository.save(ezme);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.EZME_COMPLETED)
                .httpStatus(HttpStatus.OK)
                .build();

    }
}
