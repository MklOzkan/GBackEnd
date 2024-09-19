package com.project.payload.response.business.process;


import com.project.domain.concretes.business.process._enums.KaliteKontroOperationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class KaliteKontrolResponse {
    private Long id;
    private KaliteKontroOperationType kaliteKontrolStage;//kalite kontrol aşaması

    private Integer approveCount;//onay sayısı

    private Integer scrapCount;//hurda sayısı

    private Integer returnedToIsilIslem;//isıl işlem geri dönüş

    private Integer returnedToMilTaslama;//mil taşlama geri dönüş

    
}