package com.project.controller.business;

import com.project.domain.enums.StatusType;
import com.project.payload.request.business.OrderRequest;
import com.project.payload.response.business.OrderResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.ExcelService;
import com.project.service.business.OrderService;
import com.project.service.helper.MethodHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.DateFormatter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final ExcelService excelService;
    private final MethodHelper methodHelper;

    @PreAuthorize("hasAnyAuthority('Employee')")
    @PostMapping("/createOrder")
    public ResponseMessage<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest, HttpServletRequest request){
        return orderService.createOrder(orderRequest, request);
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
    public Page<OrderResponse> getAllOrders(@RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size,
                                                           @RequestParam(value = "sort", defaultValue = "orderNumber") String sort,
                                                           @RequestParam(value = "type", defaultValue = "desc") String type){

        return orderService.getAllOrders(page, size, sort, type);

    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getAllOrdersForSupervisor")
    public Page<OrderResponse> getAllOrdersForSupervisor(
                                                            HttpServletRequest  request,
                                                            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
                                                            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size,
                                                            @RequestParam(value = "sort", defaultValue = "orderDate") String sort,
                                                            @RequestParam(value = "type", defaultValue = "desc") String type){
        return orderService.getAllOrdersForSupervisor(request,page, size, sort, type);

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

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @DeleteMapping("/deleteOrder/{orderNumber}")
    public ResponseMessage<String> deleteOrder(@PathVariable String orderNumber, HttpServletRequest request){
        return orderService.deleteOrder(orderNumber, request);
    }
    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/filterOrders")
    public Page<OrderResponse> filterOrders(
            @RequestParam(value = "statuses", required = false) Set<StatusType> statuses,
            @RequestParam(value = "startDate") String startDateStr,
            @RequestParam(value = "endDate") String endDateStr,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "orderDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type) {

//        // Default to filtering by all statuses if none are provided
//        if (statuses == null || statuses.isEmpty()) {
//            statuses = EnumSet.allOf(StatusType.class);
//        }
//
//        // Parse dates
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
//        LocalDate endDate = LocalDate.parse(endDateStr, formatter);
//
//        // Create pageable object
//        Pageable pageable =

        // Call service to get filtered orders
        return orderService.filterOrdersByStatusAndDate(statuses,startDateStr,endDateStr,page,size,sort,type);
    }

}
