package com.project.rentify.service;

import com.project.rentify.models.MaintainanceRequest;
import com.project.rentify.repository.MaintainanceRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceRequestService {

    @Autowired
    MaintainanceRequestRepo maintainanceRequestRepo;

    public List<MaintainanceRequest> getAllRequest() {
        return maintainanceRequestRepo.findAll();
    }

    public List<MaintainanceRequest> searchRequest(String search) {
        return maintainanceRequestRepo.searchByDescriptionContainingOrDescriptionContainingOrTenantNameOrPropertyName(search,search,search,search);
    }

    public Optional<MaintainanceRequest> getRequestById(int id) {
        return maintainanceRequestRepo.findById(id);
    }

    public List<MaintainanceRequest> getRequestsForProperty(int id) {
        return maintainanceRequestRepo.findAllByPropertyId(id);
    }

    public List<MaintainanceRequest> getRequestsForTenant(int id) {
        return maintainanceRequestRepo.findAllByTenantId(id);
    }

    public MaintainanceRequest createRequest(MaintainanceRequest t) {
        return maintainanceRequestRepo.save(t);
    }

    public MaintainanceRequest updateRequest(MaintainanceRequest t) {
        return maintainanceRequestRepo.save(t);
    }
}
