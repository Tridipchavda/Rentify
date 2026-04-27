package com.project.rentify.repository;

import com.project.rentify.models.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenantRepo extends JpaRepository<Tenant,Integer> {

    List<Tenant> searchByNameContainingOrEmailContainingOrPhoneContaining(String name, String email, String phone);
}
