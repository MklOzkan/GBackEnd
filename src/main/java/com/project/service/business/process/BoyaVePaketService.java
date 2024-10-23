package com.project.service.business.process;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.business.process._enums.BoyaPaketOperationType;
import com.project.domain.concretes.business.process.boyavepaket.BoyaVePaketleme;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.process.BoyaVePaketlemeRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.helper.BoyaPaketHelper;
import com.project.service.helper.MethodHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoyaVePaketService {
    private final BoyaPaketHelper boyaPaketHelper;
    private final MethodHelper methodHelper;

    @Transactional
    public ResponseMessage<String> boyaOperation(Long operationId, @Valid BoyaVePaketlemeRequest request) {
        BoyaVePaketleme boya = boyaPaketHelper.findBoyaVePaketlemeByOperationId(operationId);
        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(), boya.getRemainingQuantity());
        boya.completeOperation(request.getCompletedQuantity());
        boyaPaketHelper.saveBoyaVePaketlemeWithoutReturn(boya);

        BoyaVePaketleme nextOperation = boyaPaketHelper.findBoyaVePaketlemeByProductionProcessAndOperationType(boya.getProductionProcess(), BoyaPaketOperationType.PAKETLEME);
        nextOperation.updateNextOperation(boya.getLastCompletedQty());
        boyaPaketHelper.saveBoyaVePaketlemeWithoutReturn(nextOperation);

        return methodHelper.createResponse(String.format(SuccessMessages.BOYA_UPDATED, boya.getLastCompletedQty()), HttpStatus.OK,null);
    }

    @Transactional
    public ResponseMessage<String> paketOperation(Long operationId, @Valid BoyaVePaketlemeRequest request) {
        BoyaVePaketleme paket = boyaPaketHelper.findBoyaVePaketlemeByOperationId(operationId);
        methodHelper.compareCompletedQuantityWithRemainingQuantity(request.getCompletedQuantity(), paket.getRemainingQuantity());
        paket.completeOperation(request.getCompletedQuantity());
        boyaPaketHelper.saveBoyaVePaketlemeWithoutReturn(paket);

        Order finalizedOrder = paket.getProductionProcess().getOrder();
        finalizedOrder.setFinalProductQuantity(paket.getCompletedQuantity());
        methodHelper.saveOrderWithoutReturn(finalizedOrder);

        return methodHelper.createResponse(String.format(SuccessMessages.PAKET_UPDATED, paket.getLastCompletedQty()), HttpStatus.OK,null);
    }

    public ResponseMessage<String> rollbackOperation(Long operationId) {
        BoyaVePaketleme operation = boyaPaketHelper.findBoyaVePaketlemeByOperationId(operationId);
        int lastCompletedQty = operation.getLastCompletedQty();
        if (operation.getOperationType().equals(BoyaPaketOperationType.BOYA)) {
            BoyaVePaketleme nextOperation = boyaPaketHelper.findBoyaVePaketlemeByProductionProcessAndOperationType(operation.getProductionProcess(), BoyaPaketOperationType.PAKETLEME);
            nextOperation.removeLastFromNextOperation(lastCompletedQty);
            boyaPaketHelper.saveBoyaVePaketlemeWithoutReturn(nextOperation);
        } else {
            Order finalizedOrder = operation.getProductionProcess().getOrder();
            finalizedOrder.setFinalProductQuantity(finalizedOrder.getFinalProductQuantity() - lastCompletedQty);
            methodHelper.saveOrderWithoutReturn(finalizedOrder);
        }
        operation.removeLastCompletedQty();
        boyaPaketHelper.saveBoyaVePaketlemeWithoutReturn(operation);

        return methodHelper.createResponse(String.format(SuccessMessages.OPERATION_ROLLEDBACK, lastCompletedQty), HttpStatus.OK,null);
    }
}
