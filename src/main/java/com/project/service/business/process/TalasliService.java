package com.project.service.business.process;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.OrderStatus;
import com.project.domain.enums.StatusType;
import com.project.payload.mappers.OrderMapper;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.OrderRepository;
import com.project.service.business.OrderStatusService;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.TalasliHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TalasliService {

    private final MethodHelper methodHelper;
    private final OrderStatusService orderStatusService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TalasliHelper talasliHelper;

    public ResponseMessage<String> startStop(Long id) {

        Order order = methodHelper.findOrderById(id);
        Order updatedOrder = talasliHelper.updateOrderStatus(order);
        orderRepository.save(updatedOrder);

        return ResponseMessage.<String>builder()
                .httpStatus(HttpStatus.OK)
                .build();



    }
}
