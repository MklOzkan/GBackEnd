package com.project.controller.business;


import com.project.payload.request.business.process.KaliteKontrolRequest;
import com.project.payload.response.business.MultipleResponses;
import com.project.payload.response.business.OrderResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.process.KaliteKontrolResponse;
import com.project.payload.response.business.process.ProductionProcessResponse;
import com.project.service.business.process.KaliteKontrolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kalitekontrol")
@RequiredArgsConstructor
public class KaliteKontrolController {

    private final KaliteKontrolService kaliteKontrolService;

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getAllForOrder/{orderId}")
    public MultipleResponses<OrderResponse, ProductionProcessResponse, List<KaliteKontrolResponse>> getAllForOrder(@PathVariable Long orderId) {
        return kaliteKontrolService.getAllForOrder(orderId);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getOrderAndStage/{stageId}")
    public MultipleResponses<OrderResponse, ProductionProcessResponse, KaliteKontrolResponse> getOrderAndStahe(@PathVariable Long stageId) {
        return kaliteKontrolService.getOrderAndStage(stageId);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/aftermiltaslama/{stageId}")
    public ResponseMessage<String> afterMilTaslamaKaliteKontrol(@RequestBody @Valid KaliteKontrolRequest request, @PathVariable Long stageId) {
        return kaliteKontrolService.afterMilTaslamaKaliteKontrol(request, stageId);
    }
    //TODO: Adem
    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/afterezme/{stageId}")
    public ResponseMessage<String> afterEzmeKaliteKontrol(@RequestBody @Valid KaliteKontrolRequest request, @PathVariable Long stageId) {
        return kaliteKontrolService.afterEzmeKaliteKontrol(request, stageId);
    }
    //TODO: Cahit
    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/afterpolisaj/{stageId}")
    public ResponseMessage<String> afterPolisajKaliteKontrol(@RequestBody @Valid KaliteKontrolRequest request, @PathVariable Long stageId) {
        return kaliteKontrolService.afterPolisajKaliteKontrol(request, stageId);
    }
    //TODO: Adem
    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/aftermontaj/{stageId}")
    public ResponseMessage<String> afterMontajKaliteKontrol(@RequestBody @Valid KaliteKontrolRequest request, @PathVariable Long stageId) {
        return kaliteKontrolService.afterMontajKaliteKontrol(request, stageId);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/getAllByProductionProcessId/{id}")
    public MultipleResponses<OrderResponse, ProductionProcessResponse, List<KaliteKontrolResponse> > getKaliteKontrolStages(@PathVariable Long id) {
        return kaliteKontrolService.getKaliteKontrolStages(id);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/rollbackAfterPolisaj/{stageId}")
    public ResponseMessage<String> rollbackAfterPolisajKaliteKontrol(@PathVariable Long stageId,@RequestBody @Valid KaliteKontrolRequest request) {
        return kaliteKontrolService.rollbackAfterPolisajKaliteKontrol(stageId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/rollbackAfterMontaj/{stageId}")
    public ResponseMessage<String> rollbackAfterMontajKaliteKontrol(@PathVariable Long stageId,@RequestBody @Valid KaliteKontrolRequest request) {
        return kaliteKontrolService.rollbackAfterMontajKaliteKontrol(stageId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/rollbackAfterEzme/{stageId}")
    public ResponseMessage<String> rollbackAfterEzmeKaliteKontrol(@PathVariable Long stageId,@RequestBody @Valid KaliteKontrolRequest request) {
        return kaliteKontrolService.rollbackAfterEzmeKaliteKontrol(stageId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/rollbackAfterMilTaslama/{stageId}")
    public ResponseMessage<String> rollbackAfterMilTaslamaKaliteKontrol(@PathVariable Long stageId,@RequestBody @Valid KaliteKontrolRequest request) {
        return kaliteKontrolService.rollbackAfterMilTaslamaKaliteKontrol(stageId, request);
    }



}
