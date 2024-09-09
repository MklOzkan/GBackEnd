package com.project.domain.concretes.business.talasli;

import com.project.domain.concretes.business.Order;
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

    @Column(name = "product_type")
    private String productType = "PASLANMAZ";

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @Column(name = "remaining_quantity")
    private Integer remainingQuantity;

    @Column(name = "completed")
    private Boolean completed = false;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;  // Order ile ilişkilendirme

    // PaslanmazOrder -> PaslanmazOperation ilişkisi
    @OneToMany(mappedBy = "paslanmazOrder", cascade = CascadeType.ALL)
    private List<PaslanmazOperation> operations;
}
