package com.project.rentify.service;

import com.project.rentify.models.RentalContracts;
import com.project.rentify.models.Tenant;
import com.project.rentify.repository.RentalContractRepo;
import com.project.rentify.repository.TenantRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TenantServiceTest {

    @Mock
    private TenantRepo tenantRepo;

    @Mock
    private RentalContractRepo contractRepo;

    @InjectMocks
    private TenantService tenantService;

    // ---------------- GET ALL ----------------
    @Test
    void testGetAllTenants() {
        Tenant tenant = new Tenant();

        when(tenantRepo.findAll()).thenReturn(List.of(tenant));

        List<Tenant> result = tenantService.getAllTenants();

        assertEquals(1, result.size());
        verify(tenantRepo).findAll();
    }

    // ---------------- GET BY ID ----------------
    @Test
    void testGetTenantById_found() {
        Tenant tenant = new Tenant();
        tenant.setId(1);

        when(tenantRepo.findById(1)).thenReturn(Optional.of(tenant));

        Optional<Tenant> result = tenantService.getTenantById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testGetTenantById_notFound() {
        when(tenantRepo.findById(1)).thenReturn(Optional.empty());

        Optional<Tenant> result = tenantService.getTenantById(1);

        assertFalse(result.isPresent());
    }

    // ---------------- SEARCH ----------------
    @Test
    void testSearchTenant() {
        String search = "john";
        Tenant tenant = new Tenant();

        when(tenantRepo.searchByNameContainingOrEmailContainingOrPhoneContaining(
                search, search, search))
                .thenReturn(List.of(tenant));

        List<Tenant> result = tenantService.searchTenant(search);

        assertEquals(1, result.size());

        verify(tenantRepo)
                .searchByNameContainingOrEmailContainingOrPhoneContaining(
                        search, search, search);
    }

    // ---------------- GET CONTRACTS ----------------
    @Test
    void testGetContractForTenant() {
        int tenantId = 10;

        when(contractRepo.findAllByTenantId(tenantId))
                .thenReturn(List.of(new RentalContracts()));

        List<RentalContracts> result =
                tenantService.getContractForTenant(tenantId);

        assertEquals(1, result.size());
        verify(contractRepo).findAllByTenantId(tenantId);
    }

    // ---------------- CREATE ----------------
    @Test
    void testCreateTenant() {
        Tenant tenant = new Tenant();
        tenant.setName("John");

        when(tenantRepo.save(tenant)).thenReturn(tenant);

        Tenant result = tenantService.createTenant(tenant);

        assertNotNull(result);
        assertEquals("John", result.getName());

        verify(tenantRepo).save(tenant);
    }

    // ---------------- UPDATE ----------------
    @Test
    void testUpdateTenant() {
        Tenant tenant = new Tenant();
        tenant.setId(1);
        tenant.setName("Updated");

        when(tenantRepo.save(tenant)).thenReturn(tenant);

        Tenant result = tenantService.updateTenant(tenant);

        assertNotNull(result);
        assertEquals("Updated", result.getName());

        verify(tenantRepo).save(tenant);
    }
}