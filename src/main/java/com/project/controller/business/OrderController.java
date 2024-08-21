package com.project.controller.business;

import com.project.payload.request.business.OrderRequest;
import com.project.payload.response.business.OrderResponse;
import com.project.service.business.ExcelService;
import com.project.service.business.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final ExcelService excelService;

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
    @GetMapping("/download/excel")
    public ResponseEntity<byte[]> downLoadOrdersExcel(){

        List<OrderResponse> orders = orderService.getOrders();

        LOGGER.info("Request received to download Excel for orders.");

        try (ByteArrayInputStream in = excelService.generateExcel(orders)) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=orders.xlsx");

            LOGGER.info("Excel file generated successfully, returning the file to the client.");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(in.readAllBytes());
        } catch (IOException e) {
            LOGGER.error("Error occurred while sending Excel file: {}", e.getMessage());
            // Handle the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }






}
