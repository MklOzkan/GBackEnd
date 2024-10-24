package com.project.service.business.process;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.KaliteKontrolStage;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.process.polisajamiri.PolisajImalat;
import com.project.domain.enums.OrderType;
import com.project.exception.ConflictException;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.process.PolisajRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.process.PolisajImalatRepository;
import com.project.service.helper.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PolisajService {

    private final PolisajHelper polisajHelper;
    private final KaliteKontrolHelper  kaliteKontrolHelper;
    private final MethodHelper methodHelper;

    @Transactional
    public ResponseMessage<String> updatePolisaj(Long id, @Valid PolisajRequest request) {
        PolisajImalat polisajImalat = polisajHelper.findPolisajById(id);
        ProductionProcess productionProcess = polisajImalat.getProductionProcess();
        KaliteKontrol afterPolisaj;
        if (productionProcess.getOrder().getOrderType().equals(OrderType.BLOKLIFT)) {
            afterPolisaj = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MONTAJ);
        } else {
            afterPolisaj = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_POLISAJ);
        }

        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(), polisajImalat.getRemainingQuantity());
        polisajImalat.completeOperation(request.getCompletedQuantity());

        afterPolisaj.nextOperationByKaliteKontrol(request.getCompletedQuantity());

        polisajHelper.savePolisajWithoutReturn(polisajImalat);
        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterPolisaj);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.POLISAJ_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @Transactional
    public ResponseMessage<String> removeLastChange(Long id) {
        PolisajImalat polisajImalat = polisajHelper.findPolisajById(id);
        ProductionProcess productionProcess = polisajImalat.getProductionProcess();

        KaliteKontrol afterPolisaj;
        if (productionProcess.getOrder().getOrderType().equals(OrderType.BLOKLIFT)) {
            afterPolisaj = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_MONTAJ);
        } else {
            afterPolisaj = kaliteKontrolHelper.findKaliteKontrolByProductionProcess(productionProcess, KaliteKontrolStage.AFTER_POLISAJ);
        }
        afterPolisaj.removeLastFromNextOperation(polisajImalat.getLastCompletedQty());
        kaliteKontrolHelper.saveKaliteKontrolWithoutReturn(afterPolisaj);

        polisajImalat.removeLastCompletedQty();
        polisajHelper.savePolisajWithoutReturn(polisajImalat);

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.POLISAJ_UPDATED)
                .httpStatus(HttpStatus.OK).build();
    }
}
