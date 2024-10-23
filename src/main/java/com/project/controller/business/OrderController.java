package com.project.controller.business;

import com.project.payload.request.business.OrderRequest;
import com.project.payload.request.business.UpdateOrderRequest;
import com.project.payload.response.business.MultipleResponses;
import com.project.payload.response.business.OrderResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.process.*;
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
    private final MethodHelper methodHelper;

    @PreAuthorize("hasAnyAuthority('Employee')")
    @PostMapping("/createOrder")
    public ResponseMessage<OrderResponse> createOrder(@RequestBody @Valid OrderRequest orderRequest, HttpServletRequest request){
        return orderService.createOrder(orderRequest, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/startStop/{id}")
    public ResponseMessage<String> startStopTalasli(@PathVariable Long id) {
        return orderService.startStop(id);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getOrderById/{id}")
    public ResponseMessage<OrderResponse> getOrder(@PathVariable Long id, HttpServletRequest request){
        return orderService.getOrder(id, request);
    }


    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/updateOrder/{id}")
    public ResponseMessage<OrderResponse> updateOrder(@PathVariable Long id, @RequestBody @Valid UpdateOrderRequest orderRequest, HttpServletRequest request){
       return orderService.updateOrder(orderRequest,id,request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getByOrderNumber/{orderNumber}")
    public ResponseEntity<OrderResponse> getByOrderNumber(@PathVariable String orderNumber){
        return ResponseEntity.ok(orderService.getByOrderNumber(orderNumber));
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getMultipleResponseById/{id}")
    public MultipleResponses<OrderResponse, List<TalasliImalatResponse>, ProductionProcessResponse> getOrderById(@PathVariable Long id, HttpServletRequest request){
        return orderService.getOrderById(id, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getMultipleResponseByIdForPolisaj/{id}")
    public MultipleResponses<OrderResponse, PolisajResponse, ProductionProcessResponse> getMultipleResponseByIdForPolisaj(@PathVariable Long id, HttpServletRequest request){
        return orderService.getMultipleResponseByIdForPolisaj(id, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getResponsesForLift/{id}")
    public MultipleResponses<OrderResponse, List<LiftResponse>, ProductionProcessResponse> getOrderByIdForLiftMontaj(@PathVariable Long id, HttpServletRequest request){
        return orderService.getOrderByIdForLiftMontaj(id, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getResponsesForBlokLift/{id}")
    public MultipleResponses<OrderResponse, List<BlokLiftResponse>, ProductionProcessResponse> getOrderByIdForBlokLiftMontaj(@PathVariable Long id, HttpServletRequest request){
        return orderService.getOrderByIdForBlokLiftMontaj(id, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getResponsesForBoyaPaket/{id}")
    public MultipleResponses<OrderResponse, List<BoyaVePaketlemeResponse>, ProductionProcessResponse> getOrderByIdForBoyaPaket(@PathVariable Long id, HttpServletRequest request){
        return orderService.getOrderByIdForBoyaPaket(id, request);
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
    public Page<OrderResponse> getAllOrdersForTalasliAmir(
                                                            HttpServletRequest  request,
                                                            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
                                                            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size,
                                                            @RequestParam(value = "sort", defaultValue = "orderDate") String sort,
                                                            @RequestParam(value = "type", defaultValue = "desc") String type){
        return orderService.getAllOrdersForSupervisor(request,page, size, sort, type);

    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getOrdersForBLMontajAmiri")
    public Page<OrderResponse> getOrdersForBLMontajAmiri(
            HttpServletRequest  request,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size,
            @RequestParam(value = "sort", defaultValue = "orderDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type){
        return orderService.getOrdersForBLMontajAmiri(request,page, size, sort, type);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getOrdersForLiftMontajAmiri")
    public Page<OrderResponse> getOrdersForLiftMontajAmiri(
            HttpServletRequest  request,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size,
            @RequestParam(value = "sort", defaultValue = "orderDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type){
        return orderService.getOrdersForLiftMontajAmiri(request,page, size, sort, type);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getOrdersForPolisajAmir")
    public Page<OrderResponse> getOrdersWhichStatusIslenmekteAndBeklemedeForPolisaj(
            HttpServletRequest  request,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size,
            @RequestParam(value = "sort", defaultValue = "orderDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type){
        return orderService.getOrdersForPolisajAmir(request,page, size, sort, type);

    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getOrdersIslenmekteAndBeklemede")
    public Page<OrderResponse> getOrdersWhichStatusIslenmekteAndBeklemede(
            HttpServletRequest  request,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size,
            @RequestParam(value = "sort", defaultValue = "orderDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type){
        return orderService.getOrdersWhichStatusIslenmekteAndBeklemede(request,page, size, sort, type);

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
            @RequestParam(value = "statuses", required = false) List<String> statuses,
            @RequestParam(value = "startDate") String startDateStr,
            @RequestParam(value = "endDate") String endDateStr,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "orderQuantity") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type) {

        return orderService.filterOrdersByStatusAndDate(statuses,startDateStr,endDateStr,page,size,sort,type);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getAllOrdersForOtherSupervisor")
    public Page<OrderResponse> getAllOrdersForOtherSupervisor(
            HttpServletRequest request,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
            @RequestParam(value = "size", defaultValue = "10") @Min(1) int size,
            @RequestParam(value = "sort", defaultValue = "orderDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type){
        return orderService.getAllOrdersForOtherSuperVisor(request,page, size, sort, type);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/finishOrder/{id}")
    public ResponseMessage<String> finishOrder(@PathVariable Long id, HttpServletRequest request){
        return orderService.finishOrder(id, request);
    }



}
