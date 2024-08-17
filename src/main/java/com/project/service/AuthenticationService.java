package com.project.service;

import com.project.domain.concretes.user.User;
import com.project.domain.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.user.CreatePasswordRequest;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final MethodHelper methodHelper;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<String> createPassword(CreatePasswordRequest createPasswordRequest, HttpServletRequest request) {
        methodHelper.getUserByHttpRequest(request);


        if (isPasswordExists(createPasswordRequest.getPassword())) {
            throw new BadRequestException(ErrorMessages.PASSWORD_HAS_ALREADY_TAKEN);
        }

        RoleType roleType = RoleType.valueOf(createPasswordRequest.getRoleName().toUpperCase());

        switch (roleType) {

            case TALASLI_IMALAT_AMIRI:
                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
                break;
            case POLISAJ_AMIRI:
                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
                break;
            case LIFT_MONTAJ_AMIRI:
                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
                break;
            case KALITE_KONTROL:
                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
                break;
            case BL_MONTAJ_AMIRI:
                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
                break;
            case URETIM_PLANLAMA_AMIRI:
                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
                break;
            case BOYAMA_VE_PAKETLEME_AMIRI:
                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
                break;
            default:
                throw new IllegalArgumentException("Geçersiz rol adı: " + createPasswordRequest.getRoleName());
        }

        return ResponseEntity.ok("Şifre güncellendi.");

    }

    private void assignPasswordToUsersByRole(RoleType roleType, String password) {

            User fetchUser = userRepository.findByUserRoleRoleType(roleType).orElseThrow(()->new BadRequestException(ErrorMessages.USER_NOT_FOUND));
            fetchUser.setPassword(passwordEncoder.encode(password));
            userRepository.save(fetchUser);

    }

    public boolean isPasswordExists(String password) {
        return userRepository.findByPassword(password).isPresent();//password kullaniliksa isPresent true doner kullanmayiksak false doner.
    }






}
