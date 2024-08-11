package com.project.controller;

import com.project.payload.request.SignUpRequest;
import com.project.payload.request.user.CodeRequest;
import com.project.payload.response.SignInResponse;
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
    public ResponseEntity<SignInResponse> registerUser(@RequestBody @Valid SignUpRequest signInRequest) {
        return authenticationService.registerUser(signInRequest);

    }


    @PostMapping("/reset-password") //http://localhost:8080/auth/reset-password
    ResponseEntity<String>resetPassword(@Valid @RequestBody CodeRequest request){
        return authenticationService.resetPassword(request);
    }


}
