package com.project.payload.mappers;


import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.payload.response.business.OrderResponse;
import com.project.payload.response.business.process.ProductionProcessResponse;
import com.project.payload.response.business.process.TalasliImalatResponse;
import com.project.service.business.process.TalasliService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

//@Data
@Getter
@Setter
@Component
public class TalasliMapper {

    private TalasliService talasliService;


    public TalasliImalatResponse mapTalasliToResponse(TalasliImalat talasliImalat) {
        return TalasliImalatResponse.builder()
                .id(talasliImalat.getId())
                .completedQuantity(talasliImalat.getCompletedQuantity())
                .remainingQuantity(talasliImalat.getRemainingQuantity())
                .endDate(talasliImalat.getEndDate())
                .startDate(talasliImalat.getStartDate())
                .isCompleted(talasliImalat.getIsCompleted())
                .operationType(String.valueOf(talasliImalat.getOperationType()))
                .productionProcessId(talasliImalat.getProductionProcess().getId())
                .build();
    }


    public List<TalasliImalatResponse> mapTalasliListToResponse(List<TalasliImalat> talasliImalatList) {
        return talasliImalatList.stream()
                .map(this::mapTalasliToResponse)
                .collect(Collectors.toList());
    }


    public ProductionProcessResponse mapProductionProcessToResponse(ProductionProcess productionProcess) {
        return ProductionProcessResponse.builder()
                .id(productionProcess.getId())
                .startDate(productionProcess.getStartDate())
                .endDate(productionProcess.getEndDate())
                .completedQuantity(productionProcess.getCompletedQuantity())
                .remainingQuantity(productionProcess.getRemainingQuantity())
                .isCompleted(productionProcess.getIsCompleted())
                .build();
    }
}
