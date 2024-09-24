package com.project.controller.business;


import com.project.payload.request.business.process.KaliteKontrolRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.process.KaliteKontrolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kalitekontrol")
@RequiredArgsConstructor
public class KaliteKontrolController {

    private final KaliteKontrolService kaliteKontrolService;

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/aftermiltaslama/{stageId}")
    public ResponseMessage<String> afterMilTaslamaKaliteKontrol(@RequestBody @Valid KaliteKontrolRequest request, @PathVariable Long stageId) {
        return kaliteKontrolService.afterMilTaslamaKaliteKontrol(request, stageId);
    }
}
