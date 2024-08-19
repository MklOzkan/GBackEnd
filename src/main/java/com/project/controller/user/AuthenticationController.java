package com.project.controller.user;

import com.project.payload.request.authentication.LoginRequest;
import com.project.payload.request.user.CreatePasswordRequest;
import com.project.payload.response.authentication.AuthResponse;
import com.project.service.user.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse>authenticateUser(
            @RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authenticationService.authenticateUser(request));
    }




//    @PostMapping("/createPassword")
//    public ResponseEntity<String>createPassword(@Valid @RequestBody CreatePasswordRequest createPasswordRequest, HttpServletRequest request){
//        return authenticationService.createPassword(createPasswordRequest,request);
//    }

//    @PostMapping("/login")
//    public ResponseEntity<AuthenticatedUsersResponse>authenticateUser(
//            @RequestBody @Valid LoginRequest request) {
//        return ResponseEntity.ok(authenticationService.authenticateUser(request));
//    }


//    @PostMapping("/forgot-password") //http://localhost:8080/auth/forgot-password
//    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgetPasswordRequest request){
//
//        return ResponseEntity.ok(authenticationService.forgotPassword(request));
//    }






}
