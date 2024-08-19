package com.project.service.business;

import com.project.domain.concretes.business.OrderConfirm;
import com.project.domain.concretes.user.UserRole;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.OrderConfirmMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.business.OrderConfirmRequest;
import com.project.payload.response.business.OrderConfirmResponse;
import com.project.repository.repository.OrderConfirmRepository;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.validator.TimeValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrderConfirmService {

    private OrderConfirmRepository orderConfirmRepository;
    private OrderConfirmMapper orderConfirmMapper;
    private TimeValidator timeValidator;
    private MethodHelper methodHelper;


    public ResponseEntity<String> createOrder(OrderConfirmRequest orderConfirmRequest, HttpServletRequest request) {
        String userRole = request.getAttribute("userName").toString();

        timeValidator.checkTimeWithException(LocalDate.now(), orderConfirmRequest.getDeliveryDate());//teslim tarihi bugünden küçük olamaz
        methodHelper.isUserExist(userRole);//kullanıcı var mı kontrol ediyoruz

        OrderConfirm orderConfirmToSave = orderConfirmMapper.mapOrderConfirmRequestToOrderConfirm(orderConfirmRequest);
        OrderConfirm savedOrder = orderConfirmRepository.save(orderConfirmToSave);

        return ResponseEntity.ok("Order created successfully");
    }


    public ResponseEntity<OrderConfirmResponse> updateOrder(OrderConfirmRequest orderConfirmRequest, Long orderId, HttpServletRequest request) {
        String userRole = request.getAttribute("userName").toString();
        methodHelper.isUserExist(userRole);

        OrderConfirm order = orderConfirmRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException(ErrorMessages.NOT_FOUND_ORDER));
        timeValidator.checkTimeWithException(LocalDate.now(), orderConfirmRequest.getDeliveryDate());
        OrderConfirm updatedOrder = orderConfirmMapper.mapOrderConfirmRequestToOrderConfirm(orderConfirmRequest,order);

        OrderConfirm savedOrder = orderConfirmRepository.save(updatedOrder);

        return ResponseEntity.ok(orderConfirmMapper.mapOrderConfirmToOrderConfirmResponse(savedOrder));
    }



}
