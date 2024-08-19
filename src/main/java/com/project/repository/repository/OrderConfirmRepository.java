package com.project.repository.repository;

import com.project.domain.concretes.business.OrderConfirm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderConfirmRepository extends JpaRepository<OrderConfirm, Long> {
    OrderConfirm findByOrderNumber(String orderNumber);
}
