package com.project.domain.concretes.business.abstracts;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseOperation {

    @Column(name = "remaining_quantity")
    private int remainingQuantity;  // Kalan miktar

    @Column(name = "completed_quantity")
    private int completedQuantity;  // Tamamlanan miktar

    @Column(name = "start_date")
    private LocalDateTime startDate;  // Operasyon başlama tarihi

    @Column(name = "end_date")
    private LocalDateTime endDate;  // Operasyon bitiş tarihi

    @Column(name = "is_completed")
    private Boolean isCompleted = false;  // Operasyonun tamamlanma durumu

    private boolean active = false;  // Operasyonun aktif olup olmadığı

    private int lastCompletedQty;  // Son tamamlanan miktar

    public void startOperation() {
        this.startDate = this.startDate == null ? LocalDateTime.now() : this.startDate;  // Başlangıç tarihi
    }

    //kalite kontrolden geri dönüş olduğunda kullanılır
    public void returnedToOperation(int returnedQty) {
        this.remainingQuantity += returnedQty;  // Kalan miktar
        this.completedQuantity -= returnedQty;  // Tamamlanan miktar
    }
    //kalite kontrolde geri dönüş olduğunda kullanılır
    public void decreaseCompletedQuantity(int decreaseQty) {
        this.completedQuantity -= decreaseQty;  // Tamamlanan miktar
    }
    //Yanlışlıkla yapılan işlemi geri almak için kullanılır
    public void rollbackOperation(int rollbackQty) {
        this.remainingQuantity -= rollbackQty;  // Kalan miktar
        this.completedQuantity += rollbackQty;  // Tamamlanan miktar
    }
    //Yanlışlıkla yapılan işlemi geri almak için kullanılır
    public void increaseCompletedQuantity(int increaseQty) {
        this.completedQuantity += increaseQty;  // Tamamlanan miktar
    }

    // Operasyon tamamlandığında
    public void completeOperation(int completedQty) {
        this.completedQuantity += completedQty;  // Tamamlanan miktar
        this.remainingQuantity -= completedQty;  // Kalan miktar
        this.lastCompletedQty = completedQty;  // Son tamamlanan miktar
    }

    public void updateNextOperation(int completedQty) {
        this.remainingQuantity += completedQty;
    }

    public void removeLastCompletedQty() {
        this.completedQuantity -= this.lastCompletedQty;
        this.remainingQuantity += this.lastCompletedQty;
        this.lastCompletedQty = 0;
    }

    public void removeLastFromNextOperation(int lastCompletedQty) {
        this.remainingQuantity -= lastCompletedQty;
    }
}
