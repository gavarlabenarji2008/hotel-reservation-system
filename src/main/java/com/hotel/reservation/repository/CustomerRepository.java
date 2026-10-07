package com.hotel.reservation.repository;

import com.hotel.reservation.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByNameContainingIgnoreCase(String name);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
