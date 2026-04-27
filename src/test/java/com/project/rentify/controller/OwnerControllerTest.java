package com.project.rentify.controller;

import com.project.rentify.models.Owner;
import com.project.rentify.models.Property;
import com.project.rentify.models.dto.OwnerDto;
import com.project.rentify.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.project.rentify.repository.PropertyRepo;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OwnerService ownerService;

    @MockitoBean
    private PropertyRepo propertyRepo;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------- GET ALL OWNERS (no search) --------------------

    @Test
    void testGetAllOwners_withoutSearch() throws Exception {

        Owner owner = Owner.builder()
                .id(1)
                .name("John")
                .email("john@test.com")
                .phone("12345")
                .build();

        when(ownerService.getAllOwner()).thenReturn(List.of(owner));

        mockMvc.perform(get("/api/owner/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    // -------------------- GET ALL OWNERS (with search) --------------------

    @Test
    void testGetAllOwners_withSearch() throws Exception {

        Owner owner = Owner.builder()
                .id(1)
                .name("John")
                .email("john@test.com")
                .phone("12345")
                .build();

        when(ownerService.searchOwner("john")).thenReturn(List.of(owner));

        mockMvc.perform(get("/api/owner/")
                        .param("name", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    // -------------------- GET OWNER BY ID --------------------

    @Test
    void testGetOwnerById_success() throws Exception {

        Owner owner = Owner.builder()
                .id(1)
                .name("John")
                .build();

        when(ownerService.getOwnerById(1)).thenReturn(Optional.of(owner));

        mockMvc.perform(get("/api/owner/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"));
    }

    // -------------------- GET OWNER PROPERTY --------------------

    @Test
    void testGetOwnerProperties_success() throws Exception {

        Property property = new Property();
        property.setId(10);

        when(propertyRepo.getPropertiesByOwnerId(1))
                .thenReturn(List.of(property));

        mockMvc.perform(get("/api/owner/1/property"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10));
    }

    // -------------------- CREATE OWNER --------------------

    @Test
    void testCreateOwner_success() throws Exception {

        OwnerDto dto = new OwnerDto();
        dto.setName("John");
        dto.setEmail("john@test.com");
        dto.setPhone("12345");

        Owner owner = Owner.builder()
                .id(1)
                .name("John")
                .email("john@test.com")
                .phone("12345")
                .build();

        when(ownerService.createOwner(any(Owner.class)))
                .thenReturn(owner);

        mockMvc.perform(post("/api/owner/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"));
    }

    // -------------------- UPDATE OWNER --------------------

    @Test
    void testUpdateOwner_success() throws Exception {

        OwnerDto dto = new OwnerDto();
        dto.setName("Updated");
        dto.setEmail("updated@test.com");
        dto.setPhone("99999");

        Owner updated = Owner.builder()
                .id(1)
                .name("Updated")
                .email("updated@test.com")
                .phone("99999")
                .build();

        when(ownerService.updateOwner(any(Owner.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/owner/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    // -------------------- ERROR CASE (optional for coverage) --------------------

    @Test
    void testGetOwnerById_exception() throws Exception {

        when(ownerService.getOwnerById(1))
                .thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/api/owner/1"))
                .andExpect(status().isInternalServerError());
    }
}