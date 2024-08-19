package com.project.service.user;

import com.project.domain.concretes.user.User;
import com.project.domain.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.authentication.LoginRequest;
import com.project.payload.response.AuthenticatedUsersResponse;
import com.project.payload.response.authentication.AuthResponse;
import com.project.repository.user.UserRepository;
import com.project.security.jwt.JwtUtils;
import com.project.security.service.UserDetailsImpl;
import com.project.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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


    public AuthResponse authenticateUser(LoginRequest loginRequest){
        //login için gerekli olan email ve password LoginRequest classı üzerinden alınıyor.
        String username = loginRequest.getUsername();
        String password= loginRequest.getPassword();
        // authenticationManager user ı valide ediyor
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        // valide edilen user Security Contexte atılıyor. Bu, uygulamanın geri kalanında kimlik doğrulamanın geçerli olmasını sağlar.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // JWT token oluşturuluyor
        String token= jwtUtils.generateJwtToken(authentication);
        // authentication nesnesinden doğrulanan kullanıcının detayları alınır getPrincipal() ile. Bu, UserDetailsImpl sınıfına dönüştürülür.
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        //Kullanıcının sahip olduğu roller (GrantedAuthority nesneleri), String nesnelerine dönüştürülerek bir Set içine toplanır.
        Set<String> roles = userDetails.getAuthorities()
                .stream() // Sream<GrantedAuth>
                .map(GrantedAuthority::getAuthority) // Stream<String>
                .collect(Collectors.toSet());

        String role = roles.stream().findFirst().get();

        AuthResponse.AuthResponseBuilder responseBuilder= AuthResponse.builder();
        responseBuilder.username(username);
        responseBuilder.token(token);
        responseBuilder.role(role);

        return responseBuilder.build();

    }








//    public ResponseEntity<String> createPassword(CreatePasswordRequest createPasswordRequest, HttpServletRequest request) {
//        methodHelper.getUserByHttpRequest(request);
//
//
//        if (isPasswordExists(createPasswordRequest.getPassword())) {
//            throw new BadRequestException(ErrorMessages.PASSWORD_HAS_ALREADY_TAKEN);
//        }
//
//        RoleType roleType = RoleType.valueOf(createPasswordRequest.getRoleName().toUpperCase());
//
//        switch (roleType) {
//
//            case TALASLI_IMALAT_AMIRI:
//                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
//                break;
//            case POLISAJ_AMIRI:
//                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
//                break;
//            case LIFT_MONTAJ_AMIRI:
//                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
//                break;
//            case KALITE_KONTROL:
//                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
//                break;
//            case BL_MONTAJ_AMIRI:
//                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
//                break;
//            case URETIM_PLANLAMA_AMIRI:
//                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
//                break;
//            case BOYAMA_VE_PAKETLEME_AMIRI:
//                assignPasswordToUsersByRole(roleType, createPasswordRequest.getPassword());
//                break;
//            default:
//                throw new IllegalArgumentException("Geçersiz rol adı: " + createPasswordRequest.getRoleName());
//        }
//
//        return ResponseEntity.ok("Şifre güncellendi.");
//
//    }

    private void assignPasswordToUsersByRole(RoleType roleType, String password) {

            User fetchUser = userRepository.findByUserRoleRoleType(roleType).orElseThrow(()->new BadRequestException(ErrorMessages.USER_NOT_FOUND));
            fetchUser.setPassword(passwordEncoder.encode(password));
            userRepository.save(fetchUser);

    }

    public boolean isPasswordExists(String password) {
        return userRepository.findByPassword(password).isPresent();//password kullaniliksa isPresent true doner kullanmayiksak false doner.
    }






}
