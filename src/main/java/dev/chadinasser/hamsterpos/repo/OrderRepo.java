package dev.chadinasser.hamsterpos.repo;

import dev.chadinasser.hamsterpos.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {
    Page<Order> findAllByUser_Id(UUID userId, Pageable pageable);
}
