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
    @PutMapping("/boruKapamaForBL/{operationId}")
    public ResponseMessage<String> boruKapamaOperationForBL(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.boruKapamaOperationForBL(operationId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/boruKapamaForLift/{operationId}")
    public ResponseMessage<String> boruKapamaOperationForLift(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.boruKapamaOperationForLift(operationId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/boruKaynakForBL/{operationId}")
    public ResponseMessage<String> boruKaynakOperationForBL(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.boruKaynakOperationForBl(operationId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/boruKaynakForLift/{operationId}")
    public ResponseMessage<String> boruKaynakOperationForLift(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.boruKaynakOperationForLift(operationId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/montajOperationForBL/{operationId}")
    public ResponseMessage<String> montajOperation(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.montajOperationForBl(operationId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/montajOperationForLift/{operationId}")
    public ResponseMessage<String> montajOperationForLift(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.montajOperationForLift(operationId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/blBoruKapamaOperation/{operationId}")
    public ResponseMessage<String> blockLiftBoruKapamaOperation(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.blokLiftBoruKapamaOperation(operationId, request);
    }


    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/gazDolumOperationForBL/{operationId}")
    public ResponseMessage<String> gazDolumOperationForBL(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.gazDolumOperationForBL(operationId, request);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/gazDolumOperationForLift/{operationId}")
    public ResponseMessage<String> gazDolumOperationForLift(@PathVariable Long operationId, @RequestBody @Valid MontajRequest request) {
        return montajService.gazDolumOperationForLift(operationId, request);
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

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/removelastchangeForBL/{operationId}")
    public ResponseMessage<String> removeLastChangeFromBlokliftMontaj(@PathVariable Long operationId) {
        return montajService.removeLastChangeFromBlokliftMontaj(operationId);
    }

    @PreAuthorize("hasAnyAuthority('Admin','Employee')")
    @PutMapping("/removelastchangeForLift/{operationId}")
    public ResponseMessage<String> removeLastChangeFromLiftMontaj(@PathVariable Long operationId) {
        return montajService.removeLastChangeFromLiftMontaj(operationId);
    }





}
