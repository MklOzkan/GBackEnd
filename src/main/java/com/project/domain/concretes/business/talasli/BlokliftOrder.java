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
@Table(name = "bloklift_orders")
public class BlokliftOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ürün tipi (Bloklift)
    @Column(name = "product_type")
    private String productType = "BLOKLIFT";

    // Toplam sipariş miktarı
    @Column(name = "total_quantity")
    private Integer totalQuantity;

    // Üretimdeki kalan miktar
    @Column(name = "remaining_quantity")
    private Integer remainingQuantity;

    // Sipariş tamamlandı mı?
    @Column(name = "completed")
    private Boolean completed = false;

    // BlokliftOrder -> BlokliftOperation ilişkisi
    @OneToMany(mappedBy = "blokliftOrder", cascade = CascadeType.ALL)
    private List<BlokliftOperation> operations;
}

