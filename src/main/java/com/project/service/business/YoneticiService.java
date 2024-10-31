package com.project.service.business;


import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import com.project.domain.concretes.business.process.boyavepaket.BoyaVePaketleme;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.domain.concretes.business.process.polisajamiri.PolisajImalat;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.domain.enums.OrderType;
import com.project.payload.mappers.*;
import com.project.payload.response.business.OrderResponse;
import com.project.payload.response.business.Responses;
import com.project.payload.response.business.process.*;
import com.project.repository.business.OrderRepository;
import com.project.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YoneticiService {

    private final MethodHelper methodHelper;
    private final OrderMapper orderMapper;
    private final ProductionProcessMapper productionProcessMapper;
    private final TalasliMapper talasliMapper;
    private final PolisajMapper polisajMapper;
    private final LiftMapper liftMapper;
    private final BlokLiftMapper blokLiftMapper;
    private final BoyaVePaketMapper boyaVePaketlemeMapper;
    private final KaliteKontrolMapper kaliteKontrolMapper;
    private final OrderRepository orderRepository;

    public Responses<OrderResponse,
            ProductionProcessResponse,
            List<TalasliImalatResponse>,
            PolisajResponse,
            List<LiftResponse>,
            List<BlokLiftResponse>,
            List<BoyaVePaketlemeResponse>,
            List<KaliteKontrolResponse>,
            Page<OrderResponse>> yoneticiIslemleri(Long id, Pageable pageable) {

        Order order = methodHelper.findOrderById(id);
        ProductionProcess productionProcess = order.getProductionProcess();
        List<TalasliImalat> talasliImalatList = productionProcess.getTalasliOperations();
        PolisajImalat polisaj = null;
        if (!order.getOrderType().equals(OrderType.PASLANMAZ)){
            polisaj = productionProcess.getPolisajOperation();
        }
        List<LiftMontaj> liftList = productionProcess.getLiftOperations();
        List<BlokLiftMontaj> blokLiftList = productionProcess.getBlokLiftOperations();
        List<BoyaVePaketleme> boyaVePaketlemeList = productionProcess.getBoyaPaketOperations();
        List<KaliteKontrol> kaliteKontrolList = productionProcess.getKaliteKontrolOperations();

        String customerName = order.getCustomerName();
        Page<Order> orderPage = orderRepository.findAllByCustomerName(customerName, pageable);
        Page<OrderResponse> orderResponsePage = orderPage.map(orderMapper::mapOrderToOrderResponse);

        return Responses.<OrderResponse,
                ProductionProcessResponse,
                List<TalasliImalatResponse>,
                PolisajResponse,
                List<LiftResponse>,
                List<BlokLiftResponse>,
                List<BoyaVePaketlemeResponse>,
                List<KaliteKontrolResponse>,
                Page<OrderResponse>>builder()
                .returnBody1(orderMapper.mapOrderToOrderResponse(order))
                .returnBody2(productionProcessMapper.mapToResponse(productionProcess))
                .returnBody3(talasliMapper.mapTalasliListToResponse(talasliImalatList))
                .returnBody4(polisaj != null ? polisajMapper.mapToResponse(polisaj) : null)
                .returnBody5(liftMapper.mapLiftListToResponse(liftList))
                .returnBody6(blokLiftMapper.mapBlokLiftListToResponse(blokLiftList))
                .returnBody7(boyaVePaketlemeMapper.mapToBoyaVePaketlemeResponseList(boyaVePaketlemeList))
                .returnBody8(kaliteKontrolMapper.mapKaliteKontrolListToResponse(kaliteKontrolList))
                .returnBody9(orderResponsePage)
                .message("Sipariş işlemleri başarılı bir şekilde getirildi")
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
