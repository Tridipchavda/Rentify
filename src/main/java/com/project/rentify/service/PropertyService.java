package com.project.rentify.service;

import com.project.rentify.models.Property;
import com.project.rentify.repository.PropertyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {
    @Autowired
    PropertyRepo propertyRepo;

    public List<Property> getAllProperty(){
        return propertyRepo.findAll();
    }

    public List<Property> searchProperty(String search) {
        return propertyRepo.searchByNameContainingOrAddressContainingOrCityContaining(search,search,search);
    }

    public List<Property> getPropertiesByOwner(int id){
        return propertyRepo.getPropertiesByOwnerId(id);
    }

    public Property createProperty(Property p) {
        return propertyRepo.save(p);
    }

    public Property updateProperty(Property p) {
        return propertyRepo.save(p);
    }

    public Optional<Property> getPropertyById(int id) {
        return propertyRepo.findById(id);
    }
}
