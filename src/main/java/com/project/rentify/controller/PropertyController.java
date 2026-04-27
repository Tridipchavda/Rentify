package com.project.rentify.controller;

import com.project.rentify.models.MaintainanceRequest;
import com.project.rentify.models.Owner;
import com.project.rentify.models.Property;
import com.project.rentify.models.dto.PropertyDto;
import com.project.rentify.repository.OwnerRepo;
import com.project.rentify.service.MaintenanceRequestService;
import com.project.rentify.service.OwnerService;
import com.project.rentify.service.PropertyService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/property")
public class PropertyController {

    private final PropertyService propertyService;
    private final OwnerRepo ownerRepo;
    private final MaintenanceRequestService maintenanceRequestService;
    private final OwnerService ownerService;

    public PropertyController(PropertyService propertyService,
                              OwnerRepo ownerRepo,
                              MaintenanceRequestService maintenanceRequestService,
                              OwnerService ownerService) {
        this.propertyService = propertyService;
        this.ownerRepo = ownerRepo;
        this.maintenanceRequestService = maintenanceRequestService;
        this.ownerService = ownerService;
    }

    // ---------------- GET ALL PROPERTIES ----------------
    @GetMapping("/")
    public ResponseEntity<List<Property>> getAllproperty(
            @RequestParam(name = "name", required = false) String search) {

        log.info("GET /api/property called with search={}", search);

        List<Property> properties;

        try {
            if (search == null) {
                log.debug("Fetching all properties");
                properties = propertyService.getAllProperty();
            } else {
                log.debug("Searching properties with keyword={}", search);
                properties = propertyService.searchProperty(search);
            }

            log.info("Properties fetched count={}", properties.size());

        } catch (Exception e) {
            log.error("Error while fetching properties search={}", search, e);
            throw new RuntimeException("error while fetching owners :" + e.getMessage());
        }

        return ResponseEntity.ok(properties);
    }

    // ---------------- GET PROPERTY BY ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Property>> getProperty(@PathVariable int id) {

        log.info("GET /api/property/{} called", id);

        Optional<Property> property;

        try {
            property = propertyService.getPropertyById(id);

            log.info("Property fetched id={} found={}", id, property.isPresent());

        } catch (Exception e) {
            log.error("Error while getting property id={}", id, e);
            throw new RuntimeException("error while getting property having id:" + id + ", " + e.getMessage());
        }

        return ResponseEntity.ok(property);
    }

    // ---------------- GET MAINTENANCE REQUESTS ----------------
    @GetMapping("/{id}/request")
    public ResponseEntity<List<MaintainanceRequest>> getMaintenanceRequestForProperty(@PathVariable int id) {

        log.info("GET /api/property/{}/request called", id);

        List<MaintainanceRequest> mr;

        try {
            mr = maintenanceRequestService.getRequestsForProperty(id);

            log.info("Maintenance requests fetched propertyId={}, count={}", id, mr.size());

        } catch (Exception e) {
            log.error("Error while getting maintenance requests propertyId={}", id, e);
            throw new RuntimeException("error while getting request for particular property" + e.getMessage());
        }

        return ResponseEntity.ok(mr);
    }

    // ---------------- CREATE PROPERTY ----------------
    @PostMapping("/")
    public ResponseEntity<Property> createProperty(@Valid @RequestBody PropertyDto dto) {

        log.info("POST /api/property createProperty name={}, ownerId={}", dto.getName(), dto.getOwnerId());

        Property created;

        try {
            Owner owner = ownerRepo.findById(dto.getOwnerId())
                    .orElseThrow(() -> {
                        log.warn("Owner not found id={}", dto.getOwnerId());
                        return new IllegalArgumentException("Owner not found with id: " + dto.getOwnerId());
                    });

            Property p = Property.builder()
                    .city(dto.getCity())
                    .address(dto.getAddress())
                    .name(dto.getName())
                    .rentAmount(dto.getRentAmount())
                    .status(dto.getStatus())
                    .type(dto.getType())
                    .owner(owner)
                    .build();

            created = propertyService.createProperty(p);

            log.info("Property created successfully id={}", created.getId());

        } catch (Exception e) {
            log.error("Error while creating property name={}", dto.getName(), e);
            throw new RuntimeException("error while creating owners :" + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------------- UPDATE PROPERTY ----------------
    @PutMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@Valid @RequestBody PropertyDto dto,
                                                   @PathVariable int id) {

        log.info("PUT /api/property/{} updateProperty called", id);

        Property updated;

        try {
            Owner owner = ownerService.getOwnerById(dto.getOwnerId())
                    .orElseThrow(() -> {
                        log.warn("Owner not found id={}", dto.getOwnerId());
                        return new IllegalArgumentException("Owner not found with id: " + dto.getOwnerId());
                    });

            Property p = Property.builder()
                    .id(id)
                    .city(dto.getCity())
                    .address(dto.getAddress())
                    .name(dto.getName())
                    .rentAmount(dto.getRentAmount())
                    .status(dto.getStatus())
                    .type(dto.getType())
                    .owner(owner)
                    .build();

            updated = propertyService.updateProperty(p);

            log.info("Property updated successfully id={}", updated.getId());

        } catch (Exception e) {
            log.error("Error while updating property id={}", id, e);
            throw new RuntimeException("error while creating owners :" + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }
}