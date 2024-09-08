package com.project.domain.concretes.business.talasli;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "damper_orders")
public class DamperOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ürün tipi (Damper)
    @Column(name = "product_type")
    private String productType = "DAMPER";

    // Toplam sipariş miktarı
    @Column(name = "total_quantity")
    private Integer totalQuantity;

    // Üretimdeki kalan miktar
    @Column(name = "remaining_quantity")
    private Integer remainingQuantity;

    // Sipariş tamamlandı mı?
    @Column(name = "completed")
    private Boolean completed = false;

    // DamperOrder -> DamperOperation ilişkisi
    @OneToMany(mappedBy = "damperOrder", cascade = CascadeType.ALL)
    private List<DamperOperation> operations;
}

