package com.project.payload.response.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultipleResponses<T, S, V> {

    private T returnBody;
    private S returnBody2;
    private V returnBody3;
    private String message;
    private HttpStatus httpStatus;
}
