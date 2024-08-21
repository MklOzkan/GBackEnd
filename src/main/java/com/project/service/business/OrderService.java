package com.project.service.business;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.user.User;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.OrderMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.business.OrderRequest;
import com.project.payload.response.business.OrderResponse;
import com.project.repository.business.OrderRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import com.project.service.validator.TimeValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TimeValidator timeValidator;
    private final MethodHelper methodHelper;
    private final PageableHelper pageableHelper;


    public void createOrder(OrderRequest orderRequest, HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        User user = methodHelper.loadUserByUsername(username);
        checkUserName(user);//kullanıcı adı kontrolü yapıyoruz
        timeValidator.checkTimeWithException(LocalDate.now(), orderRequest.getDeliveryDate());//teslim tarihi bugünden küçük olamaz


        Order orderConfirmToSave = orderMapper.mapOrderConfirmRequestToOrderConfirm(orderRequest);
        Order savedOrder = orderRepository.save(orderConfirmToSave);
    }


    public ResponseEntity<OrderResponse> updateOrder(OrderRequest orderRequest, String orderNumber, HttpServletRequest request) {
        String userRole = request.getAttribute("userName").toString();
        methodHelper.isUserExist(userRole);

        Order order = methodHelper.findOrderByOrderNumber(orderNumber);
        timeValidator.checkTimeWithException(LocalDate.now(), orderRequest.getDeliveryDate());
        Order updatedOrder = orderMapper.mapOrderConfirmRequestToOrderConfirm(orderRequest,order);

        Order savedOrder = orderRepository.save(updatedOrder);

        return ResponseEntity.ok(orderMapper.mapOrderConfirmToOrderConfirmResponse(savedOrder));
    }

    private void checkUserName(User user) {
        if (!user.getUsername().equals("UretimPlanlama")) {
            throw new BadRequestException(ErrorMessages.UNAUTHORIZED_USER);
        }
    }


    public OrderResponse getByOrderNumber(String orderNumber) {
        Order order = methodHelper.findOrderByOrderNumber(orderNumber);
        return orderMapper.mapOrderConfirmToOrderConfirmResponse(order);
    }

    public Page<OrderResponse> getAllOrders(int page, int size, String sort, String type) {
        Page<Order> orders = orderRepository.findAll(pageableHelper.getPageableWithProperties(page, size, sort, type));
        return orders.map(orderMapper::mapOrderConfirmToOrderConfirmResponse);
    }
}
