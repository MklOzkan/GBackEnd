package com.project.controller.business;

import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.payload.request.business.process.MontajRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.process.MontajService;
import com.project.service.helper.KaliteKontrolHelper;
import com.project.service.helper.TalasliHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/montaj")
@RequiredArgsConstructor
public class MontajController {

    private final MontajService montajService;
    private KaliteKontrolHelper kaliteKontrolHelper;
    private final TalasliHelper talasliHelper;

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/boruKapama/{operationId}")
    public ResponseMessage<String> boruKapamaOperation(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.boruKapamaOperation(operationId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/boruKaynak/{operationId}")
    public ResponseMessage<String> boruKaynakOperation(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.boruKaynakOperation(operationId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/montajOperation/{operationId}")
    public ResponseMessage<String> montajOperation(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.montajOperation(operationId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/blBoruKapamaOperation/{operationId}")
    public ResponseMessage<String> blockLiftBoruKapamaOperation(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.blokLiftBoruKapamaOperation(operationId, request);
    }


    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/gazDolumOperation/{operationId}")
    public ResponseMessage<String> gazDolumOperation(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.gazDolumOperation(operationId, request);
    }
    //TODO: Cahit&Adem
    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/baslikTakmaOperation/{operationId}")
    public ResponseMessage<String> baslikTakmaOperation(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.baslikTakmaOperation(operationId, request);
    }

    //TODO: Cahit&Adem
    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/testOperation/{operationId}")
    public ResponseMessage<String> testOperation(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.testOperation(operationId, request);
    }







}
