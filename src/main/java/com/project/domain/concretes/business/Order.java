package com.project.domain.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.enums.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;//Müşteri adı

    @Column(unique = true)
    private String gasanNo;

    @Column(unique = true)
    private String orderNumber;//Sipariş numarası

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate orderDate;//Sipariş tarihi

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;//Teslim tarihi

    @Enumerated(EnumType.STRING)
    private OrderType orderType;//Sipariş tipi

    private Integer orderQuantity;//Sipariş miktarı

    private Integer readyMilCount;//Hazır mil sayısı

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private OrderStatus orderStatus;//Sipariş durumu

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private ProductionProcess productionProcess;//Üretim süreci

    @PrePersist
    public void onPrePersist() {
        if (this.orderDate == null) {
            this.orderDate = LocalDate.now();
        }
    }
}
