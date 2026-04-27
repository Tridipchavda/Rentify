package com.project.rentify.repository;

import com.project.rentify.models.RentalContracts;
import com.project.rentify.models.enums.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalContractRepo extends JpaRepository<RentalContracts,Integer> {
    List<RentalContracts> findAllByTenantId(int id);

    List<RentalContracts> findAllByProperty_NameOrTenant_NameOrStatus(String propertyName, String tenantName, ContractStatus status);
}
