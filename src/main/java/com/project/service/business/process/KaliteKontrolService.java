package com.project.service.business.process;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.BoyaPaketOperationType;
import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.domain.concretes.business.process.boyavepaket.BoyaVePaketleme;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.process.polisajamiri.PolisajImalat;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.domain.enums.OrderType;
import com.project.payload.mappers.KaliteKontrolMapper;
import com.project.payload.mappers.OrderMapper;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.process.KaliteKontrolRequest;
import com.project.payload.response.business.MultipleResponses;
import com.project.payload.response.business.OrderResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.process.KaliteKontrolResponse;
import com.project.payload.response.business.process.ProductionProcessResponse;
import com.project.repository.business.process.KaliteKontrolRepository;
import com.project.repository.business.process.TalasliImalatRepository;
import com.project.service.helper.BoyaPaketHelper;
import com.project.service.helper.KaliteKontrolHelper;
import com.project.service.helper.PolisajHelper;
import com.project.service.helper.TalasliHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KaliteKontrolService {

    private final KaliteKontrolRepository kaliteKontrolRepository;
    private final KaliteKontrolHelper kaliteKontrolHelper;
    private final TalasliHelper talasliHelper;
    private final OrderMapper orderMapper;
    private final KaliteKontrolMapper kaliteKontrolMapper;
    private final BoyaPaketHelper boyaPaketHelper;
    private final PolisajHelper polisajHelper;

    public ResponseMessage<String> afterMilTaslamaKaliteKontrol(@Valid KaliteKontrolRequest request, Long stageId) {
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();
        TalasliImalat ezme = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.EZME);
        TalasliImalat milTaslama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);
        TalasliImalat isilIslem = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.ISIL_ISLEM);

        kaliteKontrol.completedPart(request.getApproveCount(), request.getScrapCount(), request.getReturnedToIsilIslem(), request.getReturnedToMilTaslama());



        if(request.getApproveCount()>0){
            ezme.updateNextOperation(request.getApproveCount());
            talasliHelper.saveTalasliImalatWithoutReturn(ezme);
        }

        if (request.getReturnedToIsilIslem()>0){
            isilIslem.updateNextOperation(request.getReturnedToIsilIslem());
            talasliHelper.saveTalasliImalatWithoutReturn(isilIslem);
        }
        if (request.getReturnedToMilTaslama()>0){
            milTaslama.updateNextOperation(request.getReturnedToMilTaslama());
            talasliHelper.saveTalasliImalatWithoutReturn(milTaslama);
        }

        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.KALITE_KONTROL_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public ResponseMessage<String> afterEzmeKaliteKontrol(@Valid KaliteKontrolRequest request, Long stageId) {
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();

        kaliteKontrol.completedPart(request.getApproveCount(), request.getScrapCount(), request.getReturnedToIsilIslem(), request.getReturnedToMilTaslama());

        if(request.getApproveCount()>0){
            PolisajImalat polisaj = productionProcess.getPolisajOperation();
            polisaj.updateNextOperation(request.getApproveCount());
            polisajHelper.savePolisajWithoutReturn(polisaj);
        }

        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.KALITE_KONTROL_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();

    }


    public ResponseMessage<String> afterMontajKaliteKontrol(@Valid KaliteKontrolRequest request, Long stageId) {
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();

        kaliteKontrol.completedPart(request.getApproveCount(), request.getScrapCount(), request.getReturnedToIsilIslem(), request.getReturnedToMilTaslama());

        if (request.getApproveCount() > 0) {
            BoyaVePaketleme boyaVePaketleme = boyaPaketHelper.findBoyaVePaketlemeByProductionProcess(productionProcess, BoyaPaketOperationType.PAKETLEME);
            boyaVePaketleme.updateNextOperation(request.getApproveCount());
            boyaPaketHelper.saveBoyaVePaketlemeWithoutReturn(boyaVePaketleme);
        }

        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.KALITE_KONTROL_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
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
}
