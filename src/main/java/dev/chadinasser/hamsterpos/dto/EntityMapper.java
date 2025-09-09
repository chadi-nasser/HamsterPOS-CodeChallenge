package dev.chadinasser.hamsterpos.dto;

public interface EntityMapper<E, D> {
    E toEntity();

    D fromEntity(E entity);
}
