package com.project.payload.mappers;

import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.payload.response.business.process.KaliteKontrolResponse;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Component
public class KaliteKontrolMapper {

    public KaliteKontrolResponse mapKaliteKontrolToResponse(KaliteKontrol kaliteKontrol) {
        return KaliteKontrolResponse.builder()
                .id(kaliteKontrol.getId())
                .kaliteKontrolStage(kaliteKontrol.getKaliteKontrolStage())
                .approveCount(kaliteKontrol.getApproveCount())
                .scrapCount(kaliteKontrol.getScrapCount())
                .returnedToIsilIslem(kaliteKontrol.getReturnedToIsilIslem())
                .returnedToMilTaslama(kaliteKontrol.getReturnedToMilTaslama())
                .active(kaliteKontrol.isActive())
                .build();
    }

    public List<KaliteKontrolResponse> mapKaliteKontrolListToResponse(List<KaliteKontrol> kaliteKontrolList) {
        return kaliteKontrolList.stream()
                .map(this::mapKaliteKontrolToResponse)
                .collect(Collectors.toList());
    }
    
}
