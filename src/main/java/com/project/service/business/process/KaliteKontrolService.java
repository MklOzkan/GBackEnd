package com.project.service.business.process;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.*;
import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import com.project.domain.concretes.business.process.boyavepaket.BoyaVePaketleme;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.domain.concretes.business.process.polisajamiri.PolisajImalat;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.domain.enums.OrderType;
import com.project.exception.ConflictException;
import com.project.payload.mappers.KaliteKontrolMapper;
import com.project.payload.mappers.OrderMapper;
import com.project.payload.mappers.TalasliMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.process.KaliteKontrolRequest;
import com.project.payload.response.business.MultipleResponses;
import com.project.payload.response.business.OrderResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.process.KaliteKontrolResponse;
import com.project.payload.response.business.process.ProductionProcessResponse;
import com.project.repository.business.process.KaliteKontrolRepository;
import com.project.service.helper.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KaliteKontrolService {

    private final KaliteKontrolRepository kaliteKontrolRepository;
    private final KaliteKontrolHelper kaliteKontrolHelper;
    private final TalasliHelper talasliHelper;
    private final OrderMapper orderMapper;
    private final PolisajHelper polisajHelper;
    private final MontajHelper montajHelper;
    private final KaliteKontrolMapper kaliteKontrolMapper;
    private final MethodHelper methodHelper;
    private final TalasliMapper talasliMapper;

    //paslanmaz icin mil taslama sonrası kalite kontrol
    public ResponseMessage<String> afterMilTaslamaKaliteKontrol(@Valid KaliteKontrolRequest request, Long stageId) {
        checkIsCompletedMoreThanRemainingQuantity(stageId, request);

        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();
        TalasliImalat ezme = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.EZME);

        if (request.getApproveCount() > 0) {
            kaliteKontrol.approvedPart(request.getApproveCount());
        }
        if (request.getScrapCount() > 0) {
            kaliteKontrol.scrapPart(request.getScrapCount());
        }
        if (request.getReturnedToMilTaslama() > 0) {
            TalasliImalat miltaslama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);
            kaliteKontrol.returnedToMilTaslama(request.getReturnedToMilTaslama());
            miltaslama.returnedToOperation(request.getReturnedToMilTaslama());
            talasliHelper.saveTalasliImalatWithoutReturn(miltaslama);
        }
        updateNextOperation(ezme, request.getApproveCount());
        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.KALITE_KONTROL_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    private void updateNextOperation(TalasliImalat operation, int count) {
        if (count > 0) {
            operation.updateNextOperation(count);
            talasliHelper.saveTalasliImalatWithoutReturn(operation);
        }
    }

    //paslanmaz icin ezme sonrası kalite kontrol
    public ResponseMessage<String> afterEzmeKaliteKontrol(@Valid KaliteKontrolRequest request, Long stageId) {
        checkIsCompletedMoreThanRemainingQuantity(stageId, request);
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();
        TalasliImalat ezme = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.EZME);
        TalasliImalat milTaslama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);
        KaliteKontrol afterMilTaslama = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MIL_TASLAMA);

        int approveCount = request.getApproveCount();

        kaliteKontrol.completedPart(request);
        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
        if (request.getReturnedToMilTaslama()>0){
            milTaslama.returnedToOperation(kaliteKontrol.getReturnedToMilTaslama());
            talasliHelper.saveTalasliImalatWithoutReturn(milTaslama);
            afterMilTaslama.rollbackLastApproveCount(kaliteKontrol.getReturnedToMilTaslama());
            kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterMilTaslama);
            ezme.decreaseCompletedQuantity(kaliteKontrol.getReturnedToMilTaslama());
            talasliHelper.saveTalasliImalatWithoutReturn(ezme);
        }


        if(request.getApproveCount()>0){
            LiftMontaj liftMontaj  = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.LIFT_MONTAJ);
            liftMontaj.setMilCount(approveCount);
            montajHelper.saveLiftMontajWithoutReturn(liftMontaj);
            montajHelper.compareMilCountAndPipeCountForLiftMontaj(liftMontaj);
        }
        return ResponseMessage.<String>builder()
                .message(SuccessMessages.KALITE_KONTROL_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    //montaj sonrası kalite kontrol
    public ResponseMessage<String> afterMontajKaliteKontrol(KaliteKontrolRequest request, Long stageId) {
        checkIsCompletedMoreThanRemainingQuantity(stageId, request);
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();

        handleKaliteKontrolUpdates(kaliteKontrol, request);

        updateNextMontaj(productionProcess, request.getApproveCount());

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.KALITE_KONTROL_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    private void handleKaliteKontrolUpdates(KaliteKontrol kaliteKontrol, KaliteKontrolRequest request) {
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();
        TalasliImalat isilIslem;
        TalasliImalat milTaslama;
        PolisajImalat polisaj = productionProcess.getPolisajOperation();

        if (request.getApproveCount() > 0) {
            kaliteKontrol.approvedPart(request.getApproveCount());
        }
        if (request.getScrapCount() > 0) {
            kaliteKontrol.scrapPart(request.getScrapCount());
        }
        if (request.getReturnedToIsilIslem() > 0) {
            isilIslem = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.ISIL_ISLEM);
            kaliteKontrol.returnedToIsilIslem(request.getReturnedToIsilIslem());
            isilIslem.returnedToOperation(request.getReturnedToIsilIslem());
            polisaj.decreaseCompletedQuantity(request.getReturnedToIsilIslem());
            talasliHelper.saveTalasliImalatWithoutReturn(isilIslem);
            polisajHelper.savePolisajWithoutReturn(polisaj);
        }
        if (request.getReturnedToMilTaslama() > 0) {
            isilIslem = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.ISIL_ISLEM);
            milTaslama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);
            kaliteKontrol.returnedToMilTaslama(request.getReturnedToMilTaslama());
            milTaslama.returnedToOperation(request.getReturnedToMilTaslama());
            isilIslem.decreaseCompletedQuantity(request.getReturnedToMilTaslama());
            polisaj.decreaseCompletedQuantity(request.getReturnedToMilTaslama());
            talasliHelper.saveTalasliImalatWithoutReturn(milTaslama);
            talasliHelper.saveTalasliImalatWithoutReturn(isilIslem);
            polisajHelper.savePolisajWithoutReturn(polisaj);
        }
        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
    }

    private void updateNextMontaj(ProductionProcess productionProcess, int approveCount) {
        OrderType orderType = productionProcess.getOrder().getOrderType();
        if (orderType.equals(OrderType.DAMPER)) {
            BlokLiftMontaj blokLiftMontaj = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.GAZ_DOLUM);
            blokLiftMontaj.updateNextOperation(approveCount);
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
        } else {
            LiftMontaj liftMontaj = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.GAZ_DOLUM);
            liftMontaj.updateNextOperation(approveCount);
            montajHelper.saveLiftMontajWithoutReturn(liftMontaj);
        }
    }

    public MultipleResponses<OrderResponse, ProductionProcessResponse, List<KaliteKontrolResponse>> getKaliteKontrolStages(Long id) {

        ProductionProcess productionProcess = talasliHelper.findProductionProcessById(id);
        List<KaliteKontrol> kaliteKontrolList = kaliteKontrolRepository.findAllByProductionProcess(productionProcess);
        Order order = productionProcess.getOrder();
        OrderType orderType = order.getOrderType();
        return MultipleResponses.<OrderResponse, ProductionProcessResponse, List<KaliteKontrolResponse>>builder()
                .returnBody(orderMapper.mapOrderToOrderResponse(order))

                .build();
    }
    //polisan sonrası kalite kontrol
    public ResponseMessage<String> afterPolisajKaliteKontrol(KaliteKontrolRequest request, Long stageId) {
        checkIsCompletedMoreThanRemainingQuantity(stageId, request);
        KaliteKontrol kaliteKontrol=kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess=kaliteKontrol.getProductionProcess();
        OrderType orderType=productionProcess.getOrder().getOrderType();
        BlokLiftMontaj blokLiftMontaj;
        LiftMontaj liftMontaj;

        handleKaliteKontrolUpdates(kaliteKontrol, request);

        if(orderType.equals(OrderType.BLOKLIFT)||orderType.equals(OrderType.DAMPER)){
            blokLiftMontaj=montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BLOK_LIFT_MONTAJ);
            blokLiftMontaj.updateNextMilOperation(request.getApproveCount());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
            montajHelper.compareMilCountAndPipeCountForBLMontaj(blokLiftMontaj);
        }else{
            liftMontaj=montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.LIFT_MONTAJ);
            liftMontaj.updateNextMilOperation(request.getApproveCount());
            montajHelper.saveLiftMontajWithoutReturn(liftMontaj);
            montajHelper.compareMilCountAndPipeCountForLiftMontaj(liftMontaj);
        }

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.KALITE_KONTROL_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<String> rollbackAfterPolisajKaliteKontrol(Long stageId, KaliteKontrolRequest request) {
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();
        PolisajImalat polisaj = productionProcess.getPolisajOperation();
        TalasliImalat isilIslem;



        switch (request.getOperationField()){
            case "Mil_Taslama":
                TalasliImalat milTaslama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);
                isilIslem = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.ISIL_ISLEM);
                milTaslama.rollbackOperation(kaliteKontrol.getLastReturnedToMilTaslama());
                isilIslem.increaseCompletedQuantity(kaliteKontrol.getLastReturnedToMilTaslama());
                polisaj.increaseCompletedQuantity(kaliteKontrol.getLastReturnedToMilTaslama());
                talasliHelper.saveTalasliImalatWithoutReturn(milTaslama);
                talasliHelper.saveTalasliImalatWithoutReturn(isilIslem);
                polisajHelper.savePolisajWithoutReturn(polisaj);
                kaliteKontrol.rollBackLastReturnedToMilTaslama();
                kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
                break;
            case "Isil_Islem":
                isilIslem= talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.ISIL_ISLEM);
                isilIslem.rollbackOperation(kaliteKontrol.getLastReturnedToIsilIslem());
                polisaj.increaseCompletedQuantity(kaliteKontrol.getLastReturnedToIsilIslem());
                talasliHelper.saveTalasliImalatWithoutReturn(isilIslem);
                polisajHelper.savePolisajWithoutReturn(polisaj);
                kaliteKontrol.rollBackLastReturnedToIsilIslem();
                kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
                break;
            case "Scrap":
                kaliteKontrol.rollBackLastScrap();
                kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
                break;
            case "Approve":
                rollbackApprove(productionProcess, stageId);
                kaliteKontrol.rollBackLastApprove();
                kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
                break;
        }
        return methodHelper.createResponse(SuccessMessages.KALITE_KONTROL_UPDATED, HttpStatus.OK, null);
    }

    private void rollbackApprove(ProductionProcess productionProcess, Long id){
        OrderType orderType = productionProcess.getOrder().getOrderType();
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(id);
        if (orderType.equals(OrderType.LIFT)||orderType.equals(OrderType.PASLANMAZ)){
            LiftMontaj liftMontaj = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.LIFT_MONTAJ);
            liftMontaj.rollbackNextMilCount(kaliteKontrol.getLastApproveCount());
            montajHelper.saveLiftMontajWithoutReturn(liftMontaj);
            montajHelper.compareMilCountAndPipeCountForLiftMontaj(liftMontaj);
        }else{
            BlokLiftMontaj blokLiftMontaj = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.BLOK_LIFT_MONTAJ);
            blokLiftMontaj.rollbackNextMilCount(kaliteKontrol.getLastApproveCount());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
            montajHelper.compareMilCountAndPipeCountForBLMontaj(blokLiftMontaj);
        }
    }

    public ResponseMessage<String> rollbackAfterMontajKaliteKontrol(Long stageId, KaliteKontrolRequest request) {
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();
        if (request.getOperationField().equals("Approve")) {
            System.out.println("approve");
            rollbackApproveForAfterMontaj(productionProcess, stageId);//approve geri al
        } else if(request.getOperationField().equals("Scrap")){
            System.out.println("scrap");
            kaliteKontrol.rollBackLastScrap();//son hurda sayısını geri al
            kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
        }

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.KALITE_KONTROL_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
    }
    //montaj sonrası kalite kontrol içinde approve geri alma
    private void rollbackApproveForAfterMontaj(ProductionProcess productionProcess, Long id){
        OrderType orderType = productionProcess.getOrder().getOrderType();
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(id);
        if (orderType.equals(OrderType.LIFT)||orderType.equals(OrderType.PASLANMAZ)){
            LiftMontaj liftMontaj = montajHelper.findLiftByProductionProcessAndOperationType(productionProcess, LiftMontajOperationTye.GAZ_DOLUM);
            liftMontaj.removeLastFromNextOperation(kaliteKontrol.getLastApproveCount());
            montajHelper.saveLiftMontajWithoutReturn(liftMontaj);

        }else{
            BlokLiftMontaj blokLiftMontaj = montajHelper.findBLByProductionProcessAndOperationType(productionProcess, BlokLiftOperationType.GAZ_DOLUM);
            blokLiftMontaj.rollbackNextMilCount(kaliteKontrol.getLastApproveCount());
            montajHelper.saveBlokLiftMontajWithoutReturn(blokLiftMontaj);

        }
        kaliteKontrol.rollBackLastApprove();
        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
    }

    public ResponseMessage<String> rollbackAfterEzmeKaliteKontrol(Long stageId, KaliteKontrolRequest request) {

        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();

        switch (request.getOperationField()){
            case "Mil_Taslama":
                TalasliImalat milTaslama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);
                TalasliImalat ezme = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.EZME);
                KaliteKontrol afterMilTaslama = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MIL_TASLAMA);
                milTaslama.rollbackOperation(kaliteKontrol.getLastReturnedToMilTaslama());
                afterMilTaslama.rollbackLastApproveCount(kaliteKontrol.getLastReturnedToMilTaslama());
                kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterMilTaslama);
                ezme.increaseCompletedQuantity(kaliteKontrol.getLastReturnedToMilTaslama());
                talasliHelper.saveTalasliImalatWithoutReturn(milTaslama);
                talasliHelper.saveTalasliImalatWithoutReturn(ezme);
                kaliteKontrol.rollBackLastReturnedToMilTaslama();
                kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
                break;

            case "Scrap":
                kaliteKontrol.rollBackLastScrap();
                kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
                break;
            case "Approve":
                rollbackApprove(productionProcess, stageId);
                kaliteKontrol.rollBackLastApprove();
                kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
                break;
        }
        return ResponseMessage.<String>builder()
                .message(SuccessMessages.KALITE_KONTROL_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public MultipleResponses<OrderResponse, ProductionProcessResponse, List<KaliteKontrolResponse>> getAllForOrder(Long orderId) {
        Order order = methodHelper.findOrderById(orderId);
        ProductionProcess productionProcess = order.getProductionProcess();
        List<KaliteKontrol> kaliteKontrolList = kaliteKontrolRepository.findAllByProductionProcess(productionProcess);
        return MultipleResponses.<OrderResponse, ProductionProcessResponse, List<KaliteKontrolResponse>>builder()
                .returnBody(orderMapper.mapOrderToOrderResponse(order))
                .returnBody2(talasliMapper.mapProductionProcessToResponse(productionProcess))
                .returnBody3(kaliteKontrolMapper.mapKaliteKontrolListToResponse(kaliteKontrolList))
                .build();
    }

    public MultipleResponses<OrderResponse, ProductionProcessResponse, KaliteKontrolResponse> getOrderAndStage(Long stageId) {
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();
        Order order = productionProcess.getOrder();
        return MultipleResponses.<OrderResponse, ProductionProcessResponse, KaliteKontrolResponse>builder()
                .returnBody(orderMapper.mapOrderToOrderResponse(order))
                .returnBody3(kaliteKontrolMapper.mapKaliteKontrolToResponse(kaliteKontrol))
                .build();
    }

    public ResponseMessage<String> rollbackAfterMilTaslamaKaliteKontrol(Long stageId, @Valid KaliteKontrolRequest request) {
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();
        TalasliImalat milTaslama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);
        TalasliImalat ezme = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.EZME);
        KaliteKontrol afterMilTaslama = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MIL_TASLAMA);

        switch (request.getOperationField()){
            case "Approve":
                ezme.removeLastFromNextOperation(kaliteKontrol.getLastApproveCount());
                talasliHelper.saveTalasliImalatWithoutReturn(ezme);
                kaliteKontrol.rollBackLastApprove();
                kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
                break;
            case "Scrap":
                kaliteKontrol.rollBackLastScrap();
                kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
                break;
            case "Mil_Taslama":
                milTaslama.rollbackOperation(kaliteKontrol.getLastReturnedToMilTaslama());
                talasliHelper.saveTalasliImalatWithoutReturn(milTaslama);
                kaliteKontrol.rollBackLastReturnedToMilTaslama();
                kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);
                break;
        }
        return ResponseMessage.<String>builder()
                .message(SuccessMessages.KALITE_KONTROL_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    private void checkQuantity(int requestCount, int milCount) {
        if (requestCount > 0 && milCount < requestCount) {
            throw new ConflictException(ErrorMessages.APPROVE_COUNT_MORE_THAN_REMAINING_QUANTITY);
        }
    }

    public void checkIsCompletedMoreThanRemainingQuantity(Long stageId, KaliteKontrolRequest request) {
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        int milCount = kaliteKontrol.getMilCount();

        checkQuantity(request.getApproveCount(), milCount);
        checkQuantity(request.getReturnedToMilTaslama(), milCount);
        checkQuantity(request.getReturnedToIsilIslem(), milCount);
        checkQuantity(request.getScrapCount(), milCount);
    }
}
