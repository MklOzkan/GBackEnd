package com.project.payload.response.abstracts;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public abstract class BaseOperationResponse {

    private Integer remainingQuantity;  // Kalan miktar

    private Integer completedQuantity;  // Tamamlanan miktar

    private LocalDateTime startDate;  // Operasyon başlama tarihi

    private LocalDateTime endDate;  // Operasyon bitiş tarihi

    private Boolean isCompleted;  // Operasyonun tamamlanma durumu

    private int lastCompletedQty;  // Son tamamlanan miktar

}
