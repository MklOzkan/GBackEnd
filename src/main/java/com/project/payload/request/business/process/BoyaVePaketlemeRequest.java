package com.project.payload.request.business.process;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.concretes.business.abstracts.BaseOperation;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.BoyaPaketOperationType;
import com.project.payload.request.abstracts.BaseOperationRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BoyaVePaketlemeRequest extends BaseOperationRequest {
    @NotNull(message = "işlem tipi boş olamaz")
    private String operationType;




}
