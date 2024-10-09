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


    private int milRemainingCount =0;//mil sayısı

    private int milCompletedCount =0;//tamamlanan mil sayısı

    private int pipeRemainingCount =0;//boru sayısı

    private int pipeCompletedCount =0;//tamamlanan boru sayısı

    private int scrapPipeCount =0;//hurda boru sayısı

    private int scrapMilCount =0;//hurda mil sayısı

    private int scrapCountAfterTest =0;//test sonrası hurda sayısı

    private int lastCompletedScrapCount =0;//son tamamlanan hurda sayısı

    @NotNull(message = "Islem tipi bos olamaz")
    private String operationType;

}
