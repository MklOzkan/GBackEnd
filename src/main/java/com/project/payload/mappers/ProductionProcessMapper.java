package com.project.payload.mappers;

import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.payload.response.business.process.ProductionProcessResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ProductionProcessMapper {

    public ProductionProcessResponse mapToResponse(ProductionProcess productionProcess){
        return ProductionProcessResponse.builder()
                .id(productionProcess.getId())
                .startDate(productionProcess.getStartDate())
                .endDate(productionProcess.getEndDate())
                .isCompleted(productionProcess.getIsCompleted())
                .active(productionProcess.isActive())
                .completedQuantity(productionProcess.getCompletedQuantity())
                .remainingQuantity(productionProcess.getRemainingQuantity())
                .build();
    }

}
