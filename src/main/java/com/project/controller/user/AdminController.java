package com.project.controller.user;

import com.project.payload.request.UserUpdateByAdminRequest;
import com.project.payload.response.UserResponse;
import com.project.service.user.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/getAll")
        //TODO pageable cevir
        //http://localhost:8080/admin/getAll
    ResponseEntity<List<UserResponse>> getAllUsers(HttpServletRequest request) {

        return ResponseEntity.ok(adminService.getAllUsers(request));

    }


    @GetMapping //http://localhost:8080/admin?email=asd@gmail.com
    public ResponseEntity<UserResponse> getUserByEmail(HttpServletRequest request, @RequestParam String email) {
        return adminService.getUserByEmail(request, email);
    }

    @PatchMapping
    public ResponseEntity<String> setUserRole(HttpServletRequest request, @RequestParam Set<String> role, @RequestParam String email) {
        return adminService.setUserRole(request, role, email);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse>updateUserByAdmin(@PathVariable Long id, @Valid @RequestBody UserUpdateByAdminRequest adminRequest,HttpServletRequest request){

        return adminService.updateUserByAdmin(id,adminRequest,request);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String>deleteUserByAdmin(@PathVariable Long id,HttpServletRequest request){
        return ResponseEntity.ok(adminService.deleteUserByAdmin(id,request));
    }

}
