package com.project.service.business.process;

import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.BlokLiftOperationType;
import com.project.domain.concretes.business.process._enums.LiftMontajOperationTye;
import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.domain.enums.OrderType;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.process.MontajRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.MontajHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MontajService {


    private final MontajHelper montajHelper;
    private final MethodHelper methodHelper;


    public ResponseMessage<String> boruKapamaOperation(Long operationId, @Valid MontajRequest request) {
        BlokLiftMontaj blokLiftBoruKapama;
        LiftMontaj liftBoruKapama;

        if(request.getOrderType().equals(OrderType.DAMPER)){


            blokLiftBoruKapama= montajHelper.findBlokLiftOperationById(operationId);
            ProductionProcess productionProcess = blokLiftBoruKapama.getProductionProcess();

            blokLiftBoruKapama.completeOperation(request.getCompletedQuantity());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftBoruKapama);

            BlokLiftMontaj boruKaynak = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BORU_KAYNAK);

            boruKaynak.updateNextOperation(blokLiftBoruKapama.getLastCompletedQty());
            montajHelper.saveBlokLiftMontajWithoutReturn(boruKaynak);

        }else{
            liftBoruKapama = montajHelper.findLiftOperationById(operationId);
            ProductionProcess productionProcess = liftBoruKapama.getProductionProcess();
            liftBoruKapama.completeOperation(request.getCompletedQuantity());
            montajHelper.saveLiftMontajWithoutReturn(liftBoruKapama);

            LiftMontaj boruKaynak = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.BORU_KAYNAK);

            boruKaynak.updateNextOperation(liftBoruKapama.getLastCompletedQty());
            montajHelper.saveLiftMontajWithoutReturn(boruKaynak);
        }

        return methodHelper.createResponse(SuccessMessages.BORU_COMPLETED, HttpStatus.OK, null);
    }




    public ResponseMessage<String> boruKaynakOperation(Long operationId, @Valid MontajRequest request) {

        BlokLiftMontaj blokLiftBoruKaynak;
        LiftMontaj liftBoruKaynak;

        if(request.getOrderType().equals(OrderType.DAMPER)){

            blokLiftBoruKaynak= montajHelper.findBlokLiftOperationById(operationId);
            ProductionProcess productionProcess = blokLiftBoruKaynak.getProductionProcess();
            blokLiftBoruKaynak.completeOperation(request.getCompletedQuantity());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftBoruKaynak);

            BlokLiftMontaj blokLiftMontaj = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BLOK_LIFT_MONTAJ);
            blokLiftMontaj.updateNextPipeOperation(blokLiftBoruKaynak.getLastCompletedQty());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);

            blokLiftMontaj.updateAccordingToPipeAndMilCount();
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);


        }else{
            liftBoruKaynak=montajHelper.findLiftOperationById(operationId);

            ProductionProcess productionProcess = liftBoruKaynak.getProductionProcess();

            liftBoruKaynak.completeOperation(request.getCompletedQuantity());

            montajHelper.saveLiftMontajWithoutReturn(liftBoruKaynak);

            LiftMontaj liftMontaj = montajHelper.findLiftMontajByProductionProcess(productionProcess, LiftMontajOperationTye.LIFT_MONTAJ);

            liftMontaj.updateNextPipeOperation(liftBoruKaynak.getLastCompletedQty());
            montajHelper.saveLiftMontajWithoutReturn(liftMontaj);

            liftMontaj.updateAccordingToPipeAndMilCount();
            montajHelper.saveLiftMontajWithoutReturn(liftMontaj);
        }
        return methodHelper.createResponse(SuccessMessages.BORU_COMPLETED,HttpStatus.OK,null);
    }
}
