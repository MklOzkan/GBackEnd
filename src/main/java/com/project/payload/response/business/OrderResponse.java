package com.project.payload.response.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {


    private String customerName;
    private String gasanNo;
    private String orderNumber;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String orderType;
    private Integer orderTotal;
    private String orderStatus;
    private Integer readyMilCount;
}
