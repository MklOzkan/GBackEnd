package com.project.domain.concretes.business.process.blokliftmontajamiri;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.concretes.business.abstracts.BaseOperation;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.BlokLiftOperationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "blok_lift_montaj")
public class BlokLiftMontaj extends BaseOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int milCount;//mil sayısı

    private int pipeCount;//boru sayısı

    private int scrapPipeCount;//hurda boru sayısı

    private int scrapMilCount;//hurda mil sayısı

    private int scrapCountAfterTest;//test sonrası hurda sayısı

    private int lastCompletedScrapCount;//son tamamlanan hurda sayısı

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private BlokLiftOperationType operationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "production_process_id", referencedColumnName = "id")
    private ProductionProcess productionProcess;

    public void updateNextMilOperation(int completedQty) {
        this.milCount += completedQty;
    }

    public void updateNextMilOperation1(int completedQty) {
        this.setRemainingQuantity(this.getRemainingQuantity() + completedQty);
    }


    public void updateNextPipeOperation(int completedQty) {
        this.pipeCount += completedQty;
    }

    public void updateCompletedQuantity(){

        if (this.milCount>=this.pipeCount){
            this.startOperation();
            this.setLastCompletedQty(this.pipeCount);
            this.setCompletedQuantity(this.getCompletedQuantity()+this.getLastCompletedQty());
            this.milCount-=this.pipeCount;
            this.pipeCount=0;
        }else {
            this.startOperation();
            this.setCompletedQuantity(this.getCompletedQuantity()+this.milCount);
            this.pipeCount-=this.milCount;
            this.milCount=0;
        }
    }



    public void removeLastMilCount(int lastMilCount){

        this.removeLastFromNextOperation(lastMilCount);

    }

    public void removeLastPipeCount(int lastPipeCount){
        this.pipeCount-=lastPipeCount;

    }

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


