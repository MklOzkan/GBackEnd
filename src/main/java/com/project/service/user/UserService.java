package com.project.service.user;

import com.project.domain.concretes.user.User;
import com.project.domain.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.payload.mappers.AdminMapper;
import com.project.payload.mappers.AuthenticationMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.user.UpdateUsersRequest;
import com.project.payload.request.user.UserPasswordRequest;
import com.project.payload.response.abstracts.BaseUserResponse;
import com.project.payload.response.user.UserResponse;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MethodHelper methodHelper;
    private final AdminMapper adminMapper;
    private final AuthenticationMapper authenticationMapper;


    public ResponseEntity<String> updateUserPassword(UserPasswordRequest request, HttpServletRequest auth) {
        User user = methodHelper.getUserByHttpRequest(auth);
        if (methodHelper.isBuiltIn(user)) {
            throw new BadRequestException(ErrorMessages.BUILT_IN_USER_CAN_NOT_BE_UPDATED);
        }

        if (!(Objects.equals(request.getNewPassword(), request.getReWriteNewPassword()))) {
            throw new BadRequestException(ErrorMessages.THE_PASSWORDS_ARE_NOT_MATCHED);
        }


    //    if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
     //       throw new BadRequestException(ErrorMessages.PASSWORD_IS_INCCORECT);
      //  }
        //TODO Burasi duzeltilcek ------
      //  String password = passwordEncoder.encode(request.getNewPassword());
            String password="1";
        user.setPassword(password);
        userRepository.save(user);


        return ResponseEntity.ok(SuccessMessages.PASSWORD_UPDATED_SUCCESSFULLY);
    }

    public ResponseEntity<UserResponse> getUserById(Long id, HttpServletRequest request) {
        User user = methodHelper.getUserByHttpRequest(request);
        methodHelper.checkRoles(user, RoleType.ADMIN, RoleType.BL_MONTAJ_AMIRI,RoleType.POLISAJ_AMIRI,RoleType.BOYAMA_VE_PAKETLEME_AMIRI
        ,RoleType.LIFT_MONTAJ_AMIRI,RoleType.TALASLI_IMALAT_AMIRI);
        UserResponse userResponse = adminMapper.userToUserResponse(methodHelper.findUserWithId(id));
        return ResponseEntity.ok(userResponse);
    }

//    public ResponseEntity<BaseUserResponse> updateAuthenticatedUser(UpdateUsersRequest request, HttpServletRequest auth) {
//       User user =methodHelper.getUserByHttpRequest(auth);
//       user.setFirstName(request.getFirstName());
//       user.setLastName(request.getLastName());
//       methodHelper.checkUniqueProperties(user,request);
//       user.setEmail(request.getEmail());
//     //TODO CheckRolese gerek var mi
//
//       userRepository.save(user);
//       return ResponseEntity.ok(authenticationMapper.UserToSignInResponse(user));
//
//    }
}
