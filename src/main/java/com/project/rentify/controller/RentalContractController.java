package com.project.rentify.controller;

import com.project.rentify.models.Property;
import com.project.rentify.models.RentalContracts;
import com.project.rentify.models.Tenant;
import com.project.rentify.models.dto.RentalContractDto;
import com.project.rentify.models.enums.ContractStatus;
import com.project.rentify.models.enums.PropertyStatus;
import com.project.rentify.service.PropertyService;
import com.project.rentify.service.RentalContractService;
import com.project.rentify.service.TenantService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/contracts")
public class RentalContractController {

    @Autowired
    RentalContractService rentalContractService;

    @Autowired
    TenantService tenantService;

    @Autowired
    PropertyService propertyService;

    // ---------------- CREATE CONTRACT ----------------
    @PostMapping("/")
    public ResponseEntity<RentalContracts> createRentalContract(@Valid @RequestBody RentalContractDto dto) {

        log.info("POST /api/contracts createRentalContract tenantId={}, propertyId={}",
                dto.getTenantId(), dto.getPropertyId());

        RentalContracts create;

        try {
            Tenant t = tenantService.getTenantById(dto.getTenantId())
                    .orElseThrow(() -> {
                        log.warn("Tenant not found id={}", dto.getTenantId());
                        return new IllegalArgumentException("tenant not found with id :" + dto.getTenantId());
                    });

            Property p = propertyService.getPropertyById(dto.getPropertyId())
                    .orElseThrow(() -> {
                        log.warn("Property not found id={}", dto.getPropertyId());
                        return new IllegalArgumentException("property not found with id :" + dto.getPropertyId());
                    });

            // ---------------- BUSINESS RULE ----------------
            if (p.getStatus() == PropertyStatus.OCCUPIED) {
                log.warn("Contract creation blocked: property already occupied propertyId={}", p.getId());
                throw new RuntimeException("Property is already occupied");
            }

            p.setStatus(PropertyStatus.OCCUPIED);

            RentalContracts rc = RentalContracts.builder()
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .rentAmount(dto.getRentAmount())
                    .status(ContractStatus.ACTIVE)
                    .latePenaltyFees(dto.getLatePenaltyFees())
                    .tenant(t)
                    .property(p)
                    .build();

            create = rentalContractService.createRentalContract(rc);

            log.info("Rental contract created successfully contractId={}, tenantId={}, propertyId={}",
                    create.getId(), t.getId(), p.getId());

        } catch (Exception e) {
            log.error("Error while creating rental contract tenantId={}, propertyId={}",
                    dto.getTenantId(), dto.getPropertyId(), e);

            throw new RuntimeException("error while creating rental contract :" + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    // ---------------- GET ALL CONTRACTS ----------------
    @GetMapping("/")
    public ResponseEntity<List<RentalContracts>> getAllRentalContract(
            @RequestParam(name = "name", required = false) String search) {

        log.info("GET /api/contracts called with search={}", search);

        List<RentalContracts> rentalContracts;

        try {
            if (search != null) {
                log.debug("Searching contracts with keyword={}", search);
                rentalContracts = rentalContractService.searchContract(search);
            } else {
                log.debug("Fetching all rental contracts");
                rentalContracts = rentalContractService.getAllRentalContracts();
            }

            log.info("Contracts fetched count={}", rentalContracts.size());

        } catch (Exception e) {
            log.error("Error while fetching contracts search={}", search, e);
            throw new RuntimeException("error while getting contracts :" + e.getMessage());
        }

        return ResponseEntity.ok(rentalContracts);
    }

    // ---------------- GET CONTRACT BY ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<Optional<RentalContracts>> getRentalContract(@PathVariable int id) {

        log.info("GET /api/contracts/{} called", id);

        Optional<RentalContracts> rentalContract;

        try {
            rentalContract = rentalContractService.getRentalContractById(id);

            log.info("Contract fetched id={} found={}", id, rentalContract.isPresent());

        } catch (Exception e) {
            log.error("Error while getting contract id={}", id, e);
            throw new RuntimeException("error while getting contract by id :" + e.getMessage());
        }

        return ResponseEntity.ok(rentalContract);
    }

    // ---------------- UPDATE CONTRACT ----------------
    @PutMapping("/{id}")
    public ResponseEntity<RentalContracts> updateProperty(
            @Valid @RequestBody RentalContractDto dto,
            @PathVariable int id) {

        log.info("PUT /api/contracts/{} updateContract called", id);

        RentalContracts updated;

        try {
            Property p = propertyService.getPropertyById(dto.getPropertyId())
                    .orElseThrow(() -> {
                        log.warn("Property not found id={}", dto.getPropertyId());
                        return new IllegalArgumentException("Property not found with id: " + dto.getPropertyId());
                    });

            Tenant t = tenantService.getTenantById(dto.getTenantId())
                    .orElseThrow(() -> {
                        log.warn("Tenant not found id={}", dto.getTenantId());
                        return new IllegalArgumentException("Tenant not found with id: " + dto.getTenantId());
                    });

            RentalContracts rc = RentalContracts.builder()
                    .Id(id)
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .latePenaltyFees(dto.getLatePenaltyFees())
                    .status(ContractStatus.ACTIVE)
                    .rentAmount(dto.getRentAmount())
                    .property(p)
                    .tenant(t)
                    .build();

            updated = rentalContractService.updateRentalContract(rc);

            log.info("Rental contract updated successfully id={}", updated.getId());

        } catch (Exception e) {
            log.error("Error while updating rental contract id={}", id, e);
            throw new RuntimeException("error while updating contract :" + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }
}