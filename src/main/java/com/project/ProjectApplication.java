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
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProjectApplication implements CommandLineRunner {

    private final UserRoleService userRoleService;
    private final AdminService adminService;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;




    public ProjectApplication(PasswordEncoder passwordEncoder,UserRepository userRepository, UserRoleService userRoleService, AdminService adminService, UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleService = userRoleService;
        this.adminService = adminService;
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;

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



            UserRole admin6 = new UserRole();
            admin5.setRoleType(RoleType.KALITE_KONTROL);
            admin5.setRoleName(RoleType.KALITE_KONTROL.getName());
            userRoleRepository.save(admin6);

            UserRole admin7 = new UserRole();
            admin5.setRoleType(RoleType.URETIM_PLANLAMA_AMIRI);
            admin5.setRoleName(RoleType.URETIM_PLANLAMA_AMIRI.getName());
            userRoleRepository.save(admin7);

        }

        if (adminService.countAllAdmins() == 0) {

            UserRole userRole=userRoleService.getUserRoleByRoleType(RoleType.ADMIN);

            User admin = new User();
            	admin.setUserName("SuperAdmin");

            admin.setPassword(passwordEncoder.encode("A1a@secure"));


            UserRole userRole1=userRoleService.getUserRoleByRoleType(RoleType.BOYAMA_VE_PAKETLEME_AMIRI);
            UserRole userRole2=userRoleService.getUserRoleByRoleType(RoleType.URETIM_PLANLAMA_AMIRI);
            UserRole userRole3=userRoleService.getUserRoleByRoleType(RoleType.KALITE_KONTROL);
            UserRole userRole4=userRoleService.getUserRoleByRoleType(RoleType.POLISAJ_AMIRI);
            UserRole userRole5=userRoleService.getUserRoleByRoleType(RoleType.TALASLI_IMALAT_AMIRI);
            UserRole userRole6=userRoleService.getUserRoleByRoleType(RoleType.LIFT_MONTAJ_AMIRI);
            UserRole userRole7=userRoleService.getUserRoleByRoleType(RoleType.BL_MONTAJ_AMIRI);

            admin.setBuiltIn(true);
            admin.setUserRole(userRole);
            userRepository.save(admin);




            User talasliImalatAmiri = new User();
            talasliImalatAmiri.setBuiltIn(true);
            talasliImalatAmiri.setUserRole(userRole5);
            userRepository.save(talasliImalatAmiri);

            User boyamaPaketlemeAmiri = new User();
            boyamaPaketlemeAmiri.setBuiltIn(true);
            boyamaPaketlemeAmiri.setUserRole(userRole1);
            userRepository.save(boyamaPaketlemeAmiri);

            User uretimPlanlama = new User();
            uretimPlanlama.setBuiltIn(true);
            uretimPlanlama.setUserRole(userRole2);
            userRepository.save(uretimPlanlama);

            User kaliteKontrol = new User();
            kaliteKontrol.setBuiltIn(true);
            kaliteKontrol.setUserRole(userRole3);
            userRepository.save(kaliteKontrol);

            User polisajAmiri = new User();
            polisajAmiri.setBuiltIn(true);
            polisajAmiri.setUserRole(userRole4);
            userRepository.save(polisajAmiri);

            User liftMonatajAmiri = new User();
            liftMonatajAmiri.setBuiltIn(true);
            liftMonatajAmiri.setUserRole(userRole6);
            userRepository.save(liftMonatajAmiri);

            User blMontajAmiri = new User();
            blMontajAmiri.setBuiltIn(true);
            blMontajAmiri.setUserRole(userRole7);
            userRepository.save(blMontajAmiri);

        }

    }
}
