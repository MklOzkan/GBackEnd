package com.project.service.helper;

import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.BlokLiftOperationType;
import com.project.domain.concretes.business.process._enums.LiftMontajOperationTye;
import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.repository.business.process.BlokLiftMontajRepository;
import com.project.repository.business.process.LiftMontajRepository;
import com.project.repository.business.process.ProductionProcessRepository;
import com.project.repository.business.process.TalasliImalatRepository;
import com.project.service.business.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MontajHelper {

    private final LiftMontajRepository liftMontajRepository;
    private final BlokLiftMontajRepository blokLiftMontajRepository;
    private final TalasliHelper talasliHelper;
    private final MethodHelper methodHelper;
    private final OrderStatusService orderStatusService;
    private final ProductionProcessRepository productionProcessRepository;



    public LiftMontaj findLiftMontajByProductionProcess(ProductionProcess productionProcess, LiftMontajOperationTye operationType) {
        return productionProcess.getLiftOperations()
                .stream()
                .filter(t -> t.getOperationType().equals(operationType))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.TALASLI_IMALAT_NOT_FOUND, operationType)));

    }

    public BlokLiftMontaj findLiftMontajByProductionProcess(ProductionProcess productionProcess, BlokLiftOperationType operationType) {
        return productionProcess.getBlokLiftOperations()
                .stream()
                .filter(t -> t.getOperationType().equals(operationType))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.TALASLI_IMALAT_NOT_FOUND, operationType)));

    }


    public List<LiftMontaj> liftMontajOperations (ProductionProcess productionProcess) {
        return productionProcess.getLiftOperations();
    }

    public List<BlokLiftMontaj> blokLiftMontajOperations (ProductionProcess productionProcess) {
        return productionProcess.getBlokLiftOperations();
    }

    public BlokLiftMontaj findBLByProductionProcessAndOperationType(ProductionProcess productionProcess, BlokLiftOperationType operationType) {
        return productionProcess.getBlokLiftOperations()
                .stream()
                .filter(t -> t.getOperationType().equals(operationType))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.TALASLI_IMALAT_NOT_FOUND, operationType)));
    }

    public LiftMontaj findLiftByProductionProcessAndOperationType(ProductionProcess productionProcess, LiftMontajOperationTye operationType) {
        return productionProcess.getLiftOperations()
                .stream()
                .filter(t -> t.getOperationType().equals(operationType))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.TALASLI_IMALAT_NOT_FOUND, operationType)));
    }

    public LiftMontaj findLiftMontajByOperationType(LiftMontajOperationTye operationType) {
        return liftMontajRepository.findFirstByOperationType(operationType)
                .orElseThrow(() -> new RuntimeException(String.format(ErrorMessages.TALASLI_IMALAT_OPERATION_TYPE_NOT_FOUND,operationType)));
    }

    public BlokLiftMontaj findBlokLiftMontajByOperationType(BlokLiftOperationType operationType) {
        return blokLiftMontajRepository.findFirstByOperationType(operationType)
                .orElseThrow(() -> new RuntimeException(String.format(ErrorMessages.TALASLI_IMALAT_OPERATION_TYPE_NOT_FOUND,operationType)));
    }

    public LiftMontaj findLiftOperationById(Long id) {
        return liftMontajRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.TALASLI_IMALAT_NOT_FOUND, id)));
    }

    public BlokLiftMontaj findBlokLiftOperationById(Long id) {
        return blokLiftMontajRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.TALASLI_IMALAT_NOT_FOUND, id)));
    }

    public void saveLiftMontajWithoutReturn(LiftMontaj liftMontaj) {
        liftMontajRepository.save(liftMontaj);
    }

    public LiftMontaj saveLiftMontajWithReturn(LiftMontaj liftMontaj) {
        return liftMontajRepository.save(liftMontaj);
    }

    public void saveBlokLiftMontajWithoutReturn(BlokLiftMontaj blokLiftMontaj) {
        blokLiftMontajRepository.save(blokLiftMontaj);
    }

    public BlokLiftMontaj saveBlokLiftMontajWithReturn(BlokLiftMontaj blokLiftMontaj) {
        return blokLiftMontajRepository.save(blokLiftMontaj);
    }

    public void compareMilCountAndPipeCountForBLMontaj(BlokLiftMontaj blokLiftMontaj) {
        int milCount = blokLiftMontaj.getMilCount();
        int pipeCount = blokLiftMontaj.getPipeCount();
        int afterCompare = Math.min(milCount, pipeCount);
        int completedQty = blokLiftMontaj.getCompletedQuantity();
        int updatedQuantityForRemaining = afterCompare -completedQty;
        blokLiftMontaj.setRemainingQuantity(updatedQuantityForRemaining);
        saveBlokLiftMontajWithoutReturn(blokLiftMontaj);
    }

    public void compareMilCountAndPipeCountForLiftMontaj(LiftMontaj liftMontaj) {
        int milCount = liftMontaj.getMilCount();
        int pipeCount = liftMontaj.getPipeCount();
        int afterCompare = Math.min(milCount, pipeCount);
        int completedQty = liftMontaj.getCompletedQuantity();
        int updatedQuantityForRemaining = afterCompare -completedQty;
        liftMontaj.setRemainingQuantity(updatedQuantityForRemaining);
        saveLiftMontajWithoutReturn(liftMontaj);
    }
}
