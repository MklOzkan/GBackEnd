package com.project.payload.response.business;

import com.project.domain.enums.OrderType;
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
    private OrderType orderType;
    private Integer orderQuantity;
    private int finalProductQuantity;
    private String orderStatus;
    private Integer readyMilCount;
    private Long productionProcessId;
}
