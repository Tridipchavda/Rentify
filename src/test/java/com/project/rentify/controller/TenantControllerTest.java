package com.project.rentify.controller;

import com.project.rentify.models.MaintainanceRequest;
import com.project.rentify.models.Tenant;
import com.project.rentify.models.dto.TenantDto;
import com.project.rentify.service.MaintenanceRequestService;
import com.project.rentify.service.TenantService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TenantController.class)
class TenantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TenantService tenantService;

    @MockitoBean
    private MaintenanceRequestService maintenanceRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------- GET ALL (no search) --------------------

    @Test
    void testGetAllTenants_withoutSearch() throws Exception {

        Tenant tenant = Tenant.builder()
                .Id(1)
                .name("John")
                .email("john@test.com")
                .phone("12345")
                .build();

        when(tenantService.getAllTenants())
                .thenReturn(List.of(tenant));

        mockMvc.perform(get("/api/tenant/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    // -------------------- GET ALL (with search) --------------------

    @Test
    void testGetAllTenants_withSearch() throws Exception {

        Tenant tenant = Tenant.builder()
                .Id(1)
                .name("John")
                .build();

        when(tenantService.searchTenant("john"))
                .thenReturn(List.of(tenant));

        mockMvc.perform(get("/api/tenant/")
                        .param("name", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    // -------------------- GET TENANT BY ID --------------------

    @Test
    void testGetTenantById_success() throws Exception {

        Tenant tenant = Tenant.builder()
                .Id(1)
                .name("John")
                .build();

        when(tenantService.getTenantById(1))
                .thenReturn(Optional.of(tenant));

        mockMvc.perform(get("/api/tenant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    // -------------------- GET MAINTENANCE REQUESTS --------------------

    @Test
    void testGetTenantMaintenanceRequests_success() throws Exception {

        MaintainanceRequest request = MaintainanceRequest.builder()
                .Id(1)
                .description("Leak issue")
                .build();

        when(maintenanceRequestService.getRequestsForTenant(1))
                .thenReturn(List.of(request));

        mockMvc.perform(get("/api/tenant/1/request"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Leak issue"));
    }

    // -------------------- CREATE TENANT --------------------

    @Test
    void testCreateTenant_success() throws Exception {

        TenantDto dto = new TenantDto();
        dto.setName("John");
        dto.setEmail("john@test.com");
        dto.setPhone("12345");

        Tenant tenant = Tenant.builder()
                .Id(1)
                .name("John")
                .email("john@test.com")
                .phone("12345")
                .build();

        when(tenantService.createTenant(any(Tenant.class)))
                .thenReturn(tenant);

        mockMvc.perform(post("/api/tenant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"));
    }

    // -------------------- UPDATE TENANT --------------------

    @Test
    void testUpdateTenant_success() throws Exception {

        TenantDto dto = new TenantDto();
        dto.setName("Updated John");
        dto.setEmail("updated@test.com");
        dto.setPhone("99999");

        Tenant updated = Tenant.builder()
                .Id(1)
                .name("Updated John")
                .email("updated@test.com")
                .phone("99999")
                .build();

        when(tenantService.updateTenant(any(Tenant.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/tenant/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Updated John"));
    }

    // -------------------- ERROR CASE --------------------

    @Test
    void testGetTenant_exception() throws Exception {

        when(tenantService.getTenantById(1))
                .thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/api/tenant/1"))
                .andExpect(status().isInternalServerError());
    }
}