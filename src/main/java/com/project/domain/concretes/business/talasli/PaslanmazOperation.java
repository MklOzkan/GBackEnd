package com.project.domain.concretes.business.talasli;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.kalitekontrol.QualityControl;
import com.project.domain.concretes.business.talasli.enums.PaslanmazOperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "paslanmaz_operations")
public class PaslanmazOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type")
    private PaslanmazOperationType operationType;

    @Column(name = "remaining_quantity")
    private Integer remainingQuantity;

    @Column(name = "completed_quantity")
    private Integer completedQuantity;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    // PaslanmazOperation -> Order ilişkisi
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_operation_id")
    private PaslanmazOperation nextOperation;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @PrePersist
    public void onPrePersist() {
        if (this.startDate == null) {
            this.startDate = LocalDateTime.now();
        }
    }

    public void completeOperation() {
        this.isCompleted = true;
        this.endDate = LocalDateTime.now();  // Üretim bitiş tarihi atanır
    }

}



