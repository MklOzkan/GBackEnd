package com.project.service.helper;


import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.BoyaPaketOperationType;
import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.domain.concretes.business.process.boyavepaket.BoyaVePaketleme;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.repository.business.process.BoyaVePaketlemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoyaPaketHelper {

    private final MethodHelper methodHelper;
    private final BoyaVePaketlemeRepository boyaVePaketlemeRepository;

    public BoyaVePaketleme findBoyaVePaketlemeByProductionProcessAndOperationType(ProductionProcess productionProcess, BoyaPaketOperationType operationType) {
        return productionProcess.getBoyaPaketOperations()
                .stream()
                .filter(t -> t.getOperationType().equals(operationType))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.TALASLI_IMALAT_NOT_FOUND, operationType)));

    }

    public BoyaVePaketleme findBoyaVePaketlemeByOperationId(Long operationId) {
        return boyaVePaketlemeRepository.findById(operationId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.BOYA_PAKET_NOT_FOUND_BY_ID, operationId)));
    }

    public void saveBoyaVePaketlemeWithoutReturn(BoyaVePaketleme boyaVePaketleme) {
        boyaVePaketlemeRepository.save(boyaVePaketleme);
    }

}
