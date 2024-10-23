package com.project.service.business.process;
import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.BlokLiftOperationType;
import com.project.domain.concretes.business.process._enums.BoyaPaketOperationType;
import com.project.domain.concretes.business.process._enums.KaliteKontrolStage;
import com.project.domain.concretes.business.process._enums.LiftMontajOperationTye;
import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import com.project.domain.concretes.business.process.boyavepaket.BoyaVePaketleme;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.domain.enums.OrderType;
import com.project.exception.BadRequestException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.process.MontajRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.helper.BoyaPaketHelper;
import com.project.service.helper.KaliteKontrolHelper;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.MontajHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.geometry.partitioning.BSPTreeVisitor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MontajService {

    private final MontajHelper montajHelper;
    private final MethodHelper methodHelper;
    private final KaliteKontrolHelper kaliteKontrolHelper;
    private final BoyaPaketHelper boyaPaketHelper;

    public ResponseMessage<String> boruKapamaOperationForBL(Long operationId, MontajRequest request) {
        BlokLiftMontaj blokLiftBoruKapama= montajHelper.findBlokLiftOperationById(operationId);
        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(),blokLiftBoruKapama.getRemainingQuantity());

        ProductionProcess productionProcess = blokLiftBoruKapama.getProductionProcess();

        blokLiftBoruKapama.completeOperation(request.getCompletedQuantity());
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftBoruKapama);
        BlokLiftMontaj nextOperation;
        if (productionProcess.getOrder().getOrderType().equals(OrderType.DAMPER)){
            nextOperation = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BORU_KAYNAK);
        }else {
            nextOperation = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.GAZ_DOLUM);
        }

        nextOperation.updateNextOperation(blokLiftBoruKapama.getLastCompletedQty());
        montajHelper.saveBlokLiftMontajWithoutReturn(nextOperation);

        return methodHelper.createResponse(SuccessMessages.BORU_KAPAMA_COMPLETED, HttpStatus.OK, null);
    }

    public ResponseMessage<String> boruKapamaOperationForLift(Long operationId, MontajRequest request) {
        LiftMontaj liftBoruKapama = montajHelper.findLiftOperationById(operationId);
        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(),liftBoruKapama.getRemainingQuantity());
        ProductionProcess productionProcess = liftBoruKapama.getProductionProcess();
        liftBoruKapama.completeOperation(request.getCompletedQuantity());
        montajHelper.saveLiftMontajWithoutReturn(liftBoruKapama);

        LiftMontaj boruKaynak = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.BORU_KAYNAK);

        boruKaynak.updateNextOperation(liftBoruKapama.getLastCompletedQty());
        montajHelper.saveLiftMontajWithoutReturn(boruKaynak);

        return methodHelper.createResponse(SuccessMessages.BORU_KAPAMA_COMPLETED, HttpStatus.OK, null);
    }


    public ResponseMessage<String> boruKaynakOperationForBl(Long operationId, MontajRequest request) {

        BlokLiftMontaj blokLiftBoruKaynak= montajHelper.findBlokLiftOperationById(operationId);
        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(),blokLiftBoruKaynak.getRemainingQuantity());
        ProductionProcess productionProcess = blokLiftBoruKaynak.getProductionProcess();
        blokLiftBoruKaynak.completeOperation(request.getCompletedQuantity());
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftBoruKaynak);

        BlokLiftMontaj blokLiftMontaj = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BLOK_LIFT_MONTAJ);
        blokLiftMontaj.updateNextPipeOperation(blokLiftBoruKaynak.getLastCompletedQty());
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);

        blokLiftMontaj.updateAccordingToPipeAndMilCount();
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
        return methodHelper.createResponse(SuccessMessages.BORU_KAYANAK_COMPLETED, HttpStatus.OK, null);
    }

    public ResponseMessage<String> boruKaynakOperationForLift(Long operationId, MontajRequest request) {
        LiftMontaj liftBoruKaynak = montajHelper.findLiftOperationById(operationId);
        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(),liftBoruKaynak.getRemainingQuantity());
        ProductionProcess productionProcess = liftBoruKaynak.getProductionProcess();
        liftBoruKaynak.completeOperation(request.getCompletedQuantity());
        montajHelper.saveLiftMontajWithoutReturn(liftBoruKaynak);

        LiftMontaj liftMontaj = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.LIFT_MONTAJ);

        liftMontaj.updateNextPipeOperation(liftBoruKaynak.getLastCompletedQty());
        montajHelper.saveLiftMontajWithoutReturn(liftMontaj);

        liftMontaj.updateAccordingToPipeAndMilCount();
        montajHelper.saveLiftMontajWithoutReturn(liftMontaj);

        return methodHelper.createResponse(SuccessMessages.BORU_KAYANAK_COMPLETED, HttpStatus.OK, null);
    }


    public ResponseMessage<String> montajOperationForBl(Long operationId, MontajRequest request) {
        BlokLiftMontaj blokLiftMontaj = montajHelper.findBlokLiftOperationById(operationId);
        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(),blokLiftMontaj.getRemainingQuantity());
        if(blokLiftMontaj.getProductionProcess().getOrder().getOrderType().equals(OrderType.DAMPER)) {

            ProductionProcess productionProcess = blokLiftMontaj.getProductionProcess();

            blokLiftMontaj.completeOperation(request.getCompletedQuantity());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);

            KaliteKontrol afterMontaj =  kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MONTAJ);
            afterMontaj.nextOperationByKaliteKontrol(blokLiftMontaj.getLastCompletedQty());
            kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterMontaj);

        }else {

            ProductionProcess productionProcess = blokLiftMontaj.getProductionProcess();

            blokLiftMontaj.completeOperation(request.getCompletedQuantity());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);

           BlokLiftMontaj blokLiftBoruKapama = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BORU_KAPAMA);
            blokLiftBoruKapama.updateNextOperation(blokLiftMontaj.getLastCompletedQty());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftBoruKapama);

        }

        return methodHelper.createResponse(SuccessMessages.BORU_MONTAJ_COMPLETED, HttpStatus.OK, null);
    }

    public ResponseMessage<String> montajOperationForLift(Long operationId, MontajRequest request) {
        LiftMontaj liftMontaj = montajHelper.findLiftOperationById(operationId);
        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(),liftMontaj.getRemainingQuantity());
        ProductionProcess productionProcess = liftMontaj.getProductionProcess();

        liftMontaj.completeOperation(request.getCompletedQuantity());
        montajHelper.saveLiftMontajWithoutReturn(liftMontaj);

        KaliteKontrol afterMontaj =  kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MONTAJ);
        afterMontaj.nextOperationByKaliteKontrol(liftMontaj.getLastCompletedQty());
        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterMontaj);

        return methodHelper.createResponse(SuccessMessages.MONTAJ_OPERATION_UPDATED, HttpStatus.OK, null);
    }


    public ResponseMessage<String> blokLiftBoruKapamaOperation(Long operationId, MontajRequest request) {
        BlokLiftMontaj blokLiftMontaj = montajHelper.findBlokLiftOperationById(operationId);
        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(),blokLiftMontaj.getRemainingQuantity());
        ProductionProcess productionProcess = blokLiftMontaj.getProductionProcess();

        blokLiftMontaj.completeOperation(request.getCompletedQuantity());
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);

        BlokLiftMontaj nextOperation = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.GAZ_DOLUM);
        nextOperation.updateNextOperation(blokLiftMontaj.getLastCompletedQty());
        montajHelper.saveBlokLiftMontajWithoutReturn(nextOperation);

        return methodHelper.createResponse(SuccessMessages.BORU_KAPAMA_COMPLETED, HttpStatus.OK, null);
    }


    public ResponseMessage<String> gazDolumOperationForBL(Long operationId, MontajRequest request) {
        BlokLiftMontaj blokLiftMontaj= montajHelper.findBlokLiftOperationById(operationId);
        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(),blokLiftMontaj.getRemainingQuantity());
        ProductionProcess productionProcess = blokLiftMontaj.getProductionProcess();

        blokLiftMontaj.completeOperation(request.getCompletedQuantity());
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);

        BlokLiftMontaj nextOperation = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.TEST);
        nextOperation.updateNextOperation(blokLiftMontaj.getLastCompletedQty());
        montajHelper.saveBlokLiftMontajWithoutReturn(nextOperation);
        return methodHelper.createResponse(SuccessMessages.GAZ_DOLUM_COMPLETED, HttpStatus.OK, null);
    }

    public ResponseMessage<String> gazDolumOperationForLift(Long operationId, MontajRequest request) {
        LiftMontaj liftMontaj = montajHelper.findLiftOperationById(operationId);
        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(),liftMontaj.getRemainingQuantity());
        ProductionProcess productionProcess = liftMontaj.getProductionProcess();

        liftMontaj.completeOperation(request.getCompletedQuantity());
        montajHelper.saveLiftMontajWithoutReturn(liftMontaj);

        LiftMontaj nextOperation = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.BASLIK_TAKMA);
        nextOperation.updateNextOperation(liftMontaj.getLastCompletedQty());
        montajHelper.saveLiftMontajWithoutReturn(nextOperation);

        return methodHelper.createResponse(SuccessMessages.GAZ_DOLUM_COMPLETED, HttpStatus.OK, null);
    }


    public ResponseMessage<String> baslikTakmaOperation(Long operationId, @Valid MontajRequest request) {
        LiftMontaj liftMontaj = montajHelper.findLiftOperationById(operationId);
        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(),liftMontaj.getRemainingQuantity());
            ProductionProcess productionProcess = liftMontaj.getProductionProcess();

            liftMontaj.completeOperation(request.getCompletedQuantity());
            montajHelper.saveLiftMontajWithoutReturn(liftMontaj);

            BoyaVePaketleme nextOperation = boyaPaketHelper.findBoyaVePaketlemeByProductionProcessAndOperationType(productionProcess, BoyaPaketOperationType.BOYA);
            nextOperation.updateNextOperation(liftMontaj.getLastCompletedQty());
            boyaPaketHelper.saveBoyaVePaketlemeWithoutReturn(nextOperation);

        return methodHelper.createResponse(SuccessMessages.BASLIK_TAKMA_UPDATED, HttpStatus.OK, null);

    }


    public ResponseMessage<String> testOperation(Long operationId, @Valid MontajRequest request) {
            BlokLiftMontaj blokLiftMontajOperation= montajHelper.findBlokLiftOperationById(operationId);
            methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(),blokLiftMontajOperation.getRemainingQuantity());
            ProductionProcess productionProcess = blokLiftMontajOperation.getProductionProcess();

            if(request.getLastCompletedScrapCount()>0){
                blokLiftMontajOperation.setScrapCountAfterTest(request.getLastCompletedScrapCount() + blokLiftMontajOperation.getScrapCountAfterTest());
                blokLiftMontajOperation.setLastCompletedScrapCount(request.getLastCompletedScrapCount());
                 montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontajOperation);
            }

            if(request.getCompletedQuantity()>0){
                blokLiftMontajOperation.completeOperation(request.getCompletedQuantity());
                montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontajOperation);
                BoyaVePaketleme nextOperation = boyaPaketHelper.findBoyaVePaketlemeByProductionProcessAndOperationType(productionProcess, BoyaPaketOperationType.BOYA);
                nextOperation.updateNextOperation(blokLiftMontajOperation.getLastCompletedQty());
                boyaPaketHelper.saveBoyaVePaketlemeWithoutReturn(nextOperation);

            }

        return methodHelper.createResponse(SuccessMessages.TEST_OPERATION_UPDATED, HttpStatus.OK, null);
    }


    public ResponseMessage<String> removeLastChangeFromBlokliftMontaj(Long operationId) {
        BlokLiftMontaj blokLiftMontaj = montajHelper.findBlokLiftOperationById(operationId);
        ProductionProcess productionProcess = blokLiftMontaj.getProductionProcess();
        BlokLiftOperationType operationType = blokLiftMontaj.getOperationType();
        int lastCompletedQty = blokLiftMontaj.getLastCompletedQty();
        OrderType orderType = productionProcess.getOrder().getOrderType();

        switch (operationType) {
            case BORU_KAPAMA:
                handleRemoveLastChangeFromBoruKapama(blokLiftMontaj, productionProcess, lastCompletedQty, orderType);
                break;
            case BORU_KAYNAK:
                handleRemoveLastChangeFromBoruKaynak(blokLiftMontaj, productionProcess, lastCompletedQty);
                break;
            case BLOK_LIFT_MONTAJ:
                handleRemoveLastChangeFromBlokLiftMontaj(blokLiftMontaj, productionProcess, lastCompletedQty, orderType);
                break;
            case GAZ_DOLUM:
                handleRemoveLastChangeFromGazDolum(blokLiftMontaj, productionProcess, lastCompletedQty);
                break;
            case TEST:
                handleRemoveLastChangeFromTest(blokLiftMontaj, productionProcess, lastCompletedQty);
                break;
            default:
                throw new BadRequestException(String.format(ErrorMessages.INVALID_OPERATION_TYPE, operationType));
        }

        return methodHelper.createResponse(SuccessMessages.REMOVE_LAST_CHANGE, HttpStatus.OK, null);
    }

    private void handleRemoveLastChangeFromBoruKapama(BlokLiftMontaj blokLiftMontaj, ProductionProcess productionProcess, int lastCompletedQty, OrderType orderType) {
        if(orderType.equals(OrderType.BLOKLIFT)){
            BlokLiftMontaj nextOperation = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.GAZ_DOLUM);
            nextOperation.removeLastFromNextOperation(lastCompletedQty);
            montajHelper.saveBlokLiftMontajWithoutReturn(nextOperation);
            blokLiftMontaj.removeLastCompletedQty();
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
        }else {
            BlokLiftMontaj nextOperation = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BORU_KAYNAK);
            nextOperation.removeLastFromNextOperation(lastCompletedQty);
            montajHelper.saveBlokLiftMontajWithoutReturn(nextOperation);
            blokLiftMontaj.removeLastCompletedQty();
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
        }
    }

    private void handleRemoveLastChangeFromBoruKaynak(BlokLiftMontaj blokLiftMontaj, ProductionProcess productionProcess, int lastCompletedQty) {
        BlokLiftMontaj nextOperation = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BLOK_LIFT_MONTAJ);
        nextOperation.removeLastPipeCount(lastCompletedQty);
        montajHelper.saveBlokLiftMontajWithoutReturn(nextOperation);
        montajHelper.compareMilCountAndPipeCountForBLMontaj(nextOperation);
        blokLiftMontaj.removeLastCompletedQty();
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
    }

    private void handleRemoveLastChangeFromBlokLiftMontaj(BlokLiftMontaj blokLiftMontaj, ProductionProcess productionProcess, int lastCompletedQty, OrderType orderType) {
        if(orderType.equals(OrderType.BLOKLIFT)) {
            BlokLiftMontaj nextOperation = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BORU_KAPAMA);
            nextOperation.removeLastFromNextOperation(lastCompletedQty);
            montajHelper.saveBlokLiftMontajWithoutReturn(nextOperation);

            blokLiftMontaj.removeLastCompletedQty();
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
        }else {
            KaliteKontrol afterMontaj =  kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MONTAJ);
            afterMontaj.removeLastFromNextOperation(lastCompletedQty);
            kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterMontaj);

            blokLiftMontaj.removeLastCompletedQty();
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
        }
    }

    private void handleRemoveLastChangeFromGazDolum(BlokLiftMontaj blokLiftMontaj, ProductionProcess productionProcess, int lastCompletedQty) {
        BlokLiftMontaj nextOperation = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.TEST);
        nextOperation.removeLastFromNextOperation(lastCompletedQty);
        montajHelper.saveBlokLiftMontajWithoutReturn(nextOperation);
        blokLiftMontaj.removeLastCompletedQty();
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
    }

    private void handleRemoveLastChangeFromTest(BlokLiftMontaj blokLiftMontaj, ProductionProcess productionProcess, int lastCompletedQty) {
        BoyaVePaketleme nextOperation = boyaPaketHelper.findBoyaVePaketlemeByProductionProcessAndOperationType(productionProcess, BoyaPaketOperationType.BOYA);
        nextOperation.removeLastFromNextOperation(lastCompletedQty);
        boyaPaketHelper.saveBoyaVePaketlemeWithoutReturn(nextOperation);

        blokLiftMontaj.removeLastCompletedQty();
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
    }

    public ResponseMessage<String> removeLastChangeFromLiftMontaj(Long operationId) {
        LiftMontaj liftMontaj = montajHelper.findLiftOperationById(operationId);
        ProductionProcess productionProcess = liftMontaj.getProductionProcess();
        LiftMontajOperationTye operationType = liftMontaj.getOperationType();
        int lastCompletedQty = liftMontaj.getLastCompletedQty();
        OrderType orderType = productionProcess.getOrder().getOrderType();

        switch (operationType) {
            case BORU_KAPAMA:
                handleRemoveLastChangeFromBoruKapamaForLiftMontaj(liftMontaj, productionProcess, lastCompletedQty, orderType);
                break;
            case BORU_KAYNAK:
                handleRemoveLastChangeFromBoruKaynakForLiftMontaj(liftMontaj, productionProcess, lastCompletedQty);
                break;
            case LIFT_MONTAJ:
                handleRemoveLastChangeFromLiftMontajForLiftMontaj(liftMontaj, productionProcess, lastCompletedQty, orderType);
                break;
            case GAZ_DOLUM:
                handleRemoveLastChangeFromGazDolumForLiftMontaj(liftMontaj, productionProcess, lastCompletedQty);
                break;
            case BASLIK_TAKMA:
                handleRemoveLastChangeFromBaslikTakmaForLiftMontaj(liftMontaj, productionProcess, lastCompletedQty);
                break;
            default:
                throw new BadRequestException(String.format(ErrorMessages.INVALID_OPERATION_TYPE, operationType));
        }

        return methodHelper.createResponse(SuccessMessages.REMOVE_LAST_CHANGE, HttpStatus.OK, null);
    }

    private void handleRemoveLastChangeFromBoruKapamaForLiftMontaj(LiftMontaj liftMontaj, ProductionProcess productionProcess, int lastCompletedQty, OrderType orderType) {
        LiftMontaj nextOperation = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.BORU_KAYNAK);
        nextOperation.removeLastFromNextOperation(lastCompletedQty);
        montajHelper.saveLiftMontajWithoutReturn(nextOperation);
        liftMontaj.removeLastCompletedQty();
        montajHelper.saveLiftMontajWithoutReturn(liftMontaj);
    }

    private void handleRemoveLastChangeFromBoruKaynakForLiftMontaj(LiftMontaj liftMontaj, ProductionProcess productionProcess, int lastCompletedQty) {
        LiftMontaj nextOperation = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.LIFT_MONTAJ);
        nextOperation.rollbackNextPipeCount(lastCompletedQty);
        montajHelper.saveLiftMontajWithoutReturn(nextOperation);
        montajHelper.compareMilCountAndPipeCountForLiftMontaj(nextOperation);
        liftMontaj.removeLastCompletedQty();
        montajHelper.saveLiftMontajWithoutReturn(liftMontaj);
    }

    private void handleRemoveLastChangeFromLiftMontajForLiftMontaj(LiftMontaj liftMontaj, ProductionProcess productionProcess, int lastCompletedQty, OrderType orderType) {
        KaliteKontrol afterMontaj =  kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MONTAJ);
        afterMontaj.removeLastFromNextOperation(lastCompletedQty);
        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterMontaj);
        liftMontaj.removeLastCompletedQty();
        montajHelper.saveLiftMontajWithoutReturn(liftMontaj);
    }

    private void handleRemoveLastChangeFromGazDolumForLiftMontaj(LiftMontaj liftMontaj, ProductionProcess productionProcess, int lastCompletedQty) {
        LiftMontaj nextOperation = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.BASLIK_TAKMA);
        nextOperation.removeLastFromNextOperation(lastCompletedQty);
        montajHelper.saveLiftMontajWithoutReturn(nextOperation);
        liftMontaj.removeLastCompletedQty();
        montajHelper.saveLiftMontajWithoutReturn(liftMontaj);
    }

    private void handleRemoveLastChangeFromBaslikTakmaForLiftMontaj(LiftMontaj liftMontaj, ProductionProcess productionProcess, int lastCompletedQty) {
        BoyaVePaketleme nextOperation = boyaPaketHelper.findBoyaVePaketlemeByProductionProcessAndOperationType(productionProcess, BoyaPaketOperationType.BOYA);
        nextOperation.removeLastFromNextOperation(lastCompletedQty);
        boyaPaketHelper.saveBoyaVePaketlemeWithoutReturn(nextOperation);
        liftMontaj.removeLastCompletedQty();
        montajHelper.saveLiftMontajWithoutReturn(liftMontaj);
    }
}
