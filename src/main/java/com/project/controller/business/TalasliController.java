package com.project.controller.business;


import com.project.payload.request.business.process.TalasliImalatRequest;
import com.project.payload.response.business.MultipleResponses;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.process.TalasliImalatResponse;
import com.project.service.business.process.TalasliService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/talasli")
@RequiredArgsConstructor
public class TalasliController {

    private final TalasliService talasliService;

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/startStop/{id}")
    public ResponseMessage<String> startStopTalasli(@PathVariable Long id) {
        return talasliService.startStop(id);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/milkoparma/{operationId}")
    public ResponseMessage<String> milkoparma(@RequestBody @Valid TalasliImalatRequest  request, @PathVariable Long operationId) {

        return talasliService.milkoparma(request, operationId);
    }


    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/borukesme/{id}")
    public ResponseMessage<String> borukesmeTalasli(@PathVariable Long id, @RequestParam Integer quantity) {
        return talasliService.borukesme(quantity,id);
    }


}
