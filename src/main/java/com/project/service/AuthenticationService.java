package com.project.service;

import com.project.domain.concretes.user.User;
import com.project.payload.mappers.AuthenticationMapper;
import com.project.payload.request.SignInRequest;
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

    public ResponseEntity<SignInResponse> registerUser(SignInRequest signInRequest) {

        methodHelper.checkDuplicate(signInRequest.getEmail());
        User registeredUser = authenticationMapper.SignInRequestToUser(signInRequest);
        //TODO PasswordEncoder
        //TODO Rol bilgisi

        User savedUser = userRepository.save(registeredUser);

        return new ResponseEntity<>(authenticationMapper.UserToSignInResponse(savedUser), HttpStatus.CREATED);

    }
}
