package com.project.service.business.process;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.OrderStatus;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.domain.enums.OrderType;
import com.project.domain.enums.StatusType;
import com.project.payload.mappers.OrderMapper;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.OrderRepository;
import com.project.service.business.OrderService;
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
    private final OrderService orderService;

    public ResponseMessage<String> startStop(Long id) {

        Order order = methodHelper.findOrderById(id);
        Order updatedOrder = talasliHelper.updateOrderStatus(order);
        orderRepository.save(updatedOrder);

        return ResponseMessage.<String>builder()
                .httpStatus(HttpStatus.OK)
                .build();



    }


    public ResponseMessage<String> borukesme(Integer quantity, Long id) {

        Order order = methodHelper.findOrderById(id);
        ProductionProcess productionProcessId= talasliHelper.findProductionProcessById(order.getProductionProcess().getId());
        TalasliImalat boruKesme=talasliHelper.findTalasliImalatByProductionProcess(productionProcessId, TalasliOperationType.BORU_KESME_HAVSA);
        boruKesme.completeOperation(quantity);
        if(order.getOrderType().equals(OrderType.LIFT)){

        }


        return ResponseMessage.<String>builder()
                .httpStatus(HttpStatus.OK)
                .build();

    }
}
