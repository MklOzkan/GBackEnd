package com.project.controller.business;

import com.project.payload.request.business.OrderConfirmRequest;
import com.project.payload.response.business.OrderConfirmResponse;
import com.project.service.business.OrderConfirmService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/production")
@RequiredArgsConstructor
public class ProductionController {

    private final OrderConfirmService orderConfirmService;


    @PostMapping("/createOrder")
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderConfirmRequest orderConfirmRequest, HttpServletRequest request){
        return orderConfirmService.createOrder(orderConfirmRequest, request);
    }
}
