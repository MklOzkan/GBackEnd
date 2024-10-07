package com.project.controller.business;


import com.project.payload.request.business.process.PolisajRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.process.PolisajService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/polisaj")
@RequiredArgsConstructor
public class PolisajController {

    private final PolisajService polisajService;

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    public ResponseMessage<String> updatePolisaj(@PathVariable Long id, @RequestBody @Valid PolisajRequest request) {
        return polisajService.updatePolisaj(id, request);
    }


    @PutMapping("/removeLastChange/{id}")
    @PreAuthorize("hasAnyAuthority('Employee')")
    public ResponseMessage<String>removeLastChange(@PathVariable Long id){
        return polisajService.removeLastChange(id);
    }
}
