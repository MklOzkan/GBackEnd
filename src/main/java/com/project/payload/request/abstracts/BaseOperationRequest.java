package com.project.payload.request.abstracts;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public abstract class BaseOperationRequest {

    private Integer remainingQuantity =0;  // Kalan miktar

    private Integer completedQuantity =0;  // Tamamlanan miktar

    private LocalDateTime startDate;  // Operasyon başlama tarihi

    private LocalDateTime endDate;  // Operasyon bitiş tarihi

    private Boolean isCompleted = false;  // Operasyonun tamamlanma durumu

    private boolean active = false;  // Operasyonun aktif olup olmadığı

}
