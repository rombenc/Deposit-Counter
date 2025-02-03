package com.safekeep.storage.repository;

import com.safekeep.storage.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByNameLikeOrderByNameAsc(String name);
}
