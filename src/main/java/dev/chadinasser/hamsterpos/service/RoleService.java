package dev.chadinasser.hamsterpos.service;

import dev.chadinasser.hamsterpos.model.Role;
import dev.chadinasser.hamsterpos.model.Role.RoleType;
import dev.chadinasser.hamsterpos.repo.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Optional<Role> findByRole(RoleType role) {
        return roleRepo.findByName(RoleType.valueOf(role.name()));
    }
}
