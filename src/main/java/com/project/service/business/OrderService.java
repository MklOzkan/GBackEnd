package com.project.service.business;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process._enums.*;
import com.project.domain.concretes.business.process.blokliftmontajamiri.BlokLiftMontaj;
import com.project.domain.concretes.business.process.boyavepaket.BoyaVePaketleme;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.domain.concretes.business.process.polisajamiri.PolisajImalat;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.domain.concretes.user.User;
import com.project.domain.enums.OrderType;
import com.project.domain.enums.StatusType;
import com.project.exception.BadRequestException;
import com.project.payload.mappers.*;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.OrderRequest;
import com.project.payload.request.business.UpdateOrderRequest;
import com.project.payload.response.business.MultipleResponses;
import com.project.payload.response.business.OrderResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.process.*;
import com.project.repository.business.OrderRepository;
import com.project.repository.business.process.*;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import com.project.service.helper.TalasliHelper;
import com.project.service.validator.TimeValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TimeValidator timeValidator;
    private final MethodHelper methodHelper;
    private final PageableHelper pageableHelper;
    private final OrderStatusService orderStatusService;
    private final TalasliImalatRepository talasliImalatRepository;
    private final PolisajImalatRepository polisajImalatRepository;
    private final LiftMontajRepository liftMontajRepository;
    private final BlokLiftMontajRepository blokLiftMontajRepository;
    private final KaliteKontrolRepository kaliteKontrolRepository;
    private final BoyaVePaketlemeRepository boyaVePaketlemeRepository;
    private final ProductionProcessRepository productionProcessRepository;
    private final TalasliHelper talasliHelper;
    private final TalasliMapper talasliMapper;
    private final PolisajMapper polisajMapper;
    private final LiftMapper liftMapper;
    private final BlokLiftMapper blokLiftMapper;
    private final BoyaVePaketMapper boyaVePaketMapper;
    //private final ResponseHelper responseHelper;


    public ResponseMessage<OrderResponse> createOrder(OrderRequest orderRequest, HttpServletRequest request) {
        User user = methodHelper.checkUser(request);
        checkUserName(user);//kullanıcı adı kontrolü yapıyoruz

        methodHelper.checkGasanNo(orderRequest.getGasanNo());//gazan numarası kontrolü yapıyoruz

        methodHelper.checkOrderNumber(orderRequest.getOrderNumber());//sipariş numarası kontrolü yapıyoruz

        timeValidator.checkTimeWithException(LocalDate.now(), orderRequest.getDeliveryDate());//teslim tarihi bugünden küçük olamaz




        Order orderConfirmToSave = orderMapper.mapOrderConfirmRequestToOrderConfirm(orderRequest);
        Order savedOrder = orderRepository.save(orderConfirmToSave);

        ProductionProcess productionProcess = new ProductionProcess();
        productionProcess.setOrder(savedOrder);
        productionProcess.setRemainingQuantity(savedOrder.getOrderQuantity());
        productionProcess = productionProcessRepository.save(productionProcess);

        savedOrder.setProductionProcess(productionProcess);
        orderRepository.save(savedOrder);

        TalasliImalat boruKesme = new TalasliImalat();
        boruKesme.setRemainingQuantity(savedOrder.getOrderQuantity());
        createTalasli(boruKesme, productionProcess, TalasliOperationType.BORU_KESME_HAVSA);

        TalasliImalat milKoparma = new TalasliImalat();
        TalasliImalat milTornalama = new TalasliImalat();

        if (!savedOrder.getOrderType().equals(OrderType.BLOKLIFT)) {
            milKoparma.setRemainingQuantity(savedOrder.getOrderQuantity() - savedOrder.getReadyMilCount());
            createTalasli(milKoparma, productionProcess, TalasliOperationType.MIL_KOPARMA);
            createTalasli(milTornalama, productionProcess, TalasliOperationType.MIL_TORNALAMA);
        }

        TalasliImalat milTaslama = new TalasliImalat();

        if (savedOrder.getOrderType().equals(OrderType.BLOKLIFT)) {
            milTaslama.setRemainingQuantity(savedOrder.getOrderQuantity());
        }
        createTalasli(milTaslama, productionProcess, TalasliOperationType.MIL_TASLAMA);

        TalasliImalat isilIslem = new TalasliImalat();
        if (!savedOrder.getOrderType().equals(OrderType.PASLANMAZ)) {
            createTalasli(isilIslem, productionProcess, TalasliOperationType.ISIL_ISLEM);
        }

        TalasliImalat ezme = new TalasliImalat();
        if (savedOrder.getOrderType().equals(OrderType.PASLANMAZ)) {
            createTalasli(ezme, productionProcess, TalasliOperationType.EZME);
        }

        PolisajImalat polisaj = new PolisajImalat();
        if (!savedOrder.getOrderType().equals(OrderType.PASLANMAZ)){
            createPolisaj(polisaj, productionProcess);
        }


        LiftMontaj boruKapama = new LiftMontaj();
        LiftMontaj boruKaynak = new LiftMontaj();
        LiftMontaj liftMontaj = new LiftMontaj();
        LiftMontaj gazDolum = new LiftMontaj();
        LiftMontaj baslikTakma = new LiftMontaj();

        BlokLiftMontaj boruKapamaBlok = new BlokLiftMontaj();
        BlokLiftMontaj boruKaynakBlok = new BlokLiftMontaj();
        BlokLiftMontaj liftMontajBlok = new BlokLiftMontaj();
        BlokLiftMontaj gazDolumBlok = new BlokLiftMontaj();
        BlokLiftMontaj test = new BlokLiftMontaj();

        if (savedOrder.getOrderType().equals(OrderType.LIFT)||savedOrder.getOrderType().equals(OrderType.PASLANMAZ)) {
            createLiftMontaj(boruKapama, productionProcess, LiftMontajOperationTye.BORU_KAPAMA);
            createLiftMontaj(boruKaynak, productionProcess, LiftMontajOperationTye.BORU_KAYNAK);

            if(savedOrder.getOrderType().equals(OrderType.LIFT)&&savedOrder.getReadyMilCount()>=0){
                liftMontaj.setMilCount(savedOrder.getReadyMilCount());
            }
            createLiftMontaj(liftMontaj, productionProcess, LiftMontajOperationTye.LIFT_MONTAJ);
            createLiftMontaj(gazDolum, productionProcess, LiftMontajOperationTye.GAZ_DOLUM);
            createLiftMontaj(baslikTakma, productionProcess, LiftMontajOperationTye.BASLIK_TAKMA);
        }else {

            createBlokLiftMontaj(boruKapamaBlok, productionProcess, BlokLiftOperationType.BORU_KAPAMA);
            if (savedOrder.getOrderType().equals(OrderType.DAMPER)) {
                createBlokLiftMontaj(boruKaynakBlok, productionProcess, BlokLiftOperationType.BORU_KAYNAK);
            }
            createBlokLiftMontaj(liftMontajBlok, productionProcess, BlokLiftOperationType.BLOK_LIFT_MONTAJ);
            createBlokLiftMontaj(gazDolumBlok, productionProcess, BlokLiftOperationType.GAZ_DOLUM);
            createBlokLiftMontaj(test, productionProcess, BlokLiftOperationType.TEST);
        }

        BoyaVePaketleme boya = new BoyaVePaketleme();
        createBoyavePAketleme(boya, productionProcess, BoyaPaketOperationType.BOYA);

        BoyaVePaketleme paketleme = new BoyaVePaketleme();
        createBoyavePAketleme(paketleme, productionProcess, BoyaPaketOperationType.PAKETLEME);

        KaliteKontrol afterPolisaj = new KaliteKontrol();
        KaliteKontrol afterMontaj = new KaliteKontrol();
        KaliteKontrol afterMilTaslama = new KaliteKontrol();
        KaliteKontrol afterEzme = new KaliteKontrol();
        if (savedOrder.getOrderType().equals(OrderType.LIFT)||savedOrder.getOrderType().equals(OrderType.DAMPER)){
            createKaliteKontrol(afterPolisaj, productionProcess, KaliteKontrolStage.AFTER_POLISAJ);
            createKaliteKontrol(afterMontaj, productionProcess, KaliteKontrolStage.AFTER_MONTAJ);
        } else if (savedOrder.getOrderType().equals(OrderType.PASLANMAZ)) {
            createKaliteKontrol(afterPolisaj, productionProcess, KaliteKontrolStage.AFTER_POLISAJ);
            createKaliteKontrol(afterMilTaslama, productionProcess, KaliteKontrolStage.AFTER_MIL_TASLAMA);
            createKaliteKontrol(afterEzme, productionProcess, KaliteKontrolStage.AFTER_EZME);
        }else {
            createKaliteKontrol(afterPolisaj, productionProcess, KaliteKontrolStage.AFTER_POLISAJ);
        }

        return methodHelper.createResponse(SuccessMessages.ORDER_CREATED, HttpStatus.CREATED, orderMapper.mapOrderToOrderResponse(savedOrder));
    }

    public void createTalasli(TalasliImalat talasliImalat, ProductionProcess productionProcess, TalasliOperationType operationType) {
        talasliImalat.setOperationType(operationType);
        talasliImalat.setProductionProcess(productionProcess);
        talasliImalatRepository.save(talasliImalat);
    }

    public void createPolisaj(PolisajImalat polisajImalat, ProductionProcess productionProcess) {
        polisajImalat.setProductionProcess(productionProcess);
        polisajImalatRepository.save(polisajImalat);
    }

    public void createLiftMontaj(LiftMontaj liftMontaj, ProductionProcess productionProcess, LiftMontajOperationTye operationType) {
        liftMontaj.setOperationType(operationType);
        liftMontaj.setProductionProcess(productionProcess);
        liftMontajRepository.save(liftMontaj);
    }

    public void createBlokLiftMontaj(BlokLiftMontaj blokLiftMontaj, ProductionProcess productionProcess, BlokLiftOperationType operationType) {
        blokLiftMontaj.setOperationType(operationType);
        blokLiftMontaj.setProductionProcess(productionProcess);
        blokLiftMontajRepository.save(blokLiftMontaj);
    }

    public void createKaliteKontrol(KaliteKontrol kaliteKontrol, ProductionProcess productionProcess, KaliteKontrolStage kaliteKontrolStage) {
        kaliteKontrol.setKaliteKontrolStage(kaliteKontrolStage);
        kaliteKontrol.setProductionProcess(productionProcess);
        kaliteKontrolRepository.save(kaliteKontrol);
    }

    public void createBoyavePAketleme(BoyaVePaketleme boyaVePaketleme, ProductionProcess productionProcess, BoyaPaketOperationType operationType) {
        boyaVePaketleme.setOperationType(operationType);
        boyaVePaketleme.setProductionProcess(productionProcess);
        boyaVePaketlemeRepository.save(boyaVePaketleme);
    }


    public ResponseMessage<OrderResponse> updateOrder(UpdateOrderRequest orderRequest, Long id, HttpServletRequest request) {
        methodHelper.checkUser(request);

        Order order = methodHelper.findOrderById(id);
        timeValidator.checkTimeWithException(LocalDate.now(), orderRequest.getDeliveryDate());

        Order updatedOrder = orderMapper.updateOrderFromRequest(orderRequest, order);

        orderRepository.save(updatedOrder);
        updateTalasliOperations(updatedOrder);
        OrderResponse orderResponse = orderMapper.mapOrderToOrderResponse(updatedOrder);

        return methodHelper.createResponse(SuccessMessages.ORDER_UPDATED, HttpStatus.OK, orderResponse);
    }

    public void updateTalasliOperations(Order order){
        ProductionProcess productionProcess = order.getProductionProcess();
        TalasliImalat boruKesme = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.BORU_KESME_HAVSA);
        TalasliImalat milKoparma;
        TalasliImalat milTaslama;
        talasliHelper.updateOperation(boruKesme, order);
        if (order.getOrderType().equals(OrderType.BLOKLIFT)) {
            milTaslama = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_TASLAMA);
            talasliHelper.updateOperation(milTaslama, order);
        } else {
            milKoparma = talasliHelper.findTalasliImalatByProductionProcess(productionProcess, TalasliOperationType.MIL_KOPARMA);
            talasliHelper.updateOperation(milKoparma, order);
        }
    }

    private void checkUserName(User user) {
        if (!user.getUsername().equals("UretimPlanlama")) {
            throw new BadRequestException(ErrorMessages.UNAUTHORIZED_USER);
        }
    }


    public OrderResponse getByOrderNumber(String orderNumber) {
        Order order = methodHelper.findOrderByOrderNumber(orderNumber);
        return orderMapper.mapOrderToOrderResponse(order);
    }

    public Page<OrderResponse> getAllOrders(int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(orderMapper::mapOrderToOrderResponse);
    }

    public List<OrderResponse> getOrders() {
        return orderRepository
                .findAll()
                .stream()
                .map(orderMapper::mapOrderToOrderResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<String> deleteOrder(String orderNumber, HttpServletRequest request) {
        methodHelper.checkUser(request);

        Order order = methodHelper.findOrderByOrderNumber(orderNumber);
        orderRepository.delete(order);
        return methodHelper.createResponse(SuccessMessages.ORDER_DELETED, HttpStatus.OK, orderNumber);
    }

    public Page<OrderResponse> getAllOrdersForSupervisor(
            HttpServletRequest request, int page, int size, String sort, String type) {
        methodHelper.checkUser(request);
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        List<String> statuses = List.of(StatusType.ISLENMEYI_BEKLIYOR.getName(), StatusType.ISLENMEKTE.getName(), StatusType.BEKLEMEDE.getName());
        Page<Order> ordersPage = orderRepository.findByOrderStatus_StatusNameIn(statuses, pageable);

        System.out.println("Total Pages: " + ordersPage.getTotalPages());
        System.out.println("Current Page: " + ordersPage.getNumber());
        System.out.println("Total Elements: " + ordersPage.getTotalElements());


        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(orderMapper::mapOrderToOrderResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());

    }

    public Page<OrderResponse> getAllOrdersForOtherSuperVisor(
            HttpServletRequest request, int page, int size, String sort, String type) {
        methodHelper.checkUser(request);
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        String status = StatusType.ISLENMEKTE.getName();
        Page<Order> ordersPage = orderRepository.findByOrderStatus_StatusName(status, pageable);

        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(orderMapper::mapOrderToOrderResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());

    }

    public Page<OrderResponse> filterOrdersByStatusAndDate(
            List<String> statuses, String startDateStr, String endDateStr, int page, int size, String sort, String type) {

        // Parse dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        Page<Order> ordersPage = orderRepository.findByStatusTypeAndOrderDateBetween(statuses, startDate, endDate, pageable);

        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(orderMapper::mapOrderToOrderResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());
    }

    public MultipleResponses<OrderResponse, List<TalasliImalatResponse>, ProductionProcessResponse> getOrderById(Long id, HttpServletRequest request) {
        methodHelper.checkUser(request);

        Order order = methodHelper.findOrderById(id);
        ProductionProcess productionProcess = talasliHelper.findProductionProcessById(order.getProductionProcess().getId());
        List<TalasliImalat> talasliOperations = talasliHelper.talasliOperations(productionProcess);

        return methodHelper.multipleResponse(
                SuccessMessages.ORDER_FOUND,
                HttpStatus.OK,
                orderMapper.mapOrderToOrderResponse(order),
                talasliMapper.mapTalasliListToResponse(talasliOperations),
                talasliMapper.mapProductionProcessToResponse(productionProcess));
    }


    public void save(Order order) {
        orderRepository.save(order);
    }

    public ResponseMessage<OrderResponse> getOrder(Long id, HttpServletRequest request) {
        methodHelper.checkUser(request);

        Order order = methodHelper.findOrderById(id);
        return methodHelper.createResponse(SuccessMessages.ORDER_FOUND, HttpStatus.OK, orderMapper.mapOrderToOrderResponse(order));
    }

    public Page<OrderResponse> getOrdersWhichStatusIslenmekteAndBeklemede(HttpServletRequest request, @Min(0) int page, @Min(1) int size, String sort, String type) {
        methodHelper.checkUser(request);
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        List<String> statuses = List.of(StatusType.ISLENMEKTE.getName(), StatusType.BEKLEMEDE.getName());
        Page<Order> ordersPage = orderRepository.findByOrderStatus_StatusNameIn(statuses, pageable);

        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(orderMapper::mapOrderToOrderResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());
    }

    public Page<OrderResponse> getOrdersForPolisajAmir(HttpServletRequest request, @Min(0) int page, @Min(1) int size, String sort, String type) {

        methodHelper.checkUser(request);
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        List<String> statuses = List.of(StatusType.ISLENMEKTE.getName(), StatusType.BEKLEMEDE.getName(), StatusType.ISLENMEYI_BEKLIYOR.getName());
        Page<Order> ordersPage = orderRepository.findByStatusTypeAndOrderTypeNotLike(statuses, pageable);

        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(orderMapper::mapOrderToOrderResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());
    }

    public MultipleResponses<OrderResponse, PolisajResponse, ProductionProcessResponse> getMultipleResponseByIdForPolisaj(Long id, HttpServletRequest request) {
        methodHelper.checkUser(request);

        Order order = methodHelper.findOrderById(id);
        ProductionProcess productionProcess = order.getProductionProcess();
        PolisajImalat polisajImalat = productionProcess.getPolisajOperation();
        return methodHelper.multipleResponse(
                SuccessMessages.ORDER_FOUND,
                HttpStatus.OK,
                orderMapper.mapOrderToOrderResponse(order),
                polisajMapper.mapToResponse(polisajImalat),
                talasliMapper.mapProductionProcessToResponse(productionProcess));
    }

    public Page<OrderResponse> getOrdersForBLMontajAmiri(HttpServletRequest request, @Min(0) int page, @Min(1) int size, String sort, String type) {
        methodHelper.checkUser(request);

        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        List<String> statuses = List.of(StatusType.ISLENMEKTE.getName(), StatusType.BEKLEMEDE.getName());
        List<OrderType> orderTypes = List.of(OrderType.BLOKLIFT, OrderType.DAMPER);
        Page<Order> ordersPage = orderRepository.findByOrderStatus_StatusNameInAndOrderTypeIn(statuses, orderTypes, pageable);

        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(orderMapper::mapOrderToOrderResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());
    }

    public Page<OrderResponse> getOrdersForLiftMontajAmiri(HttpServletRequest request, @Min(0) int page, @Min(1) int size, String sort, String type) {
        methodHelper.checkUser(request);

        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        List<String> statuses = List.of(StatusType.ISLENMEKTE.getName(), StatusType.BEKLEMEDE.getName());
        List<OrderType> orderTypes = List.of(OrderType.LIFT, OrderType.PASLANMAZ );
        Page<Order> ordersPage = orderRepository.findByOrderStatus_StatusNameInAndOrderTypeIn(statuses, orderTypes, pageable);

        List<OrderResponse> orderResponses = ordersPage.getContent().stream()
                .map(orderMapper::mapOrderToOrderResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());
    }

    public MultipleResponses<OrderResponse, List<LiftResponse>, ProductionProcessResponse> getOrderByIdForLiftMontaj(Long id, HttpServletRequest request) {
        methodHelper.checkUser(request);

        Order order = methodHelper.findOrderById(id);
        ProductionProcess productionProcess = order.getProductionProcess();
        List<LiftMontaj> liftMontajList = productionProcess.getLiftOperations();

        return methodHelper.multipleResponse(
                SuccessMessages.ORDER_FOUND,
                HttpStatus.OK,
                orderMapper.mapOrderToOrderResponse(order),
                liftMapper.mapLiftListToResponse(liftMontajList),
                talasliMapper.mapProductionProcessToResponse(productionProcess));
    }

    public MultipleResponses<OrderResponse, List<BlokLiftResponse>, ProductionProcessResponse> getOrderByIdForBlokLiftMontaj(Long id, HttpServletRequest request) {
        methodHelper.checkUser(request);

        Order order = methodHelper.findOrderById(id);
        ProductionProcess productionProcess = order.getProductionProcess();
        List<BlokLiftMontaj> blokLiftMontajList = productionProcess.getBlokLiftOperations();

        return methodHelper.multipleResponse(
                SuccessMessages.ORDER_FOUND,
                HttpStatus.OK,
                orderMapper.mapOrderToOrderResponse(order),
                blokLiftMapper.mapBlokLiftListToResponse(blokLiftMontajList),
                talasliMapper.mapProductionProcessToResponse(productionProcess));
    }

    public MultipleResponses<OrderResponse, List<BoyaVePaketlemeResponse>, ProductionProcessResponse> getOrderByIdForBoyaPaket(Long id, HttpServletRequest request) {
        methodHelper.checkUser(request);

        Order order = methodHelper.findOrderById(id);
        ProductionProcess productionProcess = order.getProductionProcess();
        List<BoyaVePaketleme> boyaVePaketlemeList = productionProcess.getBoyaPaketOperations();

        return methodHelper.multipleResponse(
                SuccessMessages.ORDER_FOUND,
                HttpStatus.OK,
                orderMapper.mapOrderToOrderResponse(order),
                boyaVePaketMapper.mapToBoyaVePaketlemeResponseList(boyaVePaketlemeList),
                talasliMapper.mapProductionProcessToResponse(productionProcess));
    }

    public ResponseMessage<String> finishOrder(Long id, HttpServletRequest request) {
        methodHelper.checkUser(request);

        Order order = methodHelper.findOrderById(id);
        ProductionProcess productionProcess = order.getProductionProcess();
        if (order.getFinalProductQuantity()>=order.getOrderQuantity()){
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.TAMAMLANDI));
            productionProcess.endOperation();
            productionProcessRepository.save(productionProcess);
            orderRepository.save(order);
        }else {
            throw new BadRequestException(ErrorMessages.ORDER_CANNOT_FINISHED);
        }

        return methodHelper.createResponse(SuccessMessages.ORDER_FINISHED, HttpStatus.OK, null);
    }

    public ResponseMessage<String> startStop(Long id) {
        System.out.println("Accessing startStop for order: " + id);
        Order order = methodHelper.findOrderById(id);
        ProductionProcess productionProcess = order.getProductionProcess();
        if (order.getOrderStatus().getStatusType().getName().equals(StatusType.ISLENMEYI_BEKLIYOR.getName())) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.ISLENMEKTE));
            productionProcess.startOperation();
        }else if (order.getOrderStatus().getStatusType().getName().equals(StatusType.ISLENMEKTE.getName())) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.BEKLEMEDE));
        } else if (order.getOrderStatus().getStatusType().getName().equals(StatusType.BEKLEMEDE.getName())) {
            order.setOrderStatus(orderStatusService.getOrderStatus(StatusType.ISLENMEKTE));
        }
        productionProcessRepository.save(productionProcess);
        orderRepository.save(order);
        return methodHelper.createResponse(SuccessMessages.ORDER_STATUS_CHANGED, HttpStatus.OK, null);
    }
}
