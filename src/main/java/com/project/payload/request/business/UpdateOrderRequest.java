package com.project.payload.request.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateOrderRequest {

    @NotNull(message = "Müşteri adı boş olamaz")
    private String customerName;
    @NotNull(message = "Gasan numarası boş olamaz")
    @Pattern(regexp = "\\d{4} [A-Z]{1,3} \\d{6}", message = "Invalid Gasan No format")
    private String gasanNo;
    @NotNull(message = "Sipariş numarası boş olamaz")
    @Pattern(regexp = "\\d{6}", message = "Invalid Order No format")
    private String orderNumber;
    @NotNull(message = "Teslimat tarihi boş olamaz")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    @Future(message = "Teslimat tarihi bugünden önce olamaz")
    private LocalDate deliveryDate;
    @NotNull(message = "Sipariş tipi boş olamaz")
    private String orderType;
    @NotNull(message = "Sipariş adedi boş olamaz")
    private Integer orderQuantity;
    @NotNull(message = "Sipariş durumu boş olamaz")
    private String orderStatus;
    private int readyMilCount=0;
}
