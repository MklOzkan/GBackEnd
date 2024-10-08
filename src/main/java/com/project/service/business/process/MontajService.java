package com.project.service.business.process;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.BlokLiftOperationType;
import com.project.domain.concretes.business.process._enums.KaliteKontrolStage;
import com.project.domain.concretes.business.process._enums.LiftMontajOperationTye;
import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.domain.enums.OrderType;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.process.MontajRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.helper.KaliteKontrolHelper;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.MontajHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MontajService {

    private final MontajHelper montajHelper;
    private final MethodHelper methodHelper;
    private final KaliteKontrolHelper kaliteKontrolHelper;

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


}
