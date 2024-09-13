package com.project.domain.concretes.business.liftorder.boyavepaket;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.concretes.business.abstracts.BaseOperation;
import com.project.domain.concretes.business.liftorder._enums.BoyaPaketOperationType;
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
@Table(name = "boya_ve_paketleme")
public class BoyaVePaketleme extends BaseOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private BoyaPaketOperationType operationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "lift_order_id", referencedColumnName = "id")
    private LiftOrder liftOrder;



}
