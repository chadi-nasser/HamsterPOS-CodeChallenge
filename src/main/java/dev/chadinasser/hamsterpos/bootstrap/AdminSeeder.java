package dev.chadinasser.hamsterpos.bootstrap;

import dev.chadinasser.hamsterpos.model.Role;
import dev.chadinasser.hamsterpos.model.RoleType;
import dev.chadinasser.hamsterpos.model.User;
import dev.chadinasser.hamsterpos.repo.RoleRepo;
import dev.chadinasser.hamsterpos.repo.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!test") // Exclude from test profile
@Order(2)
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.admin.username}")
    private String adminUsername;
    @Value("${application.admin.password}")
    private String adminPassword;

    public AdminSeeder(RoleRepo roleRepo, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.createAdminstrator();
    }

    private void createAdminstrator() {
        Role adminRole = roleRepo.findByName(RoleType.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));
        if (userRepo.findByUsername(adminUsername).isEmpty()) {
            var user = User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(adminRole)
                    .build();
            userRepo.save(user);
        }
    }
}
