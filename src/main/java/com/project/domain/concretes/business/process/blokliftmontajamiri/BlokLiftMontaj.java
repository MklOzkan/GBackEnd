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

    private int lastMilCount;//son mil sayısı

    private Integer pipeCount;//boru sayısı

    private int lastPipeCount;//son boru sayısı

    private Integer scrapPipeCount;//hurda boru sayısı

    private Integer scrapMilCount;//hurda mil sayısı

    private Integer scrapCountAfterTest;//test sonrası hurda sayısı

    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private BlokLiftOperationType operationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "production_process_id", referencedColumnName = "id")
    private ProductionProcess productionProcess;

    public void updateNextMilOperation(int completedQty) {
        this.lastMilCount = completedQty;
        if (this.getRemainingQuantity() == null) {
            this.setRemainingQuantity(0);
        }
        this.setRemainingQuantity(this.getRemainingQuantity() + completedQty);

    }

    public void updateNextMilOperation1(int completedQty) {



        this.lastMilCount = completedQty;
        if (this.getRemainingQuantity() == null) {
            this.setRemainingQuantity(0);
        }
        this.setRemainingQuantity(this.getRemainingQuantity() + completedQty);

    }


    public void updateNextPipeOperation(int completedQty) {
        this.lastPipeCount = completedQty;
        if (this.pipeCount == null) {
            this.pipeCount = 0;
        }
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

    public void updateMilCount(){
        this.milCount+=this.lastMilCount;
    }

    public void updatePipeCount(){
        this.pipeCount+=this.lastPipeCount;
    }

    public void removeLastMilCount(int lastMilCount){

        this.removeLastFromNextOperation(lastMilCount);
        this.lastMilCount=0;
    }

    public void removeLastPipeCount(int lastPipeCount){
        this.pipeCount-=lastPipeCount;
        this.lastPipeCount=0;
    }



}
