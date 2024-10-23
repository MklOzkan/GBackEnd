package com.project.payload.mappers;

import com.project.domain.concretes.business.process.boyavepaket.BoyaVePaketleme;
import com.project.payload.response.business.process.BoyaVePaketlemeResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class BoyaVePaketMapper {

    public BoyaVePaketlemeResponse mapToBoyaVePaketlemeResponse(BoyaVePaketleme boyaVePaket){
        return BoyaVePaketlemeResponse.builder()
                .id(boyaVePaket.getId())
                .completedQuantity(boyaVePaket.getCompletedQuantity())
                .remainingQuantity(boyaVePaket.getRemainingQuantity())
                .lastCompletedQty(boyaVePaket.getLastCompletedQty())
                .operationType(boyaVePaket.getOperationType())
                .isCompleted(boyaVePaket.getIsCompleted())
                .startDate(boyaVePaket.getStartDate())
                .endDate(boyaVePaket.getEndDate())
                .active(boyaVePaket.isActive())
                .build();
    }

    public List<BoyaVePaketlemeResponse> mapToBoyaVePaketlemeResponseList(List<BoyaVePaketleme> boyaVePaketlemeList){
        return boyaVePaketlemeList.stream()
                .map(this::mapToBoyaVePaketlemeResponse)
                .collect(Collectors.toList());
    }
}
