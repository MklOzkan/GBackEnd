package com.project.service.helper;

import com.project.domain.concretes.business.Order;
import com.project.domain.concretes.user.User;
import com.project.domain.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.exception.ConflictException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.response.business.MultipleResponses;
import com.project.payload.response.business.ResponseMessage;
import com.project.repository.business.OrderRepository;
import com.project.repository.user.UserRepository;
import com.project.service.user.UserRoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MethodHelper {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final OrderRepository orderRepository;

    public User loadUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND,username));
        }
        return user;
    }




    public void checkRole(User user, RoleType roleType) {
     if(!user.getUserRole().getRoleType().equals(roleType)) throw new BadRequestException(ErrorMessages.USER_ROLE_IS_NOT_FOUND);
    }

    public User findUserWithId(Long id) {
      return   userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(ErrorMessages.USER_ID_IS_NOT_FOUND));
    }

    public void isUserExist(String role) {
        boolean isExist = userRepository.existsByUserRoleRoleName(role);
        if (!isExist) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.THERE_IS_NO_USER_WITH_THIS_ROLE, role));
        }
    }

    public User findUserByUsername(String username) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND, username));
        }

        return user;
    }

    public void isAdmin(User user) {
        if (!user.getUserRole().getRoleType().getName().equals(RoleType.ADMIN.getName())) {
            throw new BadRequestException(ErrorMessages.USER_IS_NOT_ADMIN);
        }
    }

    public void isEmployee(User user) {
        if (!user.getUserRole().getRoleType().equals(RoleType.EMPLOYEE)) {
            throw new BadRequestException(ErrorMessages.USER_IS_NOT_EMPLOYEE);
        }
    }

    //Order Helpers
    public Order findOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new ResourceNotFoundException(String.format(ErrorMessages.ORDER_NOT_FOUND, orderNumber));
        }
        return order;
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.ORDER_ID_IS_NOT_FOUND, id)));
    }

    public void checkGasanNo(String gasanNo) {
        boolean isExist = orderRepository.existsByGasanNo(gasanNo);
        if (isExist) {
            throw new ConflictException(String.format(ErrorMessages.GASAN_NO_IS_ALREADY_EXIST, gasanNo));
        }
    }

    public void checkOrderNumber( String orderNumber) {
        boolean isExist = orderRepository.existsByOrderNumber(orderNumber);
        if (isExist) {
            throw new ConflictException(String.format(ErrorMessages.ORDER_NUMBER_IS_ALREADY_EXIST, orderNumber));
        }
    }

    public <T> ResponseMessage<T> createResponse(String message, HttpStatus httpStatus, T data) {
        return ResponseMessage.<T>builder()
                .message(message)
                .httpStatus(httpStatus)
                .returnBody(data)
                .build();
    }

    public <T, U, V> MultipleResponses<T, U, V> multipleResponse(String message, HttpStatus httpStatus, T data, U data2, V data3) {
        return MultipleResponses.<T, U, V>builder()
                .message(message)
                .httpStatus(httpStatus)
                .returnBody(data)
                .returnBody2(data2)
                .returnBody3(data3)
                .build();
    }


}
