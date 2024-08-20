package com.project.controller.business;

import com.project.domain.concretes.business.OrderConfirm;
import com.project.payload.request.business.OrderConfirmRequest;
import com.project.payload.response.business.OrderConfirmResponse;
import com.project.service.business.OrderConfirmService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/production")
@RequiredArgsConstructor
public class ProductionController {

    private final OrderConfirmService orderConfirmService;

    @PreAuthorize("hasAnyAuthority('Employee')")
    @PostMapping("/createOrder")
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderConfirmRequest orderConfirmRequest, HttpServletRequest request){
        orderConfirmService.createOrder(orderConfirmRequest, request);
        return ResponseEntity.ok("Order created successfully");
    }



    @PutMapping("/updateOrder")
    public ResponseEntity<OrderConfirmResponse> updateOrder(@RequestBody @Valid OrderConfirmRequest orderConfirmRequest , Long orderId, HttpServletRequest request){
       return orderConfirmService.updateOrder(orderConfirmRequest,orderId,request);
    }





}
