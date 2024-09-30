package com.project.service.business.process;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.KaliteKontrolStage;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.process.polisajamiri.PolisajImalat;
import com.project.domain.enums.OrderType;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.process.PolisajRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.process.PolisajImalatRepository;
import com.project.service.helper.KaliteKontrolHelper;
import com.project.service.helper.MontajHelper;
import com.project.service.helper.PolisajHelper;
import com.project.service.helper.TalasliHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PolisajService {

    private final PolisajHelper polisajHelper;
    private final TalasliHelper talasliHelper;
    private final KaliteKontrolHelper  kaliteKontrolHelper;
    private final PolisajImalatRepository  polisajImalatRepository;


    public ResponseMessage<String> updatePolisaj(Long id, @Valid PolisajRequest request) {
        PolisajImalat polisajImalat = polisajHelper.findPolisajById(id);
        ProductionProcess productionProcess = polisajImalat.getProductionProcess();

        KaliteKontrol afterPolisaj = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_POLISAJ);

        polisajImalat.completeOperation(request.getCompletedQuantity());

        afterPolisaj.nextOperationByKaliteKontrol(request.getCompletedQuantity());

        polisajHelper.savePolisajWithoutReturn(polisajImalat);
        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterPolisaj);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.POLISAJ_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();


    }

    public ResponseMessage<String> removeLastChange(Long id) {
        PolisajImalat polisajImalat = polisajHelper.findPolisajById(id);
        ProductionProcess productionProcess = polisajImalat.getProductionProcess();

        KaliteKontrol afterPolisaj = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_POLISAJ);
        afterPolisaj.removeLastFromNextOperation(polisajImalat.getLastCompletedQty());
        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterPolisaj);

        polisajImalat.removeLastCompletedQty();
        polisajHelper.savePolisajWithoutReturn(polisajImalat);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.POLISAJ_UPDATED)
                .httpStatus(HttpStatus.OK).build();



    }
}
