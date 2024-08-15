package com.project.service.helper;

import com.project.domain.concretes.user.User;
import com.project.exception.BadRequestException;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.abstracts.AbstractUserRequest;
import com.project.repository.user.UserRepository;
import com.project.service.user.UserRoleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MethodHelper {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;

    public void checkDuplicate(String email) {

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(String.format(ErrorMessages.THIS_PHONE_NUMBER_IS_ALREADY_TAKEN, email));
        }
    }

    public User findByUserByRole(String role){

        if (role == null || role.isEmpty()) {
            throw new ResourceNotFoundException("Role can not be null or empty");
        }

        return userRepository.findByUserByUserRoleByRoleName(role).orElseThrow(()-> new BadRequestException(String.format(ErrorMessages.THERE_IS_NO_USER_WITH_THIS_Role,role)));
    }



    public User getUserByHttpRequest(HttpServletRequest request) {
        return userRepository.findByUserByUserRoleByRoleName(getRoleByRequest(request)).orElseThrow(()-> new ResourceNotFoundException("UserRole not found"));
    }


    public String getRoleByRequest(HttpServletRequest request) {
        return (String) request.getAttribute("userRole");
    }

    public boolean isBuiltIn(User user) {

        return user.getBuiltIn();
    }


//    public void checkUniqueProperties(User user, String password) {
//
//        boolean changed = false;
//        String changedPassword = "";
//
//
//        if (!user.getPassword().equalsIgnoreCase(password)) {
//            changed = true;
//            changedPassword = request.getEmail();
//        }
//        if (changed) {
//            checkDuplicate(changedPassword);
//        }
//    }


}
