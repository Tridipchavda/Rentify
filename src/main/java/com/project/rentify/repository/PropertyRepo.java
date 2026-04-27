package com.project.rentify.repository;

import com.project.rentify.models.Property;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyRepo extends JpaRepository<Property,Integer> {
    List<Property> searchByNameContainingOrAddressContainingOrCityContaining(String name, String address, String city);

    List<Property> getPropertiesByOwnerId(int id);
}
