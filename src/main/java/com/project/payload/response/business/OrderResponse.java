package com.project.payload.response.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private String customerName;
    private String gasanNo;
    private String orderNumber;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String orderType;
    private Integer orderQuantity;
    private String orderStatus;
    private Integer readyMilCount;
    // Başlama ve Bitiş tarihleri eklendi
    private LocalDateTime productionStartDate;
    private LocalDateTime productionEndDate;
}
