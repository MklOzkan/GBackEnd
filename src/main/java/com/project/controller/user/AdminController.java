package com.project.controller.user;

import com.project.payload.request.user.UserUpdateByAdminRequest;
import com.project.payload.response.user.UserResponse;
import com.project.service.user.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/getAll")  //http://localhost:8080/admin/getAll
    ResponseEntity<Page<UserResponse>> getAllUsers(HttpServletRequest request,
                                                   @RequestParam(value = "firstName", required = false) String firstName,
                                                   @RequestParam(value = "lastName", required = false) String lastName,
                                                   @RequestParam(value = "email", required = false) String email,
                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                                   @RequestParam(value = "sort", defaultValue = "firstName") String sort,
                                                   @RequestParam(value = "type", defaultValue = "desc") String type) {

        return ResponseEntity.ok(adminService.getAllUsers(request,firstName,lastName,email,page,size,sort,type));

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
