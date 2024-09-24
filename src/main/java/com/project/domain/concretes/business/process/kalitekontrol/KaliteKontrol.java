package com.project.domain.concretes.business.process.kalitekontrol;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.KaliteKontroOperationType;
import com.project.domain.concretes.business.process._enums.KaliteKontrolStage;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "kalite_kontrol")
public class KaliteKontrol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kalite_kontrol_stage")
    @Enumerated(EnumType.STRING)
    private KaliteKontrolStage kaliteKontrolStage;//kalite kontrol aşaması

    private Integer milCount;//mil sayısı

    private Integer approveCount;//onay sayısı

    private Integer scrapCount;//hurda sayısı

    private Integer returnedToIsilIslem;//isıl işlem geri dönüş

    private Integer returnedToMilTaslama;//mil taşlama geri dönüş

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;



    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "production_process_id", referencedColumnName = "id")
    private ProductionProcess productionProcess;
    
}
