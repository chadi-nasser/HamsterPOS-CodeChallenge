package dev.chadinasser.hamsterpos.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @SequenceGenerator(name = "role_seq", sequenceName = "role_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @Column(updatable = false, nullable = false)
    private Long id;
    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType name;

    public enum RoleType {
        ADMIN,
        USER
    }
}
