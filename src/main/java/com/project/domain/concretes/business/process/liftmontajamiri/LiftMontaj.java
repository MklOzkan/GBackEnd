package com.project.domain.concretes.business.process.liftmontajamiri;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.concretes.business.abstracts.BaseOperation;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.LiftMontajOperationTye;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lift_montaj")
public class LiftMontaj extends BaseOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int milCount;//mil sayısı

    private int pipeCount;//boru sayısı

    private int lastMilCount;//son mil sayısı

    private int scrapPipeCount;//hurda boru sayısı

    private int scrapMilCount;//hurda mil sayısı

    private int scarapCountAfterTest;//test sonrası hurda sayısı

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private LiftMontajOperationTye operationType;//işlem tipi

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "production_process_id", referencedColumnName = "id")
    private ProductionProcess productionProcess;

    public void updateNextMilOperation(int completedQty) {
        this.milCount += completedQty;
    }

    public void updateNextPipeOperation(int completedQty) {
        this.pipeCount += completedQty;
    }

    //Eger kalite kontrol hatali giris yaparsa geri almak icin kullanilir
    public void rollbackNextMilCount(int rollbackQty) {
        this.milCount -= rollbackQty;
    }


    public void  updateAccordingToPipeAndMilCount(){
        if (this.pipeCount<=this.milCount){
            this.setRemainingQuantity(this.pipeCount-this.getCompletedQuantity());
        }else{
            this.setRemainingQuantity(this.milCount-this.getCompletedQuantity());
        }
    }
}
