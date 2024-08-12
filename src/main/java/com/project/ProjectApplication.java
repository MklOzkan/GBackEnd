package com.project;

import com.project.domain.concretes.user.User;
import com.project.domain.concretes.user.UserRole;
import com.project.domain.enums.RoleType;
import com.project.repository.user.UserRepository;
import com.project.repository.user.UserRoleRepository;
import com.project.service.user.AdminService;
import com.project.service.user.UserRoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class ProjectApplication implements CommandLineRunner {

    private final UserRoleService userRoleService;
    private final AdminService adminService;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;


    public ProjectApplication(UserRepository userRepository,UserRoleService userRoleService, AdminService adminService, UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleService = userRoleService;
        this.adminService = adminService;
        this.userRepository=userRepository;

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

            UserRole admin1 = new UserRole();
            admin1.setRoleType(RoleType.BOYAMA_VE_PAKETLEME_AMIRI);
            admin1.setRoleName(RoleType.BOYAMA_VE_PAKETLEME_AMIRI.getName());
            userRoleRepository.save(admin1);

            UserRole admin2 = new UserRole();
            admin2.setRoleType(RoleType.BL_MONTAJ_AMIRI);
            admin2.setRoleName(RoleType.BL_MONTAJ_AMIRI.getName());
            userRoleRepository.save(admin2);

            UserRole admin3 = new UserRole();
            admin3.setRoleType(RoleType.LIFT_MONTAJ_AMIRI);
            admin3.setRoleName(RoleType.LIFT_MONTAJ_AMIRI.getName());
            userRoleRepository.save(admin3);

            UserRole admin4 = new UserRole();
            admin4.setRoleType(RoleType.POLISAJ_AMIRI);
            admin4.setRoleName(RoleType.POLISAJ_AMIRI.getName());
            userRoleRepository.save(admin4);

            UserRole admin5 = new UserRole();
            admin5.setRoleType(RoleType.TALASLI_IMALAT_AMIRI);
            admin5.setRoleName(RoleType.TALASLI_IMALAT_AMIRI.getName());
            userRoleRepository.save(admin5);

        }

        if (adminService.countAllAdmins() == 0) {

            UserRole userRole=userRoleService.getUserRoleByRoleType(RoleType.ADMIN);

            Set<UserRole> roles=new HashSet<>();
            roles.add(userRole);

            User admin = new User();

            admin.setEmail("admin@admin.com");
            //admin.setPassword(passwordEncoder.encode("A1a@secure"));
            admin.setPassword("asd");
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setBuiltIn(true);
            admin.setUserRole(roles);
            userRepository.save(admin);


        }

    }
}
