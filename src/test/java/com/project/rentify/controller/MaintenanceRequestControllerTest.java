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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MaintenanceRequestController.class)
public class MaintenanceRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MaintenanceRequestService maintenanceRequestService;

    @MockitoBean
    private TenantService tenantService;

    @MockitoBean
    private PropertyService propertyService;

    @Autowired
    private tools.jackson.databind.ObjectMapper objectMapper;

    @Test
    void testGetAllMaintenance_withoutSearch() throws Exception {

        MaintainanceRequest req = MaintainanceRequest.builder()
                .Id(1)
                .description("Leak issue")
                .status(MaintainanceStatus.PENDING)
                .build();

        when(maintenanceRequestService.getAllRequest())
                .thenReturn(List.of(req));

        mockMvc.perform(get("/api/request/"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllMaintenance_withSearch() throws Exception {

        MaintainanceRequest req = MaintainanceRequest.builder()
                .Id(1)
                .description("Pipe issue")
                .build();

        when(maintenanceRequestService.searchRequest("pipe"))
                .thenReturn(List.of(req));

        mockMvc.perform(get("/api/request/")
                        .param("name", "pipe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Pipe issue"));
    }

    @Test
    void testGetMaintenanceById() throws Exception {

        MaintainanceRequest req = MaintainanceRequest.builder()
                .Id(1)
                .description("Fan issue")
                .build();

        when(maintenanceRequestService.getRequestById(1))
                .thenReturn(Optional.of(req));

        mockMvc.perform(get("/api/request/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Fan issue"));
    }

    @Test
    void testGetMaintenanceById_exception() throws Exception {

        when(maintenanceRequestService.getRequestById(1))
                .thenThrow(new RuntimeException("error"));

        mockMvc.perform(get("/api/request/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateRequest_success() throws Exception {

        Tenant tenant = new Tenant();
        tenant.setId(1);

        Property property = new Property();
        property.setId(10);

        MaintainanceRequest saved = MaintainanceRequest.builder()
                .Id(1)
                .description("AC not working")
                .status(MaintainanceStatus.PENDING)
                .tenant(tenant)
                .property(property)
                .build();

        MaintenanceRequestDto dto = new MaintenanceRequestDto();
        dto.setTenantId(1);
        dto.setPropertyId(10);
        dto.setDescription("AC not working");

        when(tenantService.getTenantById(1)).thenReturn(Optional.of(tenant));
        when(propertyService.getPropertyById(10)).thenReturn(Optional.of(property));
        when(maintenanceRequestService.createRequest(any())).thenReturn(saved);

        mockMvc.perform(post("/api/request/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("AC not working"));
    }

    @Test
    void testCreateRequest_tenantNotFound() throws Exception {

        MaintenanceRequestDto dto = new MaintenanceRequestDto();
        dto.setTenantId(1);
        dto.setPropertyId(10);

        when(tenantService.getTenantById(1)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/request/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateRequest_propertyNotFound() throws Exception {

        Tenant tenant = new Tenant();
        tenant.setId(1);

        MaintenanceRequestDto dto = new MaintenanceRequestDto();
        dto.setTenantId(1);
        dto.setPropertyId(10);

        when(tenantService.getTenantById(1)).thenReturn(Optional.of(tenant));
        when(propertyService.getPropertyById(10)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/request/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testUpdateRequest_success() throws Exception {

        MaintainanceRequest updated = MaintainanceRequest.builder()
                .Id(1)
                .status(MaintainanceStatus.COMPLETED)
                .build();

        UpdateRequestDto dto = new UpdateRequestDto();
        dto.setStatus(MaintainanceStatus.COMPLETED);

        when(maintenanceRequestService.updateRequest(any()))
                .thenReturn(updated);

        mockMvc.perform(put("/api/request/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testUpdateRequest_exception() throws Exception {

        UpdateRequestDto dto = new UpdateRequestDto();
        dto.setStatus(MaintainanceStatus.COMPLETED);

        when(maintenanceRequestService.updateRequest(any()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/request/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }
}