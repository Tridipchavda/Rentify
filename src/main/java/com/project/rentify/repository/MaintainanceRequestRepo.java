package com.project.rentify.repository;

import com.project.rentify.models.MaintainanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintainanceRequestRepo extends JpaRepository<MaintainanceRequest,Integer> {

    List<MaintainanceRequest> findAllByPropertyId(int id);
    List<MaintainanceRequest> findAllByTenantId(int id);

    List<MaintainanceRequest> searchByDescriptionContainingOrDescriptionContainingOrTenantNameOrPropertyName(String description, String description2, String tenant_name, String property_name);
}
