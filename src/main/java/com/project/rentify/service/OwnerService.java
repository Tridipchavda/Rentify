package com.project.rentify.service;

import com.project.rentify.models.Owner;
import com.project.rentify.repository.OwnerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService {

    @Autowired
    OwnerRepo ownerRepo;

    public List<Owner> getAllOwner(){
        return ownerRepo.findAll();
    }

    public List<Owner> searchOwner(String search){
        return ownerRepo.searchByNameContainingOrEmailContainingOrPhoneContaining(search,search,search);
    }

    public Optional<Owner> getOwnerById(int id){
        return ownerRepo.findById(id);
    }

    public Owner createOwner(Owner o) {
        return ownerRepo.save(o);
    }

    public Owner updateOwner(Owner o) {
        return ownerRepo.save(o);
    }
}
