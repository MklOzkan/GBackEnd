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

    private String kaliteKontrolStage;//kalite kontrol aşaması

    private String rollBack;//geri alınacak alan

    private int milCount =0;//mil sayısı

    private int approveCount =0;//onay sayısı

    private int scrapCount =0;//hurda sayısı

    private int returnedToIsilIslem =0;//isıl işlem geri dönüş

    private int returnedToMilTaslama =0;//mil taşlama geri dönüş

    private Long productionProcessId;//üretim işlemi id

    private boolean active = false;  // Operasyonun aktif olup olmadığı

    
}
