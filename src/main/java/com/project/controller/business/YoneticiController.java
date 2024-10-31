package com.project.controller.business;


import com.project.payload.response.business.OrderResponse;
import com.project.payload.response.business.Responses;
import com.project.payload.response.business.process.*;
import com.project.service.business.YoneticiService;
import com.project.service.helper.PageableHelper;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/yonetici")
@RequiredArgsConstructor
public class YoneticiController {

    private final YoneticiService yoneticiService;
    private final PageableHelper pageableHelper;

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @GetMapping("/reports/{id}")
    public Responses<OrderResponse,
            ProductionProcessResponse,
            List<TalasliImalatResponse>,
            PolisajResponse,
            List<LiftResponse>,
            List<BlokLiftResponse>,
            List<BoyaVePaketlemeResponse>,
            List<KaliteKontrolResponse>,
            Page<OrderResponse>> yoneticiIslemleri(@PathVariable Long id,
                                                   @RequestParam(value = "page", defaultValue = "0") @Min(0) int page,
                                                   @RequestParam(value = "size", defaultValue = "5") @Min(1) int size,
                                                   @RequestParam(value = "sort", defaultValue = "orderDate") String sort,
                                                   @RequestParam(value = "type", defaultValue = "desc") String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        return yoneticiService.yoneticiIslemleri(id, pageable);
    }
}
