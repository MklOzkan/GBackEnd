package com.project.service.business.process;

import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.process.KaliteKontrolRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.process.KaliteKontrolRepository;
import com.project.repository.business.process.TalasliImalatRepository;
import com.project.service.helper.KaliteKontrolHelper;
import com.project.service.helper.TalasliHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KaliteKontrolService {

    private final KaliteKontrolRepository kaliteKontrolRepository;
    private final KaliteKontrolHelper kaliteKontrolHelper;
    private final TalasliHelper talasliHelper;

    public ResponseMessage<String> afterMilTaslamaKaliteKontrol(@Valid KaliteKontrolRequest request, Long stageId) {
        KaliteKontrol kaliteKontrol = kaliteKontrolHelper.findById(stageId);
        ProductionProcess productionProcess = kaliteKontrol.getProductionProcess();
        TalasliImalat ezme = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.EZME);
        TalasliImalat milTaslama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);
        TalasliImalat isilIslem = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.ISIL_ISLEM);

        if(kaliteKontrol.getApproveCount()==null){
            kaliteKontrol.setApproveCount(0);
        }

        if (kaliteKontrol.getScrapCount()==null){
            kaliteKontrol.setScrapCount(0);
        }

        if (kaliteKontrol.getReturnedToIsilIslem()==null){
            kaliteKontrol.setReturnedToIsilIslem(0);
        }

        if (kaliteKontrol.getReturnedToMilTaslama()==null){
            kaliteKontrol.setReturnedToMilTaslama(0);
        }

        if (kaliteKontrol.getMilCount()==0){
            kaliteKontrol.setEndDate(LocalDateTime.now());
        }

        if(request.getApproveCount()>0){
            kaliteKontrol.setApproveCount(kaliteKontrol.getApproveCount()+request.getApproveCount());
            kaliteKontrol.setMilCount(kaliteKontrol.getMilCount()-request.getApproveCount());
            ezme.setRemainingQuantity(ezme.getRemainingQuantity()+request.getApproveCount());
            talasliHelper.saveTalasliImalatWithoutReturn(ezme);
        }
        if (request.getScrapCount()>0){
            kaliteKontrol.setScrapCount(kaliteKontrol.getScrapCount()+request.getScrapCount());
            kaliteKontrol.setMilCount(kaliteKontrol.getMilCount()-request.getScrapCount());
        }
        if (request.getReturnedToIsilIslem()>0){
            kaliteKontrol.setReturnedToIsilIslem(kaliteKontrol.getReturnedToIsilIslem()+request.getReturnedToIsilIslem());
            kaliteKontrol.setMilCount(kaliteKontrol.getMilCount()-request.getReturnedToIsilIslem());
            isilIslem.setRemainingQuantity(isilIslem.getRemainingQuantity()+request.getReturnedToIsilIslem());
            talasliHelper.saveTalasliImalatWithoutReturn(isilIslem);
        }
        if (request.getReturnedToMilTaslama()>0){
            kaliteKontrol.setReturnedToMilTaslama(kaliteKontrol.getReturnedToMilTaslama()+request.getReturnedToMilTaslama());
            kaliteKontrol.setMilCount(kaliteKontrol.getMilCount()-request.getReturnedToMilTaslama());
            milTaslama.setRemainingQuantity(milTaslama.getRemainingQuantity()+request.getReturnedToMilTaslama());
            talasliHelper.saveTalasliImalatWithoutReturn(milTaslama);
        }

        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(kaliteKontrol);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.KALITE_KONTROL_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();

    }
}
