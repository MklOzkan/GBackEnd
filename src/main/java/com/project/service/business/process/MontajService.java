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

    public ResponseMessage<String> boruKapamaOperation(Long operationId, MontajRequest request) {
        BlokLiftMontaj blokLiftBoruKapama;
        LiftMontaj liftBoruKapama;

        if (request.getOrderType().equals(OrderType.DAMPER)) {


            blokLiftBoruKapama = montajHelper.findBlokLiftOperationById(operationId);
            ProductionProcess productionProcess = blokLiftBoruKapama.getProductionProcess();

            blokLiftBoruKapama.completeOperation(request.getCompletedQuantity());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftBoruKapama);

            BlokLiftMontaj boruKaynak = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BORU_KAYNAK);

            boruKaynak.updateNextOperation(blokLiftBoruKapama.getLastCompletedQty());
            montajHelper.saveBlokLiftMontajWithoutReturn(boruKaynak);

        } else {
            liftBoruKapama = montajHelper.findLiftOperationById(operationId);
            ProductionProcess productionProcess = liftBoruKapama.getProductionProcess();
            liftBoruKapama.completeOperation(request.getCompletedQuantity());
            montajHelper.saveLiftMontajWithoutReturn(liftBoruKapama);

            LiftMontaj boruKaynak = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.BORU_KAYNAK);

            boruKaynak.updateNextOperation(liftBoruKapama.getLastCompletedQty());
            montajHelper.saveLiftMontajWithoutReturn(boruKaynak);
        }

        return methodHelper.createResponse(SuccessMessages.BORU_KAPAMA_COMPLETED, HttpStatus.OK, null);
    }


    public ResponseMessage<String> boruKaynakOperation(Long operationId, MontajRequest request) {

        BlokLiftMontaj blokLiftBoruKaynak;
        LiftMontaj liftBoruKaynak;

        if (request.getOrderType().equals(OrderType.DAMPER)) {

            blokLiftBoruKaynak = montajHelper.findBlokLiftOperationById(operationId);
            ProductionProcess productionProcess = blokLiftBoruKaynak.getProductionProcess();
            blokLiftBoruKaynak.completeOperation(request.getCompletedQuantity());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftBoruKaynak);

            BlokLiftMontaj blokLiftMontaj = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BLOK_LIFT_MONTAJ);
            blokLiftMontaj.updateNextPipeOperation(blokLiftBoruKaynak.getLastCompletedQty());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);

            blokLiftMontaj.updateAccordingToPipeAndMilCount();
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);


        } else {
            liftBoruKaynak = montajHelper.findLiftOperationById(operationId);

            ProductionProcess productionProcess = liftBoruKaynak.getProductionProcess();

            liftBoruKaynak.completeOperation(request.getCompletedQuantity());

            montajHelper.saveLiftMontajWithoutReturn(liftBoruKaynak);

            LiftMontaj liftMontaj = montajHelper.findLiftMontajByProductionProcess(productionProcess, LiftMontajOperationTye.LIFT_MONTAJ);

            liftMontaj.updateNextPipeOperation(liftBoruKaynak.getLastCompletedQty());
            montajHelper.saveLiftMontajWithoutReturn(liftMontaj);

            liftMontaj.updateAccordingToPipeAndMilCount();
            montajHelper.saveLiftMontajWithoutReturn(liftMontaj);
        }
        return methodHelper.createResponse(SuccessMessages.BORU_KAYANAK_COMPLETED, HttpStatus.OK, null);
    }


    public ResponseMessage<String> montajOperation(Long operationId, MontajRequest request) {
        BlokLiftMontaj blokLiftMontaj;
        LiftMontaj liftMontaj;
        if(request.getOrderType().equals(OrderType.DAMPER)) {
            blokLiftMontaj = montajHelper.findBlokLiftOperationById(operationId);
            ProductionProcess productionProcess = blokLiftMontaj.getProductionProcess();

            blokLiftMontaj.completeOperation(request.getCompletedQuantity());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);

            KaliteKontrol afterMontaj =  kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MONTAJ);
            afterMontaj.nextOperationByKaliteKontrol(blokLiftMontaj.getLastCompletedQty());
            kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterMontaj);

        }else if(request.getOrderType().equals(OrderType.BLOKLIFT)){
            blokLiftMontaj = montajHelper.findBlokLiftOperationById(operationId);
            ProductionProcess productionProcess = blokLiftMontaj.getProductionProcess();

            blokLiftMontaj.completeOperation(request.getCompletedQuantity());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);

           BlokLiftMontaj blokLiftBoruKapama = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BORU_KAPAMA);
            blokLiftBoruKapama.updateNextOperation(blokLiftMontaj.getLastCompletedQty());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftBoruKapama);

        }else{

            liftMontaj = montajHelper.findLiftOperationById(operationId);
            ProductionProcess productionProcess = liftMontaj.getProductionProcess();

            liftMontaj.completeOperation(request.getCompletedQuantity());
            montajHelper.saveLiftMontajWithoutReturn(liftMontaj);

            KaliteKontrol afterMontaj =  kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MONTAJ);
            afterMontaj.nextOperationByKaliteKontrol(liftMontaj.getLastCompletedQty());
            kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterMontaj);
        }

        return methodHelper.createResponse(SuccessMessages.BORU_MONTAJ_COMPLETED, HttpStatus.OK, null);
    }


    public ResponseMessage<String> blokLiftBoruKapamaOperation(Long operationId, MontajRequest request) {
        BlokLiftMontaj blokLiftMontaj = montajHelper.findBlokLiftOperationById(operationId);
        ProductionProcess productionProcess = blokLiftMontaj.getProductionProcess();

        blokLiftMontaj.completeOperation(request.getCompletedQuantity());
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);

        BlokLiftMontaj nextOperation = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BORU_KAYNAK);
        nextOperation.updateNextOperation(blokLiftMontaj.getLastCompletedQty());
        montajHelper.saveBlokLiftMontajWithoutReturn(nextOperation);

        return methodHelper.createResponse(SuccessMessages.BORU_KAPAMA_COMPLETED, HttpStatus.OK, null);
    }


    public ResponseMessage<String> gazDolumOperation(Long operationId, MontajRequest request) {
        BlokLiftMontaj blokLiftMontaj;

        if (request.getOrderType().equals(OrderType.DAMPER) || request.getOrderType().equals(OrderType.BLOKLIFT)) {
            blokLiftMontaj = montajHelper.findBlokLiftOperationById(operationId);
            ProductionProcess productionProcess = blokLiftMontaj.getProductionProcess();

            blokLiftMontaj.completeOperation(request.getCompletedQuantity());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);

            BlokLiftMontaj nextOperation = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.TEST);
            nextOperation.updateNextOperation(blokLiftMontaj.getLastCompletedQty());
            montajHelper.saveBlokLiftMontajWithoutReturn(nextOperation);
        } else {
            LiftMontaj liftMontaj = montajHelper.findLiftOperationById(operationId);
            ProductionProcess productionProcess = liftMontaj.getProductionProcess();

            liftMontaj.completeOperation(request.getCompletedQuantity());
            montajHelper.saveLiftMontajWithoutReturn(liftMontaj);

            LiftMontaj nextOperation = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.BASLIK_TAKMA);
            nextOperation.updateNextOperation(liftMontaj.getLastCompletedQty());
            montajHelper.saveLiftMontajWithoutReturn(nextOperation);

        }
        return methodHelper.createResponse(SuccessMessages.GAZ_DOLUM_COMPLETED, HttpStatus.OK, null);
    }


    public ResponseMessage<String> baslikTakmaOperation(Long operationId, @Valid MontajRequest request) {
        LiftMontaj liftMontaj = montajHelper.findLiftOperationById(operationId);
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
                throw new IllegalArgumentException("Unknown operation type: " + operationType);
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
        nextOperation.removeLastFromNextOperation(lastCompletedQty);
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

        KaliteKontrol afterMontaj =  kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MONTAJ);
        afterMontaj.removeLastFromNextOperation(lastCompletedQty);
        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterMontaj);

        blokLiftMontaj.removeLastCompletedQty();
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
    }

    private void handleRemoveLastChangeFromGazDolum(BlokLiftMontaj blokLiftMontaj, ProductionProcess productionProcess, int lastCompletedQty) {
        BlokLiftMontaj nextOperation = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.TEST);
        nextOperation.removeLastFromNextOperation(lastCompletedQty);
        montajHelper.saveBlokLiftMontajWithoutReturn(nextOperation);
        blokLiftMontaj.removeLastCompletedQty();
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
    }

    private void handleRemoveLastChangeFromTest(BlokLiftMontaj blokLiftMontaj, ProductionProcess productionProcess, int lastCompletedQty) {
        blokLiftMontaj.removeLastCompletedQty();
        montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);


    }

//    public ResponseMessage<String> removeLastChangeFromLiftMontaj(Long operationId) {
//    }
}
