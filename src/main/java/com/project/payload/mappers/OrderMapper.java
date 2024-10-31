package com.project.payload.mappers;

import com.project.domain.concretes.business.Order;
import com.project.domain.enums.OrderType;
import com.project.domain.enums.StatusType;
import com.project.payload.request.business.OrderRequest;
import com.project.payload.request.business.UpdateOrderRequest;
import com.project.payload.response.business.OrderResponse;
import com.project.service.business.OrderStatusService;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

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
                .orderQuantity(orderRequest.getOrderQuantity())
                .readyMilCount(orderRequest.getReadyMilCount())
                .build();

        if (orderRequest.getOrderType().equalsIgnoreCase("Blok Lift")){
            order.setOrderType(OrderType.BLOKLIFT);
        }else if (orderRequest.getOrderType().equalsIgnoreCase("Damper")){
            order.setOrderType(OrderType.DAMPER);
        }else if (orderRequest.getOrderType().equalsIgnoreCase("Lift")){
            order.setOrderType(OrderType.LIFT);
        }else if (orderRequest.getOrderType().equalsIgnoreCase("Paslanmaz")){
            order.setOrderType(OrderType.PASLANMAZ);
        }

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
                .finalProductQuantity(order.getFinalProductQuantity())
                .readyMilCount(order.getReadyMilCount())
                .orderStatus(order.getOrderStatus().getStatusType().getName())// Bitiş tarihi
                .productionProcessId(order.getProductionProcess().getId())
                .build();
    }

    public Order updateOrderFromRequest(UpdateOrderRequest orderRequest, Order order) {
        order.setId(order.getId());
        order.setCustomerName(orderRequest.getCustomerName());
        order.setGasanNo(orderRequest.getGasanNo());
        order.setOrderNumber(orderRequest.getOrderNumber());
        order.setOrderDate(order.getOrderDate());
        order.setDeliveryDate(orderRequest.getDeliveryDate());
        order.setOrderQuantity(orderRequest.getOrderQuantity());
        order.setReadyMilCount(orderRequest.getReadyMilCount());

        if (orderRequest.getOrderType().equalsIgnoreCase("Bloklift")) {
            order.setOrderType(OrderType.BLOKLIFT);
        } else if (orderRequest.getOrderType().equalsIgnoreCase("Damper")) {
            order.setOrderType(OrderType.DAMPER);
        } else if (orderRequest.getOrderType().equalsIgnoreCase("Lift")) {
            order.setOrderType(OrderType.LIFT);
        } else if (orderRequest.getOrderType().equalsIgnoreCase("Paslanmaz")) {
            order.setOrderType(OrderType.PASLANMAZ);
        }

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


    public List<OrderResponse> mapOrderListToOrderResponseList(List<Order> orderList) {
        return orderList.stream().map(this::mapOrderToOrderResponse).toList();
    }
}
