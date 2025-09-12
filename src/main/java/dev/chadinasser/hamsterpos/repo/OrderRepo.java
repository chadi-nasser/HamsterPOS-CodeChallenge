package dev.chadinasser.hamsterpos.repo;

import dev.chadinasser.hamsterpos.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {
    @Query(
            "SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.id = :id"
    )
    Optional<Order> findByIdWithItems(UUID id);

    List<Order> findAllByUser_Id(UUID userId, Pageable pageable);
}
