package com.project.rentify.service;

import com.project.rentify.models.RentalContracts;
import com.project.rentify.models.Tenant;
import com.project.rentify.repository.RentalContractRepo;
import com.project.rentify.repository.TenantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantService {

    @Autowired
    TenantRepo tenantRepo;

    @Autowired
    RentalContractRepo contractRepo;

    public List<Tenant> getAllTenants() {
        return tenantRepo.findAll();
    }

    public Optional<Tenant> getTenantById(int id) {
        return tenantRepo.findById(id);
    }

    public List<Tenant> searchTenant(String search) {
        return tenantRepo.searchByNameContainingOrEmailContainingOrPhoneContaining(search, search, search);
    }

    public List<RentalContracts> getContractForTenant(int id) {
        return contractRepo.findAllByTenantId(id);
    }

    public Tenant createTenant(Tenant t) {
        return tenantRepo.save(t);
    }

    public Tenant updateTenant(Tenant t) {
        return tenantRepo.save(t);
    }
}
