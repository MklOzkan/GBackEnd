package com.project.payload.response.business.process;


import com.project.domain.concretes.business.process.ProductionProcess;
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
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TalasliImalatResponse extends BaseOperationResponse {
    private Long id;
    private String operationType;//işlem tipi
    private Long productionProcessId;

}