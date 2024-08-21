package com.project.controller.business;

import com.project.payload.request.business.OrderRequest;
import com.project.payload.response.business.OrderResponse;
import com.project.service.business.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/production")
@RequiredArgsConstructor
public class ProductionController {

    private final OrderService orderService;

    @PreAuthorize("hasAnyAuthority('Employee')")
    @PostMapping("/createOrder")
    public ResponseEntity<String> createOrder(@RequestBody @Valid OrderRequest orderRequest, HttpServletRequest request){
        orderService.createOrder(orderRequest, request);
        return ResponseEntity.ok("Order created successfully");
    }

    @PutMapping("/updateOrder{orderNumber}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable String orderNumber, @RequestBody @Valid OrderRequest orderRequest, HttpServletRequest request){
       return orderService.updateOrder(orderRequest,orderNumber,request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getByOrderNumber/{orderNumber}")
    public ResponseEntity<OrderResponse> getByOrderNumber(@PathVariable String orderNumber){
        return ResponseEntity.ok(orderService.getByOrderNumber(orderNumber));
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getAllOrders")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                                           @RequestParam(value = "sort", defaultValue = "orderNumber") String sort,
                                                           @RequestParam(value = "type", defaultValue = "desc") String type){
        Page<OrderResponse> orderResponses = orderService.getAllOrders(page, size, sort, type);
        return ResponseEntity.ok(orderResponses);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @DeleteMapping("/deleteOrderByOrderNumber/{orderNumber}")
    ResponseEntity<OrderResponse> delete(@PathVariable String orderNumber){
        return ResponseEntity.ok(orderService.deleteOrderByOrderNumber(orderNumber));
    }








}
