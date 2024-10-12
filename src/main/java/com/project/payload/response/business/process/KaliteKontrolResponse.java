package com.project.payload.response.business.process;


import com.project.domain.concretes.business.process._enums.KaliteKontroOperationType;
import com.project.domain.concretes.business.process._enums.KaliteKontrolStage;
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
    private KaliteKontrolStage kaliteKontrolStage;//kalite kontrol aşaması

    private int milCount;//mil sayısı

    private Integer approveCount;//onay sayısı

    private Integer scrapCount;//hurda sayısı

    private Integer returnedToIsilIslem;//isıl işlem geri dönüş

    private Integer returnedToMilTaslama;//mil taşlama geri dönüş

    private int lastApproveCount;//son onay sayısı

    private int lastScrapCount;//son hurda sayısı

    private int lastReturnedToIsilIslem;//son isıl işlem geri dönüş

    private int lastReturnedToMilTaslama;//son mil taşlama geri dönüş

    private Long productionProcessId;//üretim işlemi id

    private boolean active;  // Operasyonun aktif olup olmadığı

    
}
