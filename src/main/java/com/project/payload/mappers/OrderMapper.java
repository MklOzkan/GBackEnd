package com.project.payload.mappers;

import com.project.domain.concretes.business.Order;
import com.project.payload.request.business.OrderRequest;
import com.project.payload.response.business.OrderResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class OrderMapper {

    public Order mapOrderConfirmRequestToOrderConfirm(OrderRequest orderRequest){
        return Order.builder()
                .customerName(orderRequest.getCustomerName())
                .gasanNo(orderRequest.getGasanNo())
                .orderNumber(orderRequest.getOrderNumber())
                .deliveryDate(orderRequest.getDeliveryDate())
                .orderType(orderRequest.getOrderType())
                .orderQuantity(orderRequest.getOrderQuantity())
                .readyMilCount(orderRequest.getReadyMilCount())
                .build();
    }

    public Order mapOrderConfirmRequestToOrderConfirm(OrderRequest orderRequest, Order order){
        return order.toBuilder()
                .customerName(orderRequest.getCustomerName())
                .gasanNo(orderRequest.getGasanNo())
                .orderNumber(orderRequest.getOrderNumber())
                .deliveryDate(orderRequest.getDeliveryDate())
                .orderType(orderRequest.getOrderType())
                .orderQuantity(orderRequest.getOrderQuantity())
                .readyMilCount(orderRequest.getReadyMilCount())
                .build();
    }



    public OrderResponse mapOrderConfirmToOrderConfirmResponse(Order orderConfirm){
        return OrderResponse.builder().
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
