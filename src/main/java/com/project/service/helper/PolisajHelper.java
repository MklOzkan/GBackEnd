package com.project.service.helper;


import com.project.domain.concretes.business.process.polisajamiri.PolisajImalat;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.repository.business.process.PolisajImalatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class PolisajHelper {

    private final MethodHelper methodHelper;
    private final PolisajImalatRepository polisajImalatRepository;

    public void savePolisajWithoutReturn(PolisajImalat polisajImalat) {
        polisajImalatRepository.save(polisajImalat);
    }

    public PolisajImalat findPolisajById(Long id) {
        return polisajImalatRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.POLISAJ_NOT_FOUND, id)));
    }
}
