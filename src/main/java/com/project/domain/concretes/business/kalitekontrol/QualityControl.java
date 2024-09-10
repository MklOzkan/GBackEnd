package com.project.domain.concretes.business.kalitekontrol;

import com.project.domain.concretes.business.kalitekontrol.enums.OperationType;
import com.project.domain.concretes.business.talasli.BlokliftOperation;
import com.project.domain.concretes.business.talasli.DamperOperation;
import com.project.domain.concretes.business.talasli.LiftOperation;
import com.project.domain.concretes.business.talasli.PaslanmazOperation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "quality_controls")
public class QualityControl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kontrol edilen toplam adet
    @Column(name = "total_checked_quantity")
    private Integer totalCheckedQuantity;

    // Onaylanan ürün adedi
    @Column(name = "approved_quantity")
    private Integer approvedQuantity;

    // Hurdaya ayrılan ürün adedi
    @Column(name = "scrap_quantity")
    private Integer scrapQuantity;

    // Geri gönderilen işlem tipi (Enum: ISIL_ISLEM, MIL_TASLAMA vb.)
    @Enumerated(EnumType.STRING)
    @Column(name = "returned_operation")
    private OperationType returnedOperation;

    // Geri gönderilen ürün adedi
    @Column(name = "returned_quantity")
    private Integer returnedQuantity;

    // İşlem tamamlandı mı?
    @Column(name = "completed")
    private Boolean completed = false;

    // İlgili operasyon (Lift için)
    @OneToOne
    @JoinColumn(name = "lift_operation_id")
    private LiftOperation liftOperation;

    // İlgili operasyon (Damper için)
    @OneToOne
    @JoinColumn(name = "damper_operation_id")
    private DamperOperation damperOperation;

    // İlgili operasyon (Bloklift için)
    @OneToOne
    @JoinColumn(name = "bloklift_operation_id")
    private BlokliftOperation blokliftOperation;

    // İlgili operasyon (Paslanmaz için)
    @OneToOne
    @JoinColumn(name = "paslanmaz_operation_id")
    private PaslanmazOperation paslanmazOperation;

    // Getters ve Setters
}


