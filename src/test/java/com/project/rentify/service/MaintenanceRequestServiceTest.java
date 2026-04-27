package com.project.rentify.service;

import com.project.rentify.models.MaintainanceRequest;
import com.project.rentify.repository.MaintainanceRequestRepo;
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
class MaintenanceRequestServiceTest {

    @Mock
    private MaintainanceRequestRepo maintainanceRequestRepo;

    @InjectMocks
    private MaintenanceRequestService maintenanceRequestService;

    // ---------------- GET ALL ----------------
    @Test
    void testGetAllRequest() {
        MaintainanceRequest req = new MaintainanceRequest();
        when(maintainanceRequestRepo.findAll()).thenReturn(List.of(req));

        List<MaintainanceRequest> result = maintenanceRequestService.getAllRequest();

        assertEquals(1, result.size());
        verify(maintainanceRequestRepo, times(1)).findAll();
    }

    // ---------------- SEARCH ----------------
    @Test
    void testSearchRequest() {
        String search = "leak";
        MaintainanceRequest req = new MaintainanceRequest();

        when(maintainanceRequestRepo
                .searchByDescriptionContainingOrDescriptionContainingOrTenantNameOrPropertyName(
                        search, search, search, search))
                .thenReturn(List.of(req));

        List<MaintainanceRequest> result = maintenanceRequestService.searchRequest(search);

        assertEquals(1, result.size());
        verify(maintainanceRequestRepo, times(1))
                .searchByDescriptionContainingOrDescriptionContainingOrTenantNameOrPropertyName(
                        search, search, search, search);
    }

    // ---------------- GET BY ID ----------------
    @Test
    void testGetRequestById_found() {
        MaintainanceRequest req = new MaintainanceRequest();
        req.setId(1);

        when(maintainanceRequestRepo.findById(1)).thenReturn(Optional.of(req));

        Optional<MaintainanceRequest> result = maintenanceRequestService.getRequestById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
    }

    @Test
    void testGetRequestById_notFound() {
        when(maintainanceRequestRepo.findById(1)).thenReturn(Optional.empty());

        Optional<MaintainanceRequest> result = maintenanceRequestService.getRequestById(1);

        assertFalse(result.isPresent());
    }

    // ---------------- GET BY PROPERTY ----------------
    @Test
    void testGetRequestsForProperty() {
        MaintainanceRequest req = new MaintainanceRequest();

        when(maintainanceRequestRepo.findAllByPropertyId(10))
                .thenReturn(List.of(req));

        List<MaintainanceRequest> result =
                maintenanceRequestService.getRequestsForProperty(10);

        assertEquals(1, result.size());
        verify(maintainanceRequestRepo).findAllByPropertyId(10);
    }

    // ---------------- GET BY TENANT ----------------
    @Test
    void testGetRequestsForTenant() {
        MaintainanceRequest req = new MaintainanceRequest();

        when(maintainanceRequestRepo.findAllByTenantId(5))
                .thenReturn(List.of(req));

        List<MaintainanceRequest> result =
                maintenanceRequestService.getRequestsForTenant(5);

        assertEquals(1, result.size());
        verify(maintainanceRequestRepo).findAllByTenantId(5);
    }

    // ---------------- CREATE ----------------
    @Test
    void testCreateRequest() {
        MaintainanceRequest req = new MaintainanceRequest();

        when(maintainanceRequestRepo.save(req)).thenReturn(req);

        MaintainanceRequest result =
                maintenanceRequestService.createRequest(req);

        assertNotNull(result);
        verify(maintainanceRequestRepo).save(req);
    }

    // ---------------- UPDATE ----------------
    @Test
    void testUpdateRequest() {
        MaintainanceRequest req = new MaintainanceRequest();

        when(maintainanceRequestRepo.save(req)).thenReturn(req);

        MaintainanceRequest result =
                maintenanceRequestService.updateRequest(req);

        assertNotNull(result);
        verify(maintainanceRequestRepo).save(req);
    }
}