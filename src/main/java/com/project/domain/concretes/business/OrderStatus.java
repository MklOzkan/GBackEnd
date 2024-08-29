package com.project.domain.concretes.business;

import com.project.domain.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_ordet_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatus {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Enumerated(EnumType.STRING)
        private StatusType statusType;

        private String statusName;

}
