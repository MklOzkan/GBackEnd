package com.project.payload.response.business.process;


import com.project.payload.request.abstracts.BaseOperationRequest;
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
public class ProductionProcessResponse extends BaseOperationRequest {
    private Long id;
}
