package com.project.controller.business;


import com.project.payload.request.business.process.BoyaVePaketlemeRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.process.BoyaVePaketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boyavepaket")
@RequiredArgsConstructor
public class BoyaVePaketController {

    private final BoyaVePaketService boyaVePaketService;

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/boyaOperation/{operationId}")
    public ResponseMessage<String> boyaOperation(@PathVariable Long operationId, @RequestBody @Valid BoyaVePaketlemeRequest request) {
        return boyaVePaketService.boyaOperation(operationId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/paketOperation/{operationId}")
    public ResponseMessage<String> paketOperation(@PathVariable Long operationId, @RequestBody @Valid BoyaVePaketlemeRequest request) {
        return boyaVePaketService.paketOperation(operationId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/rollback/{operationId}")
    public ResponseMessage<String> rollbackOperation(@PathVariable Long operationId) {
        return boyaVePaketService.rollbackOperation(operationId);
    }
}
