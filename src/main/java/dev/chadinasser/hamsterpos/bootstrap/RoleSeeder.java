package dev.chadinasser.hamsterpos.bootstrap;

import dev.chadinasser.hamsterpos.model.Role;
import dev.chadinasser.hamsterpos.model.Role.RoleType;
import dev.chadinasser.hamsterpos.repo.RoleRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("!test") // Exclude from test profile
@Order(1)
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepo roleRepo;

    public RoleSeeder(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.loadRoles();
    }

    private void loadRoles() {
        RoleType[] roles = RoleType.values();
        List<Role> roleList = new ArrayList<>();
        for (RoleType role : roles) {
            if (roleRepo.findByName(role).isEmpty()) {
                Role newRole = new Role();
                newRole.setName(role);
                roleList.add(newRole);
            }
        }
        roleRepo.saveAll(roleList);
    }
}
