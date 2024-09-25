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

    private Integer milCount;//mil sayısı

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

    public void removeLastMilCount(){
        this.milCount-=this.lastMilCount;
        this.removeLastFromNextOperation(this.lastMilCount);
        this.lastMilCount=0;
    }

    public void removeLastPipeCount(){
        this.pipeCount-=this.lastPipeCount;
        this.removeLastFromNextOperation(this.lastPipeCount);
        this.lastPipeCount=0;
    }



}
