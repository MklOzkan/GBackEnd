package com.project.payload.response.business.process;


import com.project.domain.concretes.business.process._enums.BoyaPaketOperationType;
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
public class BoyaVePaketlemeResponse extends BaseOperationResponse {
    private Long id;
    private BoyaPaketOperationType operationType;




}
