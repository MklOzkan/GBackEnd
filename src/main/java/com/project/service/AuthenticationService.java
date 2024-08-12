package com.project.service;

import com.project.domain.concretes.user.User;
import com.project.exception.BadRequestException;
import com.project.exception.ConflictException;
import com.project.payload.mappers.AuthenticationMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.SignUpRequest;
import com.project.payload.request.user.CodeRequest;
import com.project.payload.request.user.ForgetPasswordRequest;
import com.project.payload.response.SignInResponse;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.user.EmailService;
import com.project.utils.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final MethodHelper methodHelper;
    private final AuthenticationMapper authenticationMapper;
    private final EmailService emailService;
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

    public String forgotPassword(ForgetPasswordRequest request) {

        String resetCode;
        try {
            User user = methodHelper.findByUserByEmail(request.getEmail());
            resetCode = UUID.randomUUID().toString();
            if(userRepository.existsByResetPasswordCode(resetCode)) throw new ConflictException("The code has already taken");
            user.setResetCode(resetCode);
            userRepository.save(user);
            MimeMessagePreparator resetPasswordEmail = MailUtil.buildResetPasswordEmail(user.getEmail(),resetCode , user.getFirstName() );
            emailService.sendEmail(resetPasswordEmail);


        } catch (BadRequestException e) {
            return ErrorMessages.THERE_IS_NO_USER_REGISTERED_WITH_THIS_EMAIL_ADRESS;
        }

        return "Code has been sent";

    }
}
