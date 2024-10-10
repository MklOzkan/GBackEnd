package com.project.service.business.process;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process.ProductionProcess;
import com.project.domain.concretes.business.process.boyavepaket.BoyaVePaketleme;
import com.project.domain.concretes.business.process.kalitekontrol.KaliteKontrol;
import com.project.domain.concretes.business.process.liftmontajamiri.LiftMontaj;
import com.project.domain.concretes.business.process.talasliimalatamiri.TalasliImalat;
import com.project.domain.enums.OrderType;
import com.project.payload.request.business.process.KaliteKontrolRequest;
import com.project.payload.response.business.MultipleResponses;
import com.project.payload.response.business.OrderResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.business.process.KaliteKontrolResponse;
import com.project.payload.response.business.process.ProductionProcessResponse;
import com.project.repository.business.process.KaliteKontrolRepository;
import com.project.service.helper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KaliteKontrolServiceTest {

    @Mock
    private KaliteKontrolRepository kaliteKontrolRepository;

    @Mock
    private KaliteKontrolHelper kaliteKontrolHelper;

    @Mock
    private TalasliHelper talasliHelper;

    @Mock
    private BoyaPaketHelper boyaPaketHelper;

    @Mock
    private PolisajHelper polisajHelper;

    @Mock
    private MontajHelper montajHelper;

    @InjectMocks
    private KaliteKontrolService kaliteKontrolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void afterMilTaslamaKaliteKontrol_withValidRequest_updatesKaliteKontrol() {
        KaliteKontrolRequest request = new KaliteKontrolRequest();
        request.setApproveCount(10);
        request.setScrapCount(2);
        request.setReturnedToIsilIslem(1);
        request.setReturnedToMilTaslama(1);

        KaliteKontrol kaliteKontrol = new KaliteKontrol();
        ProductionProcess productionProcess = new ProductionProcess();
        kaliteKontrol.setProductionProcess(productionProcess);

        when(kaliteKontrolHelper.findById(anyLong())).thenReturn(kaliteKontrol);
        when(talasliHelper.findTalasliImalatByProductionProcess(any(), any())).thenReturn(new TalasliImalat());

        ResponseMessage<String> response = kaliteKontrolService.afterMilTaslamaKaliteKontrol(request, 1L);

        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Kalite kontrol updated", response.getMessage());
    }

    @Test
    void afterMilTaslamaKaliteKontrol_withZeroApproveCount_doesNotUpdateNextOperation() {
        KaliteKontrolRequest request = new KaliteKontrolRequest();
        request.setApproveCount(0);
        request.setScrapCount(2);
        request.setReturnedToIsilIslem(1);
        request.setReturnedToMilTaslama(1);

        KaliteKontrol kaliteKontrol = new KaliteKontrol();
        ProductionProcess productionProcess = new ProductionProcess();
        kaliteKontrol.setProductionProcess(productionProcess);

        when(kaliteKontrolHelper.findById(anyLong())).thenReturn(kaliteKontrol);
        when(talasliHelper.findTalasliImalatByProductionProcess(any(), any())).thenReturn(new TalasliImalat());

        kaliteKontrolService.afterMilTaslamaKaliteKontrol(request, 1L);

        verify(talasliHelper, never()).saveTalasliImalatWithoutReturn(any());
    }

    @Test
    void afterEzmeKaliteKontrol_withValidRequest_updatesKaliteKontrol() {
        KaliteKontrolRequest request = new KaliteKontrolRequest();
        request.setApproveCount(10);
        request.setScrapCount(2);
        request.setReturnedToMilTaslama(1);

        KaliteKontrol kaliteKontrol = new KaliteKontrol();
        ProductionProcess productionProcess = new ProductionProcess();
        kaliteKontrol.setProductionProcess(productionProcess);

        when(kaliteKontrolHelper.findById(anyLong())).thenReturn(kaliteKontrol);
        when(montajHelper.findLiftByProductionProcessAndOperationType(any(), any())).thenReturn(new LiftMontaj());

        ResponseMessage<String> response = kaliteKontrolService.afterEzmeKaliteKontrol(request, 1L);

        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Kalite kontrol updated", response.getMessage());
    }

    @Test
    void afterMontajKaliteKontrol_withValidRequest_updatesKaliteKontrol() {
        KaliteKontrolRequest request = new KaliteKontrolRequest();
        request.setApproveCount(10);
        request.setScrapCount(2);
        request.setReturnedToIsilIslem(1);
        request.setReturnedToMilTaslama(1);

        KaliteKontrol kaliteKontrol = new KaliteKontrol();
        ProductionProcess productionProcess = new ProductionProcess();
        kaliteKontrol.setProductionProcess(productionProcess);

        when(kaliteKontrolHelper.findById(anyLong())).thenReturn(kaliteKontrol);
        //when(boyaPaketHelper.findBoyaVePaketlemeByProductionProcess(any(), any())).thenReturn(new BoyaVePaketleme());

        ResponseMessage<String> response = kaliteKontrolService.afterMontajKaliteKontrol(request, 1L);

        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Kalite kontrol updated", response.getMessage());
    }

    @Test
    void getKaliteKontrolStages_withValidId_returnsMultipleResponses() {
        ProductionProcess productionProcess = new ProductionProcess();
        Order order = new Order();
        order.setOrderType(OrderType.LIFT);
        productionProcess.setOrder(order);

        when(talasliHelper.findProductionProcessById(anyLong())).thenReturn(productionProcess);
        when(kaliteKontrolRepository.findAllByProductionProcess(any())).thenReturn(List.of(new KaliteKontrol()));

        MultipleResponses<OrderResponse, ProductionProcessResponse, List<KaliteKontrolResponse>> response = kaliteKontrolService.getKaliteKontrolStages(1L);

        assertNotNull(response);
        assertNotNull(response.getReturnBody());
    }
}