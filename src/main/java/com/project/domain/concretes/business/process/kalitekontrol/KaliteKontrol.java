package com.project.domain.concretes.business.process.kalitekontrol;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.KaliteKontroOperationType;
import com.project.domain.concretes.business.process._enums.KaliteKontrolStage;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.payload.request.business.process.KaliteKontrolRequest;
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

    private int milCount;//mil sayısı

    private int approveCount;//onay sayısı

    private int scrapCount;//hurda sayısı

    private int returnedToIsilIslem;//isıl işlem geri dönüş

    private int returnedToMilTaslama;//mil taşlama geri dönüş

    private int lastApproveCount;//son onay sayısı

    private int lastScrapCount;//son hurda sayısı

    private int lastReturnedToIsilIslem;//son isıl işlem geri dönüş

    private int lastReturnedToMilTaslama;//son mil taşlama geri dönüş

    private boolean active = false;  // Operasyonun aktif olup olmadığı


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;



    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "production_process_id", referencedColumnName = "id")
    private ProductionProcess productionProcess;

    public void approvedPart(int approveCount) {
        this.approveCount += approveCount;
        this.milCount -= approveCount;
        this.lastApproveCount = approveCount;
    }

    public void scrapPart(int scrapCount) {
        this.scrapCount += scrapCount;
        this.milCount -= scrapCount;
        this.lastScrapCount = scrapCount;
    }

    public void returnedToIsilIslem(int returnedToIsilIslem) {
        this.returnedToIsilIslem += returnedToIsilIslem;
        this.milCount -= returnedToIsilIslem;
        this.lastReturnedToIsilIslem = returnedToIsilIslem;
    }

    public void returnedToMilTaslama(int returnedToMilTaslama) {
        this.returnedToMilTaslama += returnedToMilTaslama;
        this.milCount -= returnedToMilTaslama;
        this.lastReturnedToMilTaslama = returnedToMilTaslama;
    }

    public void completedPart(KaliteKontrolRequest request) {

        if (request.getApproveCount() > 0) {
            this.approveCount += request.getApproveCount();
            this.milCount -= request.getApproveCount();
            this.lastApproveCount = request.getApproveCount();
        }
        if (request.getScrapCount() > 0) {
            this.scrapCount += request.getScrapCount();
            this.milCount -= request.getScrapCount();
            this.lastScrapCount = request.getScrapCount();
        }
        if (request.getReturnedToIsilIslem() > 0) {
            this.returnedToIsilIslem += request.getReturnedToIsilIslem();
            this.milCount -= request.getReturnedToIsilIslem();
            this.lastReturnedToIsilIslem = request.getReturnedToIsilIslem();
        }
        if (request.getReturnedToMilTaslama() > 0) {
            this.returnedToMilTaslama += request.getReturnedToMilTaslama();
            this.milCount -= request.getReturnedToMilTaslama();
            this.lastReturnedToMilTaslama = request.getReturnedToMilTaslama();
        }
    }

    public void nextOperationByKaliteKontrol(int completedQty) {
        this.milCount += completedQty;
    }

    public void rollBackLastScrap(){
        this.scrapCount -= this.lastScrapCount;
        this.milCount += this.lastScrapCount;
        this.lastScrapCount = 0;
    }

    public void rollBackLastApprove(){
        this.approveCount -= this.lastApproveCount;
        this.milCount += this.lastApproveCount;
        this.lastApproveCount = 0;
    }

    public void rollBackLastReturnedToIsilIslem(){
        this.returnedToIsilIslem -= this.lastReturnedToIsilIslem;
        this.milCount += this.lastReturnedToIsilIslem;
        this.lastReturnedToIsilIslem = 0;
    }

    public void rollBackLastReturnedToMilTaslama(){
        this.returnedToMilTaslama -= this.lastReturnedToMilTaslama;
        this.milCount += this.lastReturnedToMilTaslama;
        this.lastReturnedToMilTaslama = 0;
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
    }

    public void rollbackLastApproveCount(int lastApproveCount) {
        this.approveCount -= lastApproveCount;
    }
    
}
