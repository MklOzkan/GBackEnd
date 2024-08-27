package com.project.service.user;

import com.project.domain.concretes.user.User;
import com.project.domain.concretes.user.UserRole;
import com.project.domain.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.payload.mappers.AdminMapper;
import com.project.payload.mappers.AuthenticationMapper;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.user.UpdatePasswordRequest;
import com.project.payload.request.user.UserRequest;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MethodHelper methodHelper;
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;
    private final AuthenticationMapper authenticationMapper;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<String> saveUser(UserRequest userRequest, String userRole) {
        User user = userMapper.mapUserRequestToUser(userRequest, userRole);
        userRepository.save(user);
        return ResponseEntity.ok("User created successfully");
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public ResponseMessage<String> updatePassword(String employeeName, UpdatePasswordRequest updatePasswordRequest, HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        User currentUser = methodHelper.findUserByUsername(username);
        System.out.println(currentUser.getUsername());
        if (!currentUser.getBuiltIn()){
            throw new BadRequestException(ErrorMessages.USER_IS_NOT_ADMIN);
        }

        User userToUpdate = methodHelper.findUserByUsername(employeeName);
        List<User> userList = getAllUsers();
        for(User userCheck : userList){
            if(passwordEncoder.matches(updatePasswordRequest.getNewPassword(),userCheck.getPassword())){
                throw new BadRequestException(SuccessMessages.PASSWORD_SHOULD_NOT_MATCHED);
            }
        }
        userToUpdate.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(userToUpdate);
        return ResponseMessage.<String>builder()
                .message("Password updated successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
