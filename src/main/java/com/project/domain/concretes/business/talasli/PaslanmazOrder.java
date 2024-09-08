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
@Table(name = "paslanmaz_orders")
public class PaslanmazOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ürün tipi (Paslanmaz)
    @Column(name = "product_type")
    private String productType = "PASLANMAZ";

    // Toplam sipariş miktarı
    @Column(name = "total_quantity")
    private Integer totalQuantity;

    // Üretimdeki kalan miktar
    @Column(name = "remaining_quantity")
    private Integer remainingQuantity;

    // Sipariş tamamlandı mı?
    @Column(name = "completed")
    private Boolean completed = false;

    // PaslanmazOrder -> PaslanmazOperation ilişkisi
    @OneToMany(mappedBy = "paslanmazOrder", cascade = CascadeType.ALL)
    private List<PaslanmazOperation> operations;

}

