package com.project.payload.response.business.process;


import com.project.payload.request.abstracts.BaseOperationRequest;
import com.project.payload.response.abstracts.BaseOperationResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@SuperBuilder
public class ProductionProcessResponse extends BaseOperationResponse {
    private Long id;
}
