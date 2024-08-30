package com.project.service.business;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.OrderStatus;
import com.project.domain.concretes.user.User;
import com.project.domain.enums.StatusType;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.OrderMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.OrderRequest;
import com.project.payload.response.business.OrderResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.OrderRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import com.project.service.validator.TimeValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TimeValidator timeValidator;
    private final MethodHelper methodHelper;
    private final PageableHelper pageableHelper;


    public ResponseMessage<OrderResponse> createOrder(OrderRequest orderRequest, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = methodHelper.loadUserByUsername(username);
        checkUserName(user);//kullanıcı adı kontrolü yapıyoruz
        timeValidator.checkTimeWithException(LocalDate.now(), orderRequest.getDeliveryDate());//teslim tarihi bugünden küçük olamaz


        Order orderConfirmToSave = orderMapper.mapOrderConfirmRequestToOrderConfirm(orderRequest);
        Order savedOrder = orderRepository.save(orderConfirmToSave);

        return ResponseMessage.<OrderResponse>builder()
                .returnBody(orderMapper.mapOrderToOrderResponse(savedOrder))
                .message(SuccessMessages.ORDER_CREATED)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }


    public ResponseEntity<OrderResponse> updateOrder(OrderRequest orderRequest, String orderNumber, HttpServletRequest request) {
        String userRole = request.getAttribute("userName").toString();
        methodHelper.isUserExist(userRole);

        Order order = methodHelper.findOrderByOrderNumber(orderNumber);
        timeValidator.checkTimeWithException(LocalDate.now(), orderRequest.getDeliveryDate());
        Order updatedOrder = orderMapper.mapOrderConfirmRequestToOrderConfirm(orderRequest);

        Order savedOrder = orderRepository.save(updatedOrder);

        return ResponseEntity.ok(orderMapper.mapOrderToOrderResponse(savedOrder));
    }

    private void checkUserName(User user) {
        if (!user.getUsername().equals("UretimPlanlama")) {
            throw new BadRequestException(ErrorMessages.UNAUTHORIZED_USER);
        }
    }


    public OrderResponse getByOrderNumber(String orderNumber) {
        Order order = methodHelper.findOrderByOrderNumber(orderNumber);
        return orderMapper.mapOrderToOrderResponse(order);
    }

    public Page<OrderResponse> getAllOrders(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(orderMapper::mapOrderToOrderResponse);
    }

    public List<OrderResponse> getOrders() {
        return orderRepository
                .findAll()
                .stream()
                .map(orderMapper::mapOrderToOrderResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<String> deleteOrder(String orderNumber, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        methodHelper.isUserExist(username);

        Order order = methodHelper.findOrderByOrderNumber(orderNumber);
        orderRepository.delete(order);
        return ResponseMessage.<String>builder()
                .message(SuccessMessages.ORDER_DELETED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public Page<OrderResponse> getAllOrdersForSupervisor(
            HttpServletRequest request, int page, int size, String sort, String type) {
        String username = (String) request.getAttribute("username");
        methodHelper.isUserExist(username);
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        List<String> statuses = List.of(StatusType.ISLENMEYI_BEKLIYOR.getName(), StatusType.ISLENMEKTE.getName());
        Page<Order> ordersPage = orderRepository.findByOrderStatus_StatusNameIn(statuses, pageable);


        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(orderMapper::mapOrderToOrderResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());

    }

    public Page<OrderResponse> filterOrdersByStatusAndDate(
            List<String> statuses, String startDateStr, String endDateStr, int page, int size, String sort, String type) {

//         //Default to filtering by all statuses if none are provided
//        if (statuses == null || statuses.isEmpty()) {
//            statuses = EnumSet.allOf(OrderStatus.class);
//        }

        // Parse dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        Page<Order> ordersPage = orderRepository.findByStatusTypeAndOrderDateBetween(statuses, startDate, endDate, pageable);

        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(orderMapper::mapOrderToOrderResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());
    }
}
