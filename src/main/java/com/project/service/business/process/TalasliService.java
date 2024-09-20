package com.project.service.business.process;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.OrderStatus;
import com.project.domain.concretes.business.process._enums.TalasliOperationType;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.domain.enums.StatusType;
import com.project.payload.mappers.OrderMapper;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.process.TalasliImalatResponse;
import com.project.repository.business.OrderRepository;
import com.project.repository.business.process.TalasliImalatRepository;
import com.project.service.business.OrderStatusService;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.TalasliHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TalasliService {

    private final MethodHelper methodHelper;
    private final OrderStatusService orderStatusService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TalasliHelper talasliHelper;
    private final TalasliImalatRepository talasliImalatRepository;

    public ResponseMessage<String> startStop(Long id) {

        Order order = methodHelper.findOrderById(id);
        Order updatedOrder = talasliHelper.updateOrderStatus(order);
        orderRepository.save(updatedOrder);

        return ResponseMessage.<String>builder()
                .httpStatus(HttpStatus.OK)
                .build();



    }


    @Transactional
    public ResponseMessage<Map<String, TalasliImalatResponse>> milkoparma(int uretilenMilkoparmaSayisi) {
        TalasliImalat milkoparma = talasliHelper.findTalasliImalatByOperationType(TalasliOperationType.MIL_KOPARMA);
        milkoparma.completeOperation(milkoparma.getCompletedQuantity() + uretilenMilkoparmaSayisi);

        TalasliImalat milTornalama = talasliHelper.findTalasliImalatByOperationType(TalasliOperationType.MIL_TORNALAMA);
        if (milTornalama.getStartDate() == null) {
            milTornalama.setStartDate(LocalDateTime.now());
        }
        milTornalama.setRemainingQuantity(milTornalama.getRemainingQuantity() + uretilenMilkoparmaSayisi);

        talasliImalatRepository.save(milkoparma);
        talasliImalatRepository.save(milTornalama);

        Map<String, TalasliImalatResponse> response = new HashMap<>();
        response.put("milkoparma", prepareOperationDetails(milkoparma));
        response.put("miltornalama_remaining", prepareOperationDetails(milTornalama));


        return ResponseMessage.<Map<String, TalasliImalatResponse>>builder()
                .returnBody(response)
                .message(SuccessMessages.MILKOPARMA_COMPLETED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    private TalasliImalatResponse prepareOperationDetails(TalasliImalat operation) {
        return TalasliImalatResponse.builder()
                .id(operation.getId())
                .operationType(operation.getOperationType().name())
                .productionProcessId(operation.getProductionProcess().getId())
                .completedQuantity(operation.getCompletedQuantity())
                .remainingQuantity(operation.getRemainingQuantity())
                .startDate(operation.getStartDate())
                .endDate(operation.getEndDate())
                .isCompleted(operation.getIsCompleted())
                .build();
    }
}
