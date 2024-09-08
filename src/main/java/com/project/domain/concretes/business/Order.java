package com.project.domain.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.domain.concretes.business.talasli.BlokliftOperation;
import com.project.domain.concretes.business.talasli.DamperOperation;
import com.project.domain.concretes.business.talasli.LiftOperation;
import com.project.domain.concretes.business.talasli.PaslanmazOperation;
import com.project.domain.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private String customerName;
    @Column(unique = true)
    private String gasanNo;
    @Column(unique = true)
    private String orderNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;
    private String orderType;
    private Integer orderQuantity;
    private Integer readyMilCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private OrderStatus orderStatus;

    // Başlama ve Tamamlama tarihleri
    @Column(name = "production_start_date")
    private LocalDateTime productionStartDate;

    @Column(name = "production_end_date")
    private LocalDateTime productionEndDate;

    // Lift operasyonları ile ilişki
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<LiftOperation> liftOperations;

    // Bloklift operasyonları ile ilişki
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<BlokliftOperation> blokliftOperations;

    // Paslanmaz operasyonları ile ilişki
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<PaslanmazOperation> paslanmazOperations;

    // Damper operasyonları ile ilişki
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<DamperOperation> damperOperations;

    // Üretim başlatıldığında çağrılacak
    public void startProduction() {
        if (this.productionStartDate == null) {
            this.productionStartDate = LocalDateTime.now();  // Başlama tarihi atanır
            this.orderStatus.setStatusType(StatusType.ISLENMEKTE);  // Sipariş durumu üretim aşamasında olarak güncellenir
        }
    }

    // Üretim tamamlandığında çağrılacak
    public void completeProduction() {
        if (this.productionEndDate == null) {
            this.productionEndDate = LocalDateTime.now();  // Tamamlanma tarihi atanır
            this.orderStatus.setStatusType(StatusType.TAMAMLANDI);  // Sipariş durumu tamamlandı olarak güncellenir
        }
    }


    @PrePersist
    public void onPrePersist() {
        if (this.orderDate == null) {
            this.orderDate = LocalDate.now();
        }

    }

}

