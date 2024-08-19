package com.project.service.validator;


import com.project.exception.BadRequestException;
import com.project.payload.messages.ErrorMessages;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TimeValidator {

    public boolean checkTime(LocalDate orderDate, LocalDate deliveryDate) {
        return orderDate.isAfter(deliveryDate)||orderDate.equals(deliveryDate);
    }

    public void checkTimeWithException(LocalDate orderDate, LocalDate deliveryDate) {
        if (checkTime(orderDate, deliveryDate)) {
            throw new BadRequestException(ErrorMessages.TIME_NOT_VALID_MESSAGE);
        }
    }
}
