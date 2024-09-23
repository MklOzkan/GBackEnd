package com.project.payload.request.business.process;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.concretes.business.abstracts.BaseOperation;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.BlokLiftOperationType;
import com.project.payload.request.abstracts.BaseOperationRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class BlokLiftRequest extends BaseOperationRequest {


    private Integer milRemainingCount =0;//mil sayısı

    private Integer milCompletedCount =0;//tamamlanan mil sayısı

    private Integer pipeRemainingCount =0;//boru sayısı

    private Integer pipeCompletedCount =0;//tamamlanan boru sayısı

    private Integer scrapPipeCount =0;//hurda boru sayısı

    private Integer scrapMilCount =0;//hurda mil sayısı

    private Integer scrapCountAfterTest =0;//test sonrası hurda sayısı

    @NotNull(message = "Islem tipi bos olamaz")
    private String operationType;

}
