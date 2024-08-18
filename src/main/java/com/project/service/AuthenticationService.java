package com.project.service;

import com.project.domain.concretes.user.User;
import com.project.domain.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.LoginRequest;
import com.project.payload.request.user.CreatePasswordRequest;
import com.project.payload.response.AuthenticatedUsersResponse;
import com.project.repository.user.UserRepository;
import com.project.security.jwt.JwtUtils;
import com.project.security.service.UserDetailsImpl;
import com.project.service.helper.MethodHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final MethodHelper methodHelper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;


    public ResponseEntity<AuthenticatedUsersResponse> authenticateUser(LoginRequest loginRequest){
        //login için gerekli olan email ve password LoginRequest classı üzerinden alınıyor.
        String email= loginRequest.getEmail();
        String password= loginRequest.getPassword();
        // authenticationManager user ı valide ediyor
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        // valide edilen user Security Contexte atılıyor. Bu, uygulamanın geri kalanında kimlik doğrulamanın geçerli olmasını sağlar.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // JWT token oluşturuluyor
        String token="Bearer " + jwtUtils.generateJwtToken(authentication);
        // authentication nesnesinden doğrulanan kullanıcının detayları alınır getPrincipal() ile. Bu, UserDetailsImpl sınıfına dönüştürülür.
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        //Kullanıcının sahip olduğu roller (GrantedAuthority nesneleri), String nesnelerine dönüştürülerek bir Set içine toplanır.
        Set<String> roles = userDetails.getAuthorities()
                .stream() // Sream<GrantedAuth>
                .map(GrantedAuthority::getAuthority) // Stream<String>
                .collect(Collectors.toSet());

        Optional<String> role = roles.stream().findFirst();

        AuthenticatedUsersResponse.AuthenticatedUsersResponseBuilder authResponse= AuthenticatedUsersResponse.builder();
        authResponse.id(userDetails.getId());
        authResponse.built_in(userDetails.getBuiltIn());
        authResponse.token(token.substring(7));


        return ResponseEntity.ok(authResponse.build());

    }








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
