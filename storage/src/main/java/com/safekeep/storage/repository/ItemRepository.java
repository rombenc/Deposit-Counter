package com.safekeep.storage.repository;

import com.safekeep.storage.model.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "SELECT COUNT(*) FROM items WHERE customer_id = :customerId AND status = :status", nativeQuery = true)
    long countByCustomerAndStatus(@Param("customerId") Long customerId, @Param("status") String status);

    @Query(value = "SELECT * FROM items WHERE customer_id = :customerId", nativeQuery = true)
    List<Item> findByCustomer(@Param("customerId") Long customerId);

    @Query(value = "SELECT * FROM items WHERE status = :status AND expected_pickup_date < :date", nativeQuery = true)
    List<Item> findByStatusAndExpectedPickupDateBefore(@Param("status") String status, @Param("date") LocalDateTime date);
}