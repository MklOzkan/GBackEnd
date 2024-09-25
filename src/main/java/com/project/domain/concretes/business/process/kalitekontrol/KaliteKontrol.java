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

    private int lastApproveCount;//son onay sayısı

    private int lastScrapCount;//son hurda sayısı

    private int lastReturnedToIsilIslem;//son isıl işlem geri dönüş

    private int lastReturnedToMilTaslama;//son mil taşlama geri dönüş

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;



    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "production_process_id", referencedColumnName = "id")
    private ProductionProcess productionProcess;

    public void completedPart(int approveCount, int scrapCount, int returnedToIsilIslem, int returnedToMilTaslama) {
        if (this.approveCount == null) {
            this.approveCount = 0;
        }
        if (this.scrapCount == null) {
            this.scrapCount = 0;
        }
        if (this.returnedToIsilIslem == null) {
            this.returnedToIsilIslem = 0;
        }
        if (this.returnedToMilTaslama == null) {
            this.returnedToMilTaslama = 0;
        }

        if (approveCount > 0) {
            this.approveCount += approveCount;
            this.milCount -= approveCount;
            this.lastApproveCount = approveCount;
        }
        if (scrapCount > 0) {
            this.scrapCount += scrapCount;
            this.milCount -= scrapCount;
            this.lastScrapCount = scrapCount;
        }
        if (returnedToIsilIslem > 0) {
            this.returnedToIsilIslem += returnedToIsilIslem;
            this.milCount -= returnedToIsilIslem;
            this.lastReturnedToIsilIslem = returnedToIsilIslem;
        }
        if (returnedToMilTaslama > 0) {
            this.returnedToMilTaslama += returnedToMilTaslama;
            this.milCount -= returnedToMilTaslama;
            this.lastReturnedToMilTaslama = returnedToMilTaslama;
        }

        if (this.milCount == 0) {
            this.endDate = LocalDateTime.now();
        }
    }

    public void nextOperationByKaliteKontrol(int completedQty) {
        if (this.milCount == null) {
            this.milCount = 0;
            this.startDate = LocalDateTime.now();
        }
        this.milCount += completedQty;
    }

    public void removeLastCompletedQty() {
        this.approveCount -= this.lastApproveCount;
        this.scrapCount -= this.lastScrapCount;
        this.returnedToIsilIslem -= this.lastReturnedToIsilIslem;
        this.returnedToMilTaslama -= this.lastReturnedToMilTaslama;
        this.milCount += (this.lastApproveCount + this.lastScrapCount + this.lastReturnedToIsilIslem + this.lastReturnedToMilTaslama);
        this.lastApproveCount = 0;
        this.lastScrapCount = 0;
        this.lastReturnedToIsilIslem = 0;
        this.lastReturnedToMilTaslama = 0;
    }

    public void removeLastFromNextOperation(int lastAmount) {
        this.milCount -= lastAmount;
        if (this.milCount == 0 && this.startDate != null) {
            this.startDate = null;
        }
    }
    
}
