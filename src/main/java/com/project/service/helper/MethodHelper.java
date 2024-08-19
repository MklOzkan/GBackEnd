package com.project.service.helper;

import com.project.domain.concretes.user.User;
import com.project.domain.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
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



    public User findByUserByRole(String role){

        if (role == null || role.isEmpty()) {
            throw new ResourceNotFoundException("Role can not be null or empty");
        }

        return userRepository.findByUserRoleRoleName(role).orElseThrow(()-> new BadRequestException(String.format(ErrorMessages.THERE_IS_NO_USER_WITH_THIS_Role,role)));
    }



    public User getUserByHttpRequest(HttpServletRequest request) {
        return userRepository.findByUserRoleRoleName(getUserNameByRequest(request)).orElseThrow(()-> new ResourceNotFoundException("Username not found"));
    }


    public String getUserNameByRequest(HttpServletRequest request) {
        return (String) request.getAttribute("userName");
    }

    public boolean isBuiltIn(User user) {

        return user.getBuiltIn();
    }

    public void checkRole(User user, RoleType roleType) {
     if(!user.getUserRole().getRoleType().equals(roleType)) throw new BadRequestException(ErrorMessages.USER_ROLE_IS_NOT_FOUND);
    }

    public User findUserWithId(Long id) {
      return   userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(ErrorMessages.USER_ID_IS_NOT_FOUND));
    }

    public boolean isUserExist(String role) {
        boolean isExist = userRepository.existsByUserRoleRoleName(role);
        if (!isExist) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.THERE_IS_NO_USER_WITH_THIS_ROLE, role));
        }
        return true;
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
