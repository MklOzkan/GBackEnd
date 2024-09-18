package com.project.payload.response.business.process;

import com.project.domain.concretes.business.abstracts.BaseOperation;
import com.project.domain.concretes.business.process._enums.LiftMontajOperationTye;
import com.project.payload.response.abstracts.BaseOperationResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LiftResponse extends BaseOperationResponse {
    private Long id;
    private Integer milCount;//mil sayısı

    private Integer scrapPipeCount;//hurda boru sayısı

    private Integer scrapMilCount;//hurda mil sayısı

    private Integer scarapCountAfterTest;//test sonrası hurda sayısı

    private LiftMontajOperationTye operationType;//işlem tipi

}
