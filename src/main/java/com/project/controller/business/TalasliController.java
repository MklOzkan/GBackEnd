package com.project.controller.business;


import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.process.TalasliImalatResponse;
import com.project.service.business.process.TalasliService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/milkoparma")
    public ResponseMessage<Map<String, TalasliImalatResponse>> milkoparma(@RequestBody Map<String, Integer> requestBody){
        int uretilenMilkoparmaSayisi = requestBody.get("uretilenMilkoparmaSayisi");
        return talasliService.milkoparma(uretilenMilkoparmaSayisi);
    }


}
