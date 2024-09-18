package com.project.payload.request.business.process;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.concretes.business.abstracts.BaseOperation;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.LiftMontajOperationTye;
import com.project.payload.request.abstracts.BaseOperationRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class LiftRequest extends BaseOperationRequest {

    private Integer milCount;//mil sayısı

    private Integer scrapPipeCount;//hurda boru sayısı

    private Integer scrapMilCount;//hurda mil sayısı

    private Integer scarapCountAfterTest;//test sonrası hurda sayısı

    @NotNull(message = "işlem tipi boş olamaz")
    private String operationType;//işlem tipi

}
