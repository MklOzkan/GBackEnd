package com.project.domain.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.domain.concretes.business.talasli.BlokliftOrder;
import com.project.domain.concretes.business.talasli.DamperOrder;
import com.project.domain.concretes.business.talasli.LiftOrder;
import com.project.domain.concretes.business.talasli.PaslanmazOrder;
import com.project.domain.enums.StatusType;
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

    private String customerName;

    @Column(unique = true)
    private String gasanNo;

    @Column(unique = true)
    private String orderNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate orderDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deliveryDate;

    private String orderType;

    private Integer orderQuantity;

    private Integer readyMilCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private OrderStatus orderStatus;

    @Column(name = "production_start_date")
    private LocalDateTime productionStartDate;

    @Column(name = "production_end_date")
    private LocalDateTime productionEndDate;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private LiftOrder liftOrder;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private BlokliftOrder blokliftOrder;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private DamperOrder damperOrder;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private PaslanmazOrder paslanmazOrder;

    public void startProduction() {
        if (this.productionStartDate == null) {
            this.productionStartDate = LocalDateTime.now();
            this.orderStatus.setStatusType(StatusType.ISLENMEKTE);
        }
    }

    public void completeProduction() {
        if (this.productionEndDate == null) {
            this.productionEndDate = LocalDateTime.now();
            this.orderStatus.setStatusType(StatusType.TAMAMLANDI);
        }
    }

    @PrePersist
    public void onPrePersist() {
        if (this.orderDate == null) {
            this.orderDate = LocalDate.now();
        }
    }
}
