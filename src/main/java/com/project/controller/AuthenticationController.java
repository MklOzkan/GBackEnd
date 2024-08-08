package com.project.controller;

import com.project.payload.request.SignInRequest;
import com.project.payload.response.SignInResponse;
import com.project.payload.response.UserResponse;
import com.project.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/register") //http://localhost:8080/auth/register
    public ResponseEntity<SignInResponse> registerUser(@RequestBody @Valid SignInRequest signInRequest) {
        return authenticationService.registerUser(signInRequest);

    }




}
