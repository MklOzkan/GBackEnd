package com.project.domain.concretes.business.abstracts;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

public abstract class BaseOperation {

    @Column(name = "remaining_quantity")
    private Integer remainingQuantity;  // Kalan miktar

    @Column(name = "completed_quantity")
    private Integer completedQuantity;  // Tamamlanan miktar

    @Column(name = "start_date")
    private LocalDateTime startDate;  // Operasyon başlama tarihi

    @Column(name = "end_date")
    private LocalDateTime endDate;  // Operasyon bitiş tarihi

    @Column(name = "is_completed")
    private Boolean isCompleted = false;  // Operasyonun tamamlanma durumu

    @PrePersist
    public void onPrePersist() {
        if (this.startDate == null) {
            this.startDate = LocalDateTime.now();  // Başlama tarihi atanır
        }
    }

    private void rejectOperation(int rejectedQty) { // Reddedilen miktar
        this.remainingQuantity += rejectedQty;  // Kalan miktar
        this.completedQuantity -= rejectedQty;  // Tamamlanan miktar
        if (this.completedQuantity <= 0) {
            this.isCompleted = false;  // Tamamlanan miktar sıfırlanırsa operasyon tamamlanmamış olur
            this.endDate = null;  // Bitiş tarihi sıfırlanır
        }
    }

    // Operasyon tamamlandığında
    public void completeOperation(int completedQty) {
        this.completedQuantity += completedQty;  // Tamamlanan miktar
        this.remainingQuantity -= completedQty;  // Kalan miktar
        if (this.remainingQuantity <= 0) {
            this.isCompleted = true;  // Kalan miktar sıfırlanırsa operasyon tamamlanır
            this.endDate = LocalDateTime.now();  // Bitiş tarihi atanır
        }
    }
}
