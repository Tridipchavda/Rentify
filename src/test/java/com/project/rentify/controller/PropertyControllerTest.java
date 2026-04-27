package com.project.rentify.controller;

import com.project.rentify.models.MaintainanceRequest;
import com.project.rentify.models.Owner;
import com.project.rentify.models.Property;
import com.project.rentify.models.dto.PropertyDto;
import com.project.rentify.service.MaintenanceRequestService;
import com.project.rentify.service.OwnerService;
import com.project.rentify.service.PropertyService;
import com.project.rentify.repository.OwnerRepo;

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

@WebMvcTest(PropertyController.class)
class PropertyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PropertyService propertyService;

    @MockitoBean
    private OwnerRepo ownerRepo;

    @MockitoBean
    private MaintenanceRequestService maintenanceRequestService;

    @MockitoBean
    private OwnerService ownerService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------------- GET ALL (no search) ----------------

    @Test
    void testGetAllProperties_withoutSearch() throws Exception {

        Property property = Property.builder()
                .id(1)
                .name("Villa")
                .city("Ahmedabad")
                .build();

        when(propertyService.getAllProperty())
                .thenReturn(List.of(property));

        mockMvc.perform(get("/api/property/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Villa"));
    }

    // ---------------- GET ALL (with search) ----------------

    @Test
    void testGetAllProperties_withSearch() throws Exception {

        Property property = Property.builder()
                .id(1)
                .name("Villa")
                .build();

        when(propertyService.searchProperty("villa"))
                .thenReturn(List.of(property));

        mockMvc.perform(get("/api/property/")
                        .param("name", "villa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Villa"));
    }

    // ---------------- GET BY ID ----------------

    @Test
    void testGetPropertyById_success() throws Exception {

        Property property = Property.builder()
                .id(1)
                .name("House")
                .build();

        when(propertyService.getPropertyById(1))
                .thenReturn(Optional.of(property));

        mockMvc.perform(get("/api/property/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("House"));
    }

    // ---------------- GET MAINTENANCE REQUESTS ----------------

    @Test
    void testGetMaintenanceRequests_success() throws Exception {

        MaintainanceRequest request = MaintainanceRequest.builder()
                .Id(1)
                .description("Leak issue")
                .build();

        when(maintenanceRequestService.getRequestsForProperty(1))
                .thenReturn(List.of(request));

        mockMvc.perform(get("/api/property/1/request"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Leak issue"));
    }

    // ---------------- CREATE PROPERTY ----------------

    @Test
    void testCreateProperty_success() throws Exception {

        Owner owner = new Owner();
        owner.setId(1);

        PropertyDto dto = new PropertyDto();
        dto.setOwnerId(1);
        dto.setName("Villa");
        dto.setCity("Ahmedabad");
        dto.setAddress("Street 1");

        Property saved = Property.builder()
                .id(1)
                .name("Villa")
                .city("Ahmedabad")
                .owner(owner)
                .build();

        when(ownerRepo.findById(1)).thenReturn(Optional.of(owner));
        when(propertyService.createProperty(any())).thenReturn(saved);

        mockMvc.perform(post("/api/property/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Villa"));
    }

    // ---------------- UPDATE PROPERTY ----------------

    @Test
    void testUpdateProperty_success() throws Exception {

        Owner owner = new Owner();
        owner.setId(1);

        PropertyDto dto = new PropertyDto();
        dto.setOwnerId(1);
        dto.setName("Updated Villa");
        dto.setCity("Ahmedabad");

        Property updated = Property.builder()
                .id(1)
                .name("Updated Villa")
                .city("Ahmedabad")
                .owner(owner)
                .build();

        when(ownerService.getOwnerById(1)).thenReturn(Optional.of(owner));
        when(propertyService.updateProperty(any())).thenReturn(updated);

        mockMvc.perform(put("/api/property/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Updated Villa"));
    }

    // ---------------- ERROR CASE (optional for coverage) ----------------

    @Test
    void testGetProperty_exception() throws Exception {

        when(propertyService.getPropertyById(1))
                .thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/api/property/1"))
                .andExpect(status().isInternalServerError());
    }
}