package com.project.service;

import com.project.domain.concretes.user.User;
import com.project.domain.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.exception.ConflictException;
import com.project.payload.mappers.AuthenticationMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.SignUpRequest;
import com.project.payload.request.user.CodeRequest;
import com.project.payload.request.user.CreatePasswordRequest;
import com.project.payload.request.user.ForgetPasswordRequest;
import com.project.payload.response.SignInResponse;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.user.EmailService;
import com.project.utils.MailUtil;
import jakarta.servlet.http.HttpServletRequest;
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

    public ResponseEntity<String> createPassword(CreatePasswordRequest createPasswordRequest, HttpServletRequest request) {
        methodHelper.getUserByHttpRequest(request);

        switch (RoleType.valueOf(createPasswordRequest.getRoleName().toUpperCase())) {

            case TALASLI_IMALAT_AMIRI:
                methodHelper.getUserByHttpRequest(request);
                break;

            case POLISAJ_AMIRI:

                break;

            case LIFT_MONTAJ_AMIRI:

                break;

            case KALITE_KONTROL:

                break;

            case BL_MONTAJ_AMIRI:

                break;

            case URUETIM_PLANLAMA:

                break;

            case BOYAMA_VE_PAKETLEME_AMIRI:

                break;

            default:
                throw new IllegalArgumentException("Geçersiz rol adı: " + createPasswordRequest.getRoleName());
        }

    }





}
