package com.project.payload.response.business.process;

import com.project.domain.concretes.business.process._enums.BlokLiftOperationType;
import com.project.payload.request.abstracts.BaseOperationRequest;
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
@AllArgsConstructor
public class BlokLiftResponse extends BaseOperationResponse {

    private Long id;

    private Integer milCount;//mil sayısı

    private Integer scrapPipeCount;//hurda boru sayısı

    private Integer scrapMilCount;//hurda mil sayısı

    private Integer scrapCountAfterTest;//test sonrası hurda sayısı

    private BlokLiftOperationType operationType;

}
