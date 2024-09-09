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
@Table(name = "bloklift_orders")
public class BlokliftOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_type")
    private String productType = "BLOKLIFT";

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @Column(name = "remaining_quantity")
    private Integer remainingQuantity;

    @Column(name = "completed")
    private Boolean completed = false;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @OneToMany(mappedBy = "blokliftOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlokliftOperation> operations;
}
