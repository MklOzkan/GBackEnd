package com.project.payload.mappers;

import com.project.domain.concretes.business.OrderConfirm;
import com.project.payload.request.business.OrderConfirmRequest;
import com.project.payload.response.business.OrderConfirmResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class OrderConfirmMapper {

    public OrderConfirm mapOrderConfirmRequestToOrderConfirm(OrderConfirmRequest orderConfirmRequest){
        return OrderConfirm.builder()
                .customerName(orderConfirmRequest.getCustomerName())
                .gasanNo(orderConfirmRequest.getGasanNo())
                .orderNumber(orderConfirmRequest.getOrderNumber())
                .deliveryDate(orderConfirmRequest.getDeliveryDate())
                .orderType(orderConfirmRequest.getOrderType())
                .orderQuantity(orderConfirmRequest.getOrderQuantity())
                .readyMilCount(orderConfirmRequest.getReadyMilCount())
                .build();
    }

    public OrderConfirm mapOrderConfirmRequestToOrderConfirm(OrderConfirmRequest orderConfirmRequest,OrderConfirm order){
        return order.toBuilder()
                .customerName(orderConfirmRequest.getCustomerName())
                .gasanNo(orderConfirmRequest.getGasanNo())
                .orderNumber(orderConfirmRequest.getOrderNumber())
                .deliveryDate(orderConfirmRequest.getDeliveryDate())
                .orderType(orderConfirmRequest.getOrderType())
                .orderQuantity(orderConfirmRequest.getOrderQuantity())
                .readyMilCount(orderConfirmRequest.getReadyMilCount())
                .build();
    }



    public OrderConfirmResponse mapOrderConfirmToOrderConfirmResponse(OrderConfirm orderConfirm){
        return OrderConfirmResponse.builder().
                customerName(orderConfirm.getCustomerName())
                .gasanNo(orderConfirm.getGasanNo())
                .orderNumber(orderConfirm.getOrderNumber())
                .orderDate(orderConfirm.getOrderDate())
                .deliveryDate(orderConfirm.getDeliveryDate())
                .orderType(orderConfirm.getOrderType())
                .orderTotal(orderConfirm.getOrderQuantity())
                .readyMilCount(orderConfirm.getReadyMilCount())
                .build();
    }


}
