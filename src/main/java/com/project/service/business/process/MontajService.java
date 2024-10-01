package com.project.service.business.process;

import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.business.process.MontajRequest;
import com.project.payload.response.business.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MontajService {
    public ResponseMessage<String> boruKapamaOperation(Long operationId, @Valid MontajRequest request) {

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.BORU_COMPLETED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<String> boruKaynakOperation(Long operationId, @Valid MontajRequest request) {

        return ResponseMessage.<String>builder()
                .message(SuccessMessages.BORU_COMPLETED)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
