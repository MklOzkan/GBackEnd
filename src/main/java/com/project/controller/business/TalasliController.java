package com.project.controller.business;


import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.process.TalasliService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PutMapping("/borukesme/{id}")
    public ResponseMessage<String> borukesmeTalasli(@PathVariable Long id, @RequestParam Integer quantity) {
        return talasliService.borukesme(quantity,id);
    }


}
