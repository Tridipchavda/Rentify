package com.project.rentify.controller;

import com.project.rentify.models.MaintainanceRequest;
import com.project.rentify.models.Property;
import com.project.rentify.models.Tenant;
import com.project.rentify.models.dto.MaintenanceRequestDto;
import com.project.rentify.models.dto.UpdateRequestDto;
import com.project.rentify.models.enums.MaintainanceStatus;
import com.project.rentify.service.MaintenanceRequestService;
import com.project.rentify.service.PropertyService;
import com.project.rentify.service.TenantService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/request")
public class MaintenanceRequestController {

    @Autowired
    TenantService tenantService;

    @Autowired
    private MaintenanceRequestService maintenanceRequestService;

    @Autowired
    private PropertyService propertyService;

    // ---------------- GET ALL ----------------
    @GetMapping("/")
    public ResponseEntity<List<MaintainanceRequest>> getAllMaintenance(
            @RequestParam(name = "name", required = false) String search) {

        log.info("GET /api/request called with search={}", search);

        List<MaintainanceRequest> requests;

        try {
            if (search == null) {
                log.debug("Fetching all maintenance requests");
                requests = maintenanceRequestService.getAllRequest();
            } else {
                log.debug("Searching maintenance requests with keyword={}", search);
                requests = maintenanceRequestService.searchRequest(search);
            }

            log.info("Fetched {} maintenance requests", requests.size());

        } catch (Exception e) {
            log.error("Error while fetching maintenance requests search={}", search, e);
            throw new RuntimeException("error while fetching requests :" + e.getMessage());
        }

        return ResponseEntity.ok(requests);
    }

    // ---------------- GET BY ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<Optional<MaintainanceRequest>> getMaintenanceRequest(@PathVariable int id) {

        log.info("GET /api/request/{} called", id);

        Optional<MaintainanceRequest> request;

        try {
            request = maintenanceRequestService.getRequestById(id);

            log.info("Maintenance request fetched id={} found={}", id, request.isPresent());

        } catch (Exception e) {
            log.error("Error while getting maintenance request id={}", id, e);
            throw new RuntimeException("error while getting request having id:" + id + ", " + e.getMessage());
        }

        return ResponseEntity.ok(request);
    }

    // ---------------- CREATE ----------------
    @PostMapping("/")
    public ResponseEntity<MaintainanceRequest> createRequest(
            @Valid @RequestBody MaintenanceRequestDto dto) {

        log.info("POST /api/request createRequest tenantId={}, propertyId={}",
                dto.getTenantId(), dto.getPropertyId());

        MaintainanceRequest created;

        try {
            Tenant t = tenantService.getTenantById(dto.getTenantId())
                    .orElseThrow(() -> {
                        log.warn("Tenant not found id={}", dto.getTenantId());
                        return new IllegalArgumentException("Tenant not found with id :" + dto.getTenantId());
                    });

            Property p = propertyService.getPropertyById(dto.getPropertyId())
                    .orElseThrow(() -> {
                        log.warn("Property not found id={}", dto.getPropertyId());
                        return new IllegalArgumentException("Property not found with id :" + dto.getPropertyId());
                    });

            MaintainanceRequest mr = MaintainanceRequest.builder()
                    .RequestDate(LocalDate.now())
                    .description(dto.getDescription())
                    .status(MaintainanceStatus.PENDING)
                    .tenant(t)
                    .property(p)
                    .build();

            created = maintenanceRequestService.createRequest(mr);

            log.info("Maintenance request created successfully id={}", created.getId());

        } catch (Exception e) {
            log.error("Error while creating maintenance request tenantId={}, propertyId={}",
                    dto.getTenantId(), dto.getPropertyId(), e);

            throw new RuntimeException("error while creating request :" + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------------- UPDATE ----------------
    @PutMapping("/{id}")
    public ResponseEntity<MaintainanceRequest> updateProperty(
            @Valid @RequestBody UpdateRequestDto dto,
            @PathVariable int id) {

        log.info("PUT /api/request/{} update called with status={}", id, dto.getStatus());

        MaintainanceRequest updated;

        try {
            MaintainanceRequest mr = MaintainanceRequest.builder()
                    .Id(id)
                    .status(dto.getStatus())
                    .build();

            updated = maintenanceRequestService.updateRequest(mr);

            log.info("Maintenance request updated id={} newStatus={}", id, dto.getStatus());

        } catch (Exception e) {
            log.error("Error while updating maintenance request id={}", id, e);

            throw new RuntimeException("error while updating request :" + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }
}