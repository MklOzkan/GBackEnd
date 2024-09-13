package com.project.domain.concretes.business.liftorder.liftmontajamiri;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.concretes.business.abstracts.BaseOperation;
import com.project.domain.concretes.business.liftorder._enums.TalasliOperationType;
import com.project.domain.concretes.business.liftorder.LiftOrder;
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

    @Column(name = "operation_type")
    private TalasliOperationType operationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "lift_order_id", referencedColumnName = "id")
    private LiftOrder liftOrder;
}
