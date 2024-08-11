package com.project.service;

import com.project.domain.concretes.user.User;
import com.project.payload.mappers.AuthenticationMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.SignUpRequest;
import com.project.payload.request.user.CodeRequest;
import com.project.payload.response.SignInResponse;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final MethodHelper methodHelper;
    private final AuthenticationMapper authenticationMapper;
    //TODO PasswordEncoder

    public ResponseEntity<SignInResponse> registerUser(SignUpRequest signInRequest) {

        methodHelper.checkDuplicate(signInRequest.getEmail());
        User registeredUser = authenticationMapper.SignInRequestToUser(signInRequest);
        //TODO PasswordEncoder
        //TODO Rol bilgisi

        User savedUser = userRepository.save(registeredUser);

        return new ResponseEntity<>(authenticationMapper.UserToSignInResponse(savedUser), HttpStatus.CREATED);

    }

    public ResponseEntity<String> resetPassword(CodeRequest request) {

        User user = userRepository.findByResetPasswordCode(request.getCode()).orElseThrow(() ->
                new IllegalArgumentException(String.format(ErrorMessages.RESET_CODE_IS_NOT_FOUND, request.getCode())));


     //   String requestPassword = passwordEncoder.encode(request.getPassword());
     //   user.setPassword(requestPassword);
        user.setResetCode(null);
        userRepository.save(user);

        return new ResponseEntity<>(SuccessMessages.PASSWORD_RESET_SUCCESSFULLY, HttpStatus.OK);

    }
}
