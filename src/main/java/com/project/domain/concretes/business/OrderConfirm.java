package com.project.domain.concretes.business;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name = "order_confirm")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    @Column(unique = true)
    private String gasanNo;
    @Column(unique = true)
    private String orderNumber;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String orderType;
    private Integer orderQuantity;
    private Integer readyMilCount;


    @PrePersist
    public void onPrePersist() {
        if (this.orderDate == null) {
            this.orderDate = LocalDate.now();
        }
//        if (this.gasanNo == null) {
//            this.gasanNo = generateGasanNo();
//        }
    }

//    private String generateGasanNo() {
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder();
//
//        // 4 rakam ekle
//        for (int i = 0; i < 4; i++) {
//            sb.append(random.nextInt(10));
//        }
//
//        // ilk 4 hane sonrası bir boşluk bırak
//        sb.append(' ');
//
//        // 1-3 arası harf ekle
//        int lettersCount = 1 + random.nextInt(3); // Generates 1, 2, or 3
//        for (int i = 0; i < lettersCount; i++) {
//            char letter = (char) (random.nextInt(26) + 'A'); // Uppercase letters A-Z
//            sb.append(letter);
//        }
//
//        // ilk 4 hane sonrası bir boşluk bırak
//        sb.append(' ');
//
//        // 6 rakam ekle
//        for (int i = 0; i < 6; i++) {
//            sb.append(random.nextInt(10));
//        }
//
//        return sb.toString();
//    }
}

