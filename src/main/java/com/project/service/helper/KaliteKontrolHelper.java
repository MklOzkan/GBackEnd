package com.project.service.helper;

import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.KaliteKontrolStage;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.repository.business.process.KaliteKontrolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KaliteKontrolHelper {

    private final KaliteKontrolRepository kaliteKontrolRepository;
    private final TalasliHelper talasliHelper;

    public List<KaliteKontrol> findKaliteKontrolsByProductionProcess(ProductionProcess productionProcess) {
        return kaliteKontrolRepository.findAllByProductionProcess(productionProcess);
    }

    public KaliteKontrol findKaliteKontrolByProductionProcess(ProductionProcess productionProcess, KaliteKontrolStage kaliteKontrolStage) {
        return kaliteKontrolRepository.findByProductionProcess(productionProcess, kaliteKontrolStage)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.KALITE_KONTROL_NOT_FOUND));
    }

    public KaliteKontrol saveKaliteKontrolWithReturn(KaliteKontrol kaliteKontrol) {
        return kaliteKontrolRepository.save(kaliteKontrol);
    }

    public void saveKaliteKontrolWithoutReturn(KaliteKontrol kaliteKontrol){
        kaliteKontrolRepository.save(kaliteKontrol);
    }

    public KaliteKontrol findById(Long id) {
        return kaliteKontrolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.KALITE_KONTROL_NOT_FOUND));
    }

}
