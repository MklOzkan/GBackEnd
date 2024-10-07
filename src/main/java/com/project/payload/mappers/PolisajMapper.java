package com.project.payload.mappers;

import com.project.domain.concretes.business.process.polisajamiri.PolisajImalat;
import com.project.payload.response.business.process.PolisajResponse;
import com.project.service.business.process.PolisajService;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class PolisajMapper {

    private final PolisajService polisajService;

    public PolisajResponse mapToResponse(PolisajImalat polisaj){
        return PolisajResponse.builder()
                .id(polisaj.getId())
                .completedQuantity(polisaj.getCompletedQuantity())
                .active(polisaj.isActive())
                .isCompleted(polisaj.getIsCompleted())
                .remainingQuantity(polisaj.getRemainingQuantity())
                .startDate(polisaj.getStartDate())
                .endDate(polisaj.getEndDate())
                .lastCompletedQty(polisaj.getLastCompletedQty())
                .operationType(polisaj.getOperationType())
                .build();
    }
}
