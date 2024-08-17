package com.project.controller;

import com.project.payload.request.SignUpRequest;
import com.project.payload.request.user.CodeRequest;
import com.project.payload.request.user.CreatePasswordRequest;
import com.project.payload.request.user.ForgetPasswordRequest;
import com.project.payload.response.SignInResponse;
import com.project.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;





    @PostMapping("/createPassword")
    public ResponseEntity<String>createPassword(@Valid @RequestBody CreatePasswordRequest createPasswordRequest, HttpServletRequest request){
        return authenticationService.createPassword(createPasswordRequest,request);
    }


//    @PostMapping("/forgot-password") //http://localhost:8080/auth/forgot-password
//    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgetPasswordRequest request){
//
//        return ResponseEntity.ok(authenticationService.forgotPassword(request));
//    }






}
