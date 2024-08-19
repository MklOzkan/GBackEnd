package com.project;

import com.project.domain.concretes.user.User;
import com.project.domain.concretes.user.UserRole;
import com.project.domain.enums.RoleType;
import com.project.payload.request.user.UserRequest;
import com.project.repository.user.UserRepository;
import com.project.repository.user.UserRoleRepository;
import com.project.service.user.AdminService;
import com.project.service.user.UserRoleService;
import com.project.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProjectApplication implements CommandLineRunner {

    private final UserRoleService userRoleService;
    private final AdminService adminService;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    public ProjectApplication(PasswordEncoder passwordEncoder, UserRepository userRepository, UserRoleService userRoleService, AdminService adminService, UserRoleRepository userRoleRepository, UserService userService) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleService = userRoleService;
        this.adminService = adminService;
        this.userRepository=userRepository;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRoleService.getAllUserRoles().isEmpty()) {
            UserRole admin = new UserRole();

            admin.setRoleType(RoleType.ADMIN);
            admin.setRoleName(RoleType.ADMIN.getName());
            userRoleRepository.save(admin);

            UserRole employee = new UserRole();
            employee.setRoleType(RoleType.EMPLOYEE);
            employee.setRoleName(RoleType.EMPLOYEE.getName());
            userRoleRepository.save(employee);

        }

        if (userService.getAllUsers().isEmpty()) {

            UserRequest userRequest = yonetici();
            userService.saveUser(userRequest, RoleType.ADMIN.getName());

            UserRequest userRequest1 = talasliImalatAmiri();
            userService.saveUser(userRequest1, RoleType.EMPLOYEE.getName());

            UserRequest userRequest2 = boyamaPaketlemeAmiri();
            userService.saveUser(userRequest2, RoleType.EMPLOYEE.getName());

            UserRequest userRequest3 = uretimPlanlama();
            userService.saveUser(userRequest3, RoleType.EMPLOYEE.getName());

            UserRequest userRequest4 = kaliteKontrol();
            userService.saveUser(userRequest4, RoleType.EMPLOYEE.getName());

            UserRequest userRequest5 = polisajAmiri();
            userService.saveUser(userRequest5, RoleType.EMPLOYEE.getName());

            UserRequest userRequest6 = liftMonatajAmiri();
            userService.saveUser(userRequest6, RoleType.EMPLOYEE.getName());

            UserRequest userRequest7 = blMontajAmiri();
            userService.saveUser(userRequest7, RoleType.EMPLOYEE.getName());

        }

    }

    private static UserRequest yonetici() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("Yonetici");
        userRequest.setPassword("Ankara01*");
        userRequest.setBuildIn(true);
        return userRequest;
    }

    private static UserRequest talasliImalatAmiri() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("TalasliImalatAmiri");
        userRequest.setPassword("Ankara02*");
        userRequest.setBuildIn(false);
        return userRequest;
    }

    private static UserRequest boyamaPaketlemeAmiri() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("BoyamaPaketlemeAmiri");
        userRequest.setPassword("Ankara03*");
        userRequest.setBuildIn(false);
        return userRequest;
    }

    private static UserRequest uretimPlanlama() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("UretimPlanlama");
        userRequest.setPassword("Ankara04*");
        userRequest.setBuildIn(false);
        return userRequest;
    }

    private static UserRequest kaliteKontrol() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("KaliteKontrol");
        userRequest.setPassword("Ankara05*");
        userRequest.setBuildIn(false);
        return userRequest;
    }

    private static UserRequest polisajAmiri() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("PolisajAmiri");
        userRequest.setPassword("Ankara06*");
        userRequest.setBuildIn(false);
        return userRequest;
    }

    private static UserRequest liftMonatajAmiri() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("LiftMontajAmiri");
        userRequest.setPassword("Ankara07*");
        userRequest.setBuildIn(false);
        return userRequest;
    }

    private static UserRequest blMontajAmiri() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("BlMontajAmiri");
        userRequest.setPassword("Ankara08*");
        userRequest.setBuildIn(false);
        return userRequest;
    }
}
