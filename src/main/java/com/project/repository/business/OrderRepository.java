package com.project.repository.business;

import com.project.domain.concretes.business.Order;
import com.project.domain.enums.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderNumber(String orderNumber);

    Page<Order> findByOrderStatus_StatusTypeIn(Set<StatusType> statuses, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.orderStatus.statusType IN :statusTypes AND o.orderDate BETWEEN :startDate AND :endDate")
    Page<Order> findByStatusTypeAndOrderDateBetween(
            @Param("statusTypes") Set<StatusType> statusTypes,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}
