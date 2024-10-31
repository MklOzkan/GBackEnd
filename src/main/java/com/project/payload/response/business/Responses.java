package com.project.payload.response.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Responses<A,B,T, S, V, X, W, Y, Z> {
    private A returnBody1;
    private B returnBody2;
    private T returnBody3;
    private S returnBody4;
    private V returnBody5;
    private X returnBody6;
    private W returnBody7;
    private Y returnBody8;
    private Z returnBody9;
    private String message;
    private HttpStatus httpStatus;
}
