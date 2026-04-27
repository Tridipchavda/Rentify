package com.project.rentify.controller;

import com.project.rentify.models.MaintainanceRequest;
import com.project.rentify.models.Tenant;
import com.project.rentify.models.dto.TenantDto;
import com.project.rentify.service.MaintenanceRequestService;
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
@RequestMapping("/api/tenant")
public class TenantController {

    @Autowired
    TenantService tenantService;

    @Autowired
    private MaintenanceRequestService maintenanceRequestService;

    // ---------------- GET ALL TENANTS ----------------
    @GetMapping("/")
    public ResponseEntity<List<Tenant>> getAllTenants(
            @RequestParam(name = "name", required = false) String search) {

        log.info("GET /api/tenant called with search={}", search);

        List<Tenant> tenants;

        try {
            if (search == null) {
                log.debug("Fetching all tenants");
                tenants = tenantService.getAllTenants();
            } else {
                log.debug("Searching tenants with keyword={}", search);
                tenants = tenantService.searchTenant(search);
            }

            log.info("Tenants fetched count={}", tenants.size());

        } catch (Exception e) {
            log.error("Error while fetching tenants search={}", search, e);
            throw new RuntimeException("error while fetching owners :" + e.getMessage());
        }

        return ResponseEntity.ok(tenants);
    }

    // ---------------- GET TENANT BY ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Tenant>> getTenant(@PathVariable int id) {

        log.info("GET /api/tenant/{} called", id);

        Optional<Tenant> tenant;

        try {
            tenant = tenantService.getTenantById(id);

            log.info("Tenant fetched id={} found={}", id, tenant.isPresent());

        } catch (Exception e) {
            log.error("Error while getting tenant id={}", id, e);
            throw new RuntimeException("error while getting property having id:" + id + ", " + e.getMessage());
        }

        return ResponseEntity.ok(tenant);
    }

    // ---------------- GET MAINTENANCE REQUESTS ----------------
    @GetMapping("/{id}/request")
    public ResponseEntity<List<MaintainanceRequest>> getMaintenanceRequestForTenant(@PathVariable int id) {

        log.info("GET /api/tenant/{}/request called", id);

        List<MaintainanceRequest> mr;

        try {
            mr = maintenanceRequestService.getRequestsForTenant(id);

            log.info("Maintenance requests fetched tenantId={}, count={}", id, mr.size());

        } catch (Exception e) {
            log.error("Error while fetching maintenance requests tenantId={}", id, e);
            throw new RuntimeException("error while getting request for particular property" + e.getMessage());
        }

        return ResponseEntity.ok(mr);
    }

    // ---------------- CREATE TENANT ----------------
    @PostMapping("/")
    public ResponseEntity<Tenant> createTenant(@Valid @RequestBody TenantDto dto) {

        log.info("POST /api/tenant createTenant name={}, email={}", dto.getName(), dto.getEmail());

        Tenant created;

        try {
            Tenant t = Tenant.builder()
                    .name(dto.getName())
                    .phone(dto.getPhone())
                    .email(dto.getEmail())
                    .build();

            created = tenantService.createTenant(t);

            log.info("Tenant created successfully id={}", created.getId());

        } catch (Exception e) {
            log.error("Error while creating tenant name={}", dto.getName(), e);
            throw new RuntimeException("error while creating tenant :" + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------------- UPDATE TENANT ----------------
    @PutMapping("/{id}")
    public ResponseEntity<Tenant> updateProperty(
            @Valid @RequestBody TenantDto dto,
            @PathVariable int id) {

        log.info("PUT /api/tenant/{} updateTenant called", id);

        Tenant updated;

        try {
            Tenant t = Tenant.builder()
                    .Id(id)
                    .name(dto.getName())
                    .phone(dto.getPhone())
                    .email(dto.getEmail())
                    .build();

            updated = tenantService.updateTenant(t);

            log.info("Tenant updated successfully id={}", updated.getId());

        } catch (Exception e) {
            log.error("Error while updating tenant id={}", id, e);
            throw new RuntimeException("error while updating tenant :" + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }
}