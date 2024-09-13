package com.project.domain.concretes.business.liftorder.talasliimalatamiri;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.concretes.business.abstracts.BaseOperation;
import com.project.domain.concretes.business.liftorder.LiftOrder;
import com.project.domain.concretes.business.liftorder._enums.KaliteKontrolStage;
import com.project.domain.concretes.business.liftorder._enums.TalasliOperationType;
import com.project.domain.concretes.business.liftorder.kalitekontrol.KaliteKontrol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "talasli_imalat")
public class TalasliImalat extends BaseOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    private TalasliOperationType operationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "lift_order_id", referencedColumnName = "id")
    private LiftOrder liftOrder;

    @OneToMany(mappedBy = "talasliImalat",cascade = CascadeType.ALL)
    private List<KaliteKontrol> kaliteKontrol;
}
