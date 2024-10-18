package com.project.payload.mappers;

import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.payload.response.business.process.LiftResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class LiftMapper {


    public LiftResponse mapLiftToResponse(LiftMontaj lift) {
        return LiftResponse.builder()
                .id(lift.getId())
                .milCount(lift.getMilCount())
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

    public List<LiftResponse> mapLiftListToResponse(List<LiftMontaj> liftList) {
        return liftList.stream()
                .map(this::mapLiftToResponse)
                .collect(Collectors.toList());
    }
}
