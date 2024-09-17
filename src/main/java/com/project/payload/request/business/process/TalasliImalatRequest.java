package com.project.payload.request.business.process;


import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.payload.request.abstracts.BaseOperationRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TalasliImalatRequest extends BaseOperationRequest {
    @NotNull(message = "işlem tipi boş olamaz")
    private String operationType;//işlem tipi

}
