package com.project.domain.concretes.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.domain.concretes.user.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "order_confirm")
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

