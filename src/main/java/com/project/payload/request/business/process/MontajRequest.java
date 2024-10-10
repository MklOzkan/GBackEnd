package com.project.payload.request.business.process;

import com.project.domain.enums.OrderType;
import com.project.payload.request.abstracts.BaseOperationRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class MontajRequest extends BaseOperationRequest {

    private int milCount;//mil sayısı

    private int pipeCount;//boru sayısı

    private int scrapPipeCount;//hurda boru sayısı

    private int scrapMilCount;//hurda mil sayısı

    private int scarapCountAfterTest;//test sonrası hurda sayısı

    private int lastCompletedScrapCount;//son tamamlanan hurda sayısı

    @NotNull(message = "işlem tipi boş olamaz")
    private String operationType;//işlem tipi

    private OrderType orderType;//sipariş tipi

}
