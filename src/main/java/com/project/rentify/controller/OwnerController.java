package com.project.rentify.controller;

import com.project.rentify.models.Owner;
import com.project.rentify.models.Property;
import com.project.rentify.models.dto.OwnerDto;
import com.project.rentify.repository.PropertyRepo;
import com.project.rentify.service.OwnerService;
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
@RequestMapping("/api/owner")
public class OwnerController {

    @Autowired
    OwnerService ownerService;

    @Autowired
    private PropertyRepo propertyRepo;

    // ---------------- GET ALL OWNERS ----------------
    @GetMapping("/")
    public ResponseEntity<List<Owner>> getAllOwners(@RequestParam(name = "name", required = false) String search) {

        log.info("GET /api/owner called with search={}", search);

        List<Owner> owners;

        try {
            if (search == null) {
                log.debug("Fetching all owners");
                owners = ownerService.getAllOwner();
            } else {
                log.debug("Searching owners with keyword={}", search);
                owners = ownerService.searchOwner(search);
            }

            log.info("Owners fetched count={}", owners.size());

        } catch (Exception e) {
            log.error("Error while fetching owners search={}", search, e);
            throw new RuntimeException("error while fetching owners :" + e.getMessage());
        }

        return ResponseEntity.ok(owners);
    }

    // ---------------- GET OWNER BY ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Owner>> getOwnerById(@PathVariable int id) {

        log.info("GET /api/owner/{} called", id);

        Optional<Owner> owner;

        try {
            owner = ownerService.getOwnerById(id);

            log.info("Owner fetched id={} found={}", id, owner.isPresent());

        } catch (Exception e) {
            log.error("Error while finding owner id={}", id, e);
            throw new RuntimeException("Error while finding owners :" + e.getMessage());
        }

        return ResponseEntity.ok(owner);
    }

    // ---------------- GET PROPERTIES BY OWNER ----------------
    @GetMapping("/{id}/property")
    public ResponseEntity<List<Property>> getAllPropertyByOwnerId(@PathVariable int id) {

        log.info("GET /api/owner/{}/property called", id);

        List<Property> properties;

        try {
            properties = propertyRepo.getPropertiesByOwnerId(id);

            log.info("Properties fetched for ownerId={} count={}", id, properties.size());

        } catch (Exception e) {
            log.error("Error while getting properties for ownerId={}", id, e);
            throw new RuntimeException("Error while getting properties of owner :" + e.getMessage());
        }

        return ResponseEntity.ok(properties);
    }

    // ---------------- CREATE OWNER ----------------
    @PostMapping("/")
    public ResponseEntity<Owner> createOwner(@Valid @RequestBody OwnerDto dto) {

        log.info("POST /api/owner createOwner name={}, email={}", dto.getName(), dto.getEmail());

        Owner created;

        try {
            Owner o = Owner.builder()
                    .email(dto.getEmail())
                    .name(dto.getName())
                    .phone(dto.getPhone())
                    .build();

            created = ownerService.createOwner(o);

            log.info("Owner created successfully id={}", created.getId());

        } catch (Exception e) {
            log.error("Error while creating owner name={}", dto.getName(), e);
            throw new RuntimeException("error while creating owner: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------------- UPDATE OWNER ----------------
    @PutMapping("/{id}")
    public ResponseEntity<Owner> updateOwner(@PathVariable int id,
                                             @Valid @RequestBody OwnerDto dto) {

        log.info("PUT /api/owner/{} updateOwner called", id);

        Owner updated;

        try {
            Owner o = Owner.builder()
                    .id(id)
                    .email(dto.getEmail())
                    .name(dto.getName())
                    .phone(dto.getPhone())
                    .build();

            updated = ownerService.updateOwner(o);

            log.info("Owner updated successfully id={}", updated.getId());

        } catch (Exception e) {
            log.error("Error while updating owner id={}", id, e);
            throw new RuntimeException("error while updating owner: " + e.getMessage());
        }

        return ResponseEntity.ok(updated);
    }
}