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
import com.project.exception.ConflictException;
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
import jakarta.validation.Valid;
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
    @Autowired
    private final MethodHelper methodHelper;
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

        TalasliImalat milkoparma = talasliHelper.findOperationById(operationId);
        ProductionProcess productionProcess = milkoparma.getProductionProcess();

        milkoparma.completeOperation(request.getCompletedQuantity());

        TalasliImalat milTornalama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TORNALAMA);

        milTornalama.updateNextOperation(request.getCompletedQuantity());

        talasliImalatRepository.save(milkoparma);
        talasliImalatRepository.save(milTornalama);

        return ResponseMessage.<String>builder()
                .message(String.format(SuccessMessages.MILKOPARMA_COMPLETED, request.getCompletedQuantity()))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Transactional
    public  ResponseMessage<String>  borukesme(TalasliImalatRequest request, Long id) {

        TalasliImalat boruKesme = talasliHelper.findOperationById(id);
        ProductionProcess productionProcess= boruKesme.getProductionProcess();
        OrderType orderType = productionProcess.getOrder().getOrderType();

        boruKesme.completeOperation(request.getCompletedQuantity());

        if((orderType.equals(OrderType.LIFT)||orderType.equals(OrderType.PASLANMAZ))){
            LiftMontaj liftBoruKapama =montajHelper.findLiftMontajByProductionProcess(productionProcess, LiftMontajOperationTye.BORU_KAPAMA);
            liftBoruKapama.updateNextOperation(request.getCompletedQuantity());
            montajHelper.saveLiftMontajWithoutReturn(liftBoruKapama);

        }else if(orderType.equals(OrderType.DAMPER)){
            BlokLiftMontaj blokLiftBoru = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BORU_KAPAMA);
            blokLiftBoru.updateNextOperation(request.getCompletedQuantity());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftBoru);
        }else {
            BlokLiftMontaj blokLiftBoru = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BLOK_LIFT_MONTAJ);
            blokLiftBoru.updateNextPipeOperation(request.getCompletedQuantity());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftBoru);
            montajHelper.compareMilCountAndPipeCountForBLMontaj(blokLiftBoru);
        }

        talasliHelper.saveTalasliImalatWithoutReturn(boruKesme);

        return methodHelper.createResponse(SuccessMessages.BORU_COMPLETED, HttpStatus.OK, null);

    }

    public void isCompletedCountMoreThanRemainingCount(int completedCount, int remainingCount) {
        if (completedCount > remainingCount) {
            throw new ConflictException("Üretilen miktar hedef miktarı aşamaz");
        }
    }

        

    @Transactional
    public ResponseMessage<String> milTornalama(TalasliImalatRequest request, Long operationId) {

        TalasliImalat miltornalama = talasliHelper.findOperationById(operationId);
        ProductionProcess productionProcess = miltornalama.getProductionProcess();

        isCompletedCountMoreThanRemainingCount(request.getCompletedQuantity(), miltornalama.getRemainingQuantity());

        miltornalama.completeOperation(request.getCompletedQuantity());

        TalasliImalat miltaslama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);

        miltaslama.updateNextOperation(request.getCompletedQuantity());

        talasliImalatRepository.save(miltornalama);
        talasliImalatRepository.save(miltaslama);

        return methodHelper.createResponse(SuccessMessages.MILTORNALAMA_COMPLETED, HttpStatus.OK, null);
    }


    @Transactional
    public ResponseMessage<String> milTaslama(TalasliImalatRequest request, Long operationId) {

        TalasliImalat miltaslama = talasliHelper.findOperationById(operationId);
        ProductionProcess productionProcess = miltaslama.getProductionProcess();
        OrderType orderType = productionProcess.getOrder().getOrderType();

        if (request.getCompletedQuantity()> miltaslama.getRemainingQuantity()) {
            throw new ConflictException("Üretilen miktar hedef miktarı aşamaz");
        }

        miltaslama.completeOperation(request.getCompletedQuantity());

        TalasliImalat isilIslem;
        KaliteKontrol afterMiltaslama;

        if (orderType.equals(OrderType.PASLANMAZ)){
            afterMiltaslama = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MIL_TASLAMA);
            afterMiltaslama.nextOperationByKaliteKontrol(request.getCompletedQuantity());
            kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterMiltaslama);
        } else {
            isilIslem = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.ISIL_ISLEM);
            isilIslem.updateNextOperation(request.getCompletedQuantity());
            talasliHelper.saveTalasliImalatWithoutReturn(isilIslem);
        }

        talasliImalatRepository.save(miltaslama);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.MILTASLAMA_COMPLETED)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    @Transactional
    public ResponseMessage<String> isilIslem(TalasliImalatRequest request, Long operationId) {
        TalasliImalat isilislem = talasliHelper.findOperationById(operationId);
        ProductionProcess productionProcess = isilislem.getProductionProcess();

        if (request.getCompletedQuantity()> isilislem.getRemainingQuantity()) {
            throw new ConflictException("Üretilen miktar hedef miktarı aşamaz");
        }

        isilislem.completeOperation(request.getCompletedQuantity());

        PolisajImalat polisajImalat = talasliHelper.findPolisajImalatByProductionProcess(productionProcess);

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
        TalasliImalat ezme = talasliHelper.findOperationById(operationId);
        ProductionProcess productionProcess = ezme.getProductionProcess();

        if (request.getCompletedQuantity()> ezme.getRemainingQuantity()) {
            throw new ConflictException("Üretilen miktar hedef miktarı aşamaz");
        }

        ezme.completeOperation(request.getCompletedQuantity());

        KaliteKontrol afterEzme= kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_EZME);

        afterEzme.nextOperationByKaliteKontrol(request.getCompletedQuantity());

        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterEzme);
        talasliImalatRepository.save(ezme);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.EZME_COMPLETED)
                .httpStatus(HttpStatus.OK)
                .build();

    }
    @Transactional
    public ResponseMessage<String> removeLastChange(@Valid Long operationId) {
        TalasliImalat talasliImalat = talasliHelper.findOperationById(operationId);
        ProductionProcess productionProcess = talasliImalat.getProductionProcess();
        TalasliOperationType operationType = talasliImalat.getOperationType();
        int lastCompletedQty = talasliImalat.getLastCompletedQty();
        OrderType orderType = productionProcess.getOrder().getOrderType();
        PolisajImalat polisajImalat = productionProcess.getPolisajOperation();

        switch (operationType) {
            case BORU_KESME_HAVSA:
                handleBoruKesmeHavsa(productionProcess, orderType, lastCompletedQty);
                break;
            case MIL_KOPARMA:
                handleNextOperation(productionProcess, TalasliOperationType.MIL_TORNALAMA, lastCompletedQty);
                break;
            case MIL_TORNALAMA:
                handleNextOperation(productionProcess, TalasliOperationType.MIL_TASLAMA, lastCompletedQty);
                break;
            case MIL_TASLAMA:
                handleMilTaslama(productionProcess, orderType, lastCompletedQty);
                break;
            case ISIL_ISLEM:
                polisajImalat.removeLastFromNextOperation(lastCompletedQty);
                polisajImalatRepository.save(polisajImalat);
                break;
            case EZME:
                KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_EZME);
                kaliteKontrol.removeLastFromNextOperation(lastCompletedQty);
                kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
                break;
        }

        talasliImalat.removeLastCompletedQty();
        talasliImalatRepository.save(talasliImalat);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.LAST_CHANGE_REMOVED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    private void handleBoruKesmeHavsa(ProductionProcess productionProcess, OrderType orderType, int lastCompletedQty) {
        if (orderType.equals(OrderType.BLOKLIFT)) {
            BlokLiftMontaj blokLiftMontaj = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BLOK_LIFT_MONTAJ);
            blokLiftMontaj.removeLastPipeCount(lastCompletedQty);
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
            montajHelper.compareMilCountAndPipeCountForBLMontaj(blokLiftMontaj);
        } else if (orderType.equals(OrderType.DAMPER)) {
            BlokLiftMontaj blokLiftMontaj = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BORU_KAPAMA);
            blokLiftMontaj.removeLastFromNextOperation(lastCompletedQty);
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
        } else {
            LiftMontaj boruKapama = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.BORU_KAPAMA);
            boruKapama.removeLastFromNextOperation(lastCompletedQty);
            montajHelper.saveLiftMontajWithoutReturn(boruKapama);
        }
    }

    private void handleNextOperation(ProductionProcess productionProcess, TalasliOperationType nextOperationType, int lastCompletedQty) {
        TalasliImalat nextOperation = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, nextOperationType);
        nextOperation.removeLastFromNextOperation(lastCompletedQty);
        talasliHelper.saveTalasliImalatWithoutReturn(nextOperation);
    }

    private void handleMilTaslama(ProductionProcess productionProcess, OrderType orderType, int lastCompletedQty) {
        if (orderType.equals(OrderType.PASLANMAZ)) {
            KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MIL_TASLAMA);
            kaliteKontrol.removeLastFromNextOperation(lastCompletedQty);
            kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
        } else {
            handleNextOperation(productionProcess, TalasliOperationType.ISIL_ISLEM, lastCompletedQty);
        }
    }
}
