package com.project.repository.business;

import com.project.domain.concretes.business.OrderStatus;
import com.project.domain.enums.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {


    Optional<OrderStatus> findByStatusType(StatusType statusType);
}
