package com.project.service.business;


import com.project.domain.concretes.business.OrderStatus;
import com.project.domain.enums.StatusType;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.business.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;

    public OrderStatus getOrderStatus(StatusType statusType) {
        return orderStatusRepository.findByStatusType(statusType).orElseThrow(
                ()-> new ResourceNotFoundException("Order status is not found")
        );

    }

    public List<OrderStatus> getAllOrderStatuses() {
        return orderStatusRepository.findAll();
    }
}
