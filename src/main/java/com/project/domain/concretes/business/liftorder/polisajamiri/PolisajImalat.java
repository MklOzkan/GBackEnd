package com.project.domain.concretes.business.liftorder.polisajamiri;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.concretes.business.abstracts.BaseOperation;
import com.project.domain.concretes.business.liftorder.LiftOrder;
import com.project.domain.concretes.business.liftorder.talasliimalatamiri.TalasliImalat;
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
@Table(name = "polisaj_imalat")
public class PolisajImalat extends BaseOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operationType="POLISAJ";

    @OneToOne
    @JoinColumn(name = "talasli_imalat_id", referencedColumnName = "id")
    private TalasliImalat talasliImalat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "lift_order_id", referencedColumnName = "id")
    private LiftOrder liftOrder;



}
