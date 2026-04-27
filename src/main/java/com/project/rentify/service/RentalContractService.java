package com.project.rentify.service;

import com.project.rentify.models.RentalContracts;
import com.project.rentify.models.enums.ContractStatus;
import com.project.rentify.repository.RentalContractRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalContractService {

    @Autowired
    RentalContractRepo rentalContractRepo;

    public RentalContracts createRentalContract(RentalContracts rc){
        return rentalContractRepo.save(rc);
    }

    public List<RentalContracts> getAllRentalContracts(){
        return rentalContractRepo.findAll();
    }

    public RentalContracts updateRentalContract(RentalContracts rc) {
        return rentalContractRepo.save(rc);
    }

    public Optional<RentalContracts> getRentalContractById(int id) {
        return rentalContractRepo.findById(id);
    }

    public List<RentalContracts> searchContract(String search) {
        return rentalContractRepo.findAllByProperty_NameOrTenant_NameOrStatus(search,search,ContractStatus.valueOf(search));
    }
}
