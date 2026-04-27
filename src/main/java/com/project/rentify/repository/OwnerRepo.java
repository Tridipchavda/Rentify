package com.project.rentify.repository;

import com.project.rentify.models.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepo extends JpaRepository<Owner,Integer> {


    List<Owner> searchByNameContainingOrEmailContainingOrPhoneContaining(String name, String email, String phone);


}
