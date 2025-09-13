package dev.chadinasser.hamsterpos.repo;

import dev.chadinasser.hamsterpos.model.Role;
import dev.chadinasser.hamsterpos.model.Role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
