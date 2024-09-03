package com.project.payload.mappers;

import com.project.domain.concretes.business.Order;
import com.project.domain.enums.StatusType;
import com.project.payload.request.business.OrderRequest;
import com.project.payload.request.business.UpdateOrderRequest;
import com.project.payload.response.business.OrderResponse;
import com.project.service.business.OrderStatusService;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class OrderMapper {

    private final OrderStatusService orderStatusService;

    public Order mapOrderConfirmRequestToOrderConfirm(OrderRequest orderRequest){
        Order order = Order.builder()
                .customerName(orderRequest.getCustomerName())
                .gasanNo(orderRequest.getGasanNo())
                .orderNumber(orderRequest.getOrderNumber())
                .deliveryDate(orderRequest.getDeliveryDate())
                .orderType(orderRequest.getOrderType())
                .orderQuantity(orderRequest.getOrderQuantity())
                .readyMilCount(orderRequest.getReadyMilCount())
                .build();

        if (orderRequest.getOrderStatus().equalsIgnoreCase("İşlenmeyi Bekliyor")){
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.ISLENMEYI_BEKLIYOR));
        }else if (orderRequest.getOrderStatus().equalsIgnoreCase("İşlenmekte")){
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.ISLENMEKTE));
        } else if (orderRequest.getOrderStatus().equalsIgnoreCase("Tamamlandı")){
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.TAMAMLANDI));
        }else{
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.IPTAL_EDILDI));
        }
        return order;
    }

    public OrderResponse mapOrderToOrderResponse(Order order){
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .gasanNo(order.getGasanNo())
                .orderNumber(order.getOrderNumber())
                .orderDate(order.getOrderDate())
                .deliveryDate(order.getDeliveryDate())
                .orderType(order.getOrderType())
                .orderQuantity(order.getOrderQuantity())
                .readyMilCount(order.getReadyMilCount())
                .orderStatus(order.getOrderStatus().getStatusType().getName())
                .build();
    }

    public Order updateOrderFromRequest(UpdateOrderRequest orderRequest, Order order) {
        order.setId(order.getId());
        order.setCustomerName(orderRequest.getCustomerName());
        order.setGasanNo(orderRequest.getGasanNo());
        order.setOrderNumber(orderRequest.getOrderNumber());
        order.setOrderDate(order.getOrderDate());
        order.setDeliveryDate(orderRequest.getDeliveryDate());
        order.setOrderType(orderRequest.getOrderType());
        order.setOrderQuantity(orderRequest.getOrderQuantity());
        order.setReadyMilCount(orderRequest.getReadyMilCount());

        if (orderRequest.getOrderStatus().equalsIgnoreCase("İşlenmeyi Bekliyor")) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.ISLENMEYI_BEKLIYOR));
        } else if (orderRequest.getOrderStatus().equalsIgnoreCase("İşlenmekte")) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.ISLENMEKTE));
        } else if (orderRequest.getOrderStatus().equalsIgnoreCase("Tamamlandı")) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.TAMAMLANDI));
        } else if (orderRequest.getOrderStatus().equalsIgnoreCase("İptal Edildi")) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.IPTAL_EDILDI));
        }

        return order;
    }



}
