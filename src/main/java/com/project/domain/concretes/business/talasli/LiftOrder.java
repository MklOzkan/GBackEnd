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
@Table(name = "lift_orders")
public class LiftOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ürün tipi (Lift)
    @Column(name = "product_type")
    private String productType = "LIFT";

    // Toplam sipariş miktarı
    @Column(name = "total_quantity")
    private Integer totalQuantity;

    // Üretimdeki kalan miktar
    @Column(name = "remaining_quantity")
    private Integer remainingQuantity;

    // Sipariş tamamlandı mı?
    @Column(name = "completed")
    private Boolean completed = false;

    // LiftOrder -> LiftOperation ilişkisi
    @OneToMany(mappedBy = "liftOrder", cascade = CascadeType.ALL)
    private List<LiftOperation> operations;

}

