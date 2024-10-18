package com.project.payload.mappers;

import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.payload.response.business.process.BlokLiftResponse;
import com.project.payload.response.business.process.LiftResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class BlokLiftMapper {

    public BlokLiftResponse mapBlokLiftToResponse(BlokLiftMontaj lift) {
        return BlokLiftResponse.builder()
                .id(lift.getId())
                .milCount(lift.getMilCount())
                .pipeCount(lift.getPipeCount())
                .scrapCountAfterTest(lift.getScrapCountAfterTest())
                .scrapPipeCount(lift.getScrapPipeCount())
                .completedQuantity(lift.getCompletedQuantity())
                .remainingQuantity(lift.getRemainingQuantity())
                .endDate(lift.getEndDate())
                .startDate(lift.getStartDate())
                .isCompleted(lift.getIsCompleted())
                .operationType(lift.getOperationType())
                .lastCompletedQty(lift.getLastCompletedQty())
                .active(lift.isActive())
                .build();
    }

    public List<BlokLiftResponse> mapBlokLiftListToResponse(List<BlokLiftMontaj> blokLiftMontaj) {
        return blokLiftMontaj.stream()
                .map(this::mapBlokLiftToResponse)
                .collect(Collectors.toList());
    }
}
