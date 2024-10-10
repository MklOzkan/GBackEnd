package com.project.repository.business;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.OrderStatus;
import com.project.domain.enums.OrderType;
import com.project.domain.enums.StatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByOrderNumber(String orderNumber);

    Page<Order> findByOrderStatus_StatusNameIn(List<String> statuses, Pageable pageable);

    

    @Query("SELECT o FROM Order o WHERE o.orderStatus.statusName IN :statusNames AND o.deliveryDate BETWEEN :startDate AND :endDate")
    Page<Order> findByStatusTypeAndOrderDateBetween(
            @Param("statusNames") List<String> statusNames,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("SELECT o FROM Order o WHERE o.orderStatus.statusName IN :statusNames AND o.orderType <> com.project.domain.enums.OrderType.PASLANMAZ")
    Page<Order> findByStatusTypeAndOrderTypeNotLike(
            @Param("statusNames") List<String> statusNames,
            Pageable pageable
    );

    Page<Order> findByOrderStatus_StatusName(String status, Pageable pageable);

    boolean existsByGasanNo(String gasanNo);

    boolean existsByOrderNumber(String orderNumber);
}
