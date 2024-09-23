package com.project.payload.request.business.process;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.KaliteKontrolStage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class KaliteKontrolRequest {
    @NotNull(message = "işlem tipi boş olamaz")
    private String kaliteKontrolStage;//kalite kontrol aşaması

    private Integer approveCount =0;//onay sayısı

    private Integer scrapCount =0;//hurda sayısı

    private Integer returnedToIsilIslem =0;//isıl işlem geri dönüş

    private Integer returnedToMilTaslama =0;//mil taşlama geri dönüş

    
}
