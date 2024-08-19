package com.project.payload.response.business;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class OrderConfirmResponse {


    private String customerName;
    private String gasanNo;
    private String orderNumber;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String orderType;
    private Integer orderTotal;
    private Integer readyMilCount;
}
