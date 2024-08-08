package com.project.service.user;

import com.project.domain.concretes.user.User;
import com.project.domain.enums.RoleType;
import com.project.payload.mappers.AdminMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.response.UserResponse;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AdminMapper adminMapper;
    private final MethodHelper methodHelper;
    private final UserRoleService userRoleService;


    public List<UserResponse> getAllUsers(HttpServletRequest request) {

        User user = methodHelper.getUserByHttpRequest(request);
        methodHelper.checkRoles(user, RoleType.ADMIN);

        List<User> users = userRepository.findAll();

        return users.stream().map(adminMapper::UserToUserResponse).collect(Collectors.toList());

    }

    public ResponseEntity<UserResponse> getUserByEmail(HttpServletRequest request, String email) {

        User user = methodHelper.getUserByHttpRequest(request);
        methodHelper.checkRoles(user, RoleType.ADMIN);

        return ResponseEntity.ok(adminMapper.UserToUserResponse(methodHelper.findByUserByEmail(email)));


    }

    public ResponseEntity<String> setUserRole(HttpServletRequest request, String role, String email) {

        User authenticatedUser = methodHelper.getUserByHttpRequest(request);
        methodHelper.checkRoles(authenticatedUser, RoleType.ADMIN);
        User user = methodHelper.findByUserByEmail(email);

        try {
            RoleType roleType = RoleType.fromString(role);
          //  userRoleService
            //TODO DEVAM EDILECEK

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ErrorMessages.USER_ROLE_IS_NOT_FOUND);
        }
return null; //TODO NUll duzeltilecek
    }

    public Long countAllAdmins() {

        return userRepository.countAllAdmins(RoleType.ADMIN);

    }
}
