package com.project.domain.concretes.business.liftorder.kalitekontrol;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.concretes.business.liftorder.LiftOrder;
import com.project.domain.concretes.business.liftorder._enums.KaliteKontrolApproveType;
import com.project.domain.concretes.business.liftorder._enums.KaliteKontrolStage;
import com.project.domain.concretes.business.liftorder.liftmontajamiri.LiftMontaj;
import com.project.domain.concretes.business.liftorder.talasliimalatamiri.TalasliImalat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "kalite_kontrol_approve_type")
    @Enumerated(EnumType.STRING)
    private KaliteKontrolApproveType kaliteKontrolApproveType;

    @Column(name = "kalite_kontrol_stage")
    @Enumerated(EnumType.STRING)
    private KaliteKontrolStage kaliteKontrolStage;

    private Integer approveCount;

    private Integer rejectCount;

    private Integer hurdaCount;


    @OneToOne
    private LiftMontaj liftMontaj;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "lift_order_id", referencedColumnName = "id")
    private LiftOrder liftOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "talasli_imalat_id", referencedColumnName = "id")
    private TalasliImalat talasliImalat;

    private void approve(int onaylanan){
        this.approveCount+=onaylanan;
        if (liftOrder.getOrder().getOrderQuantity()<=0){

        }
    }

    private void reject(int reddedilen, KaliteKontrolApproveType nextApproveType){
        this.rejectCount+=reddedilen;
        if (nextApproveType.equals(KaliteKontrolApproveType.HURDA)){
            this.hurdaCount+=reddedilen;

        }
    }
    
}
