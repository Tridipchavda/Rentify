package com.project.rentify.controller;

import com.project.rentify.models.Property;
import com.project.rentify.models.RentalContracts;
import com.project.rentify.models.Tenant;
import com.project.rentify.models.dto.RentalContractDto;
import com.project.rentify.models.enums.ContractStatus;
import com.project.rentify.models.enums.PropertyStatus;
import com.project.rentify.service.PropertyService;
import com.project.rentify.service.RentalContractService;
import com.project.rentify.service.TenantService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RentalContractController.class)
class RentalContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RentalContractService rentalContractService;

    @MockitoBean
    private TenantService tenantService;

    @MockitoBean
    private PropertyService propertyService;

    @Autowired
    private ObjectMapper objectMapper;

    // -------------------- GET ALL (no search) --------------------

    @Test
    void testGetAllContracts_withoutSearch() throws Exception {

        RentalContracts contract = RentalContracts.builder()
                .Id(1)
                .rentAmount(5000)
                .status(ContractStatus.ACTIVE)
                .build();

        when(rentalContractService.getAllRentalContracts())
                .thenReturn(List.of(contract));

        mockMvc.perform(get("/api/contracts/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rentAmount").value(5000));
    }

    // -------------------- GET ALL (with search) --------------------

    @Test
    void testGetAllContracts_withSearch() throws Exception {

        RentalContracts contract = RentalContracts.builder()
                .Id(1)
                .rentAmount(5000)
                .build();

        when(rentalContractService.searchContract("john"))
                .thenReturn(List.of(contract));

        mockMvc.perform(get("/api/contracts/")
                        .param("name", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rentAmount").value(5000));
    }

    // -------------------- GET BY ID --------------------

    @Test
    void testGetContractById_success() throws Exception {

        RentalContracts contract = RentalContracts.builder()
                .Id(1)
                .rentAmount(5000)
                .build();

        when(rentalContractService.getRentalContractById(1))
                .thenReturn(Optional.of(contract));

        mockMvc.perform(get("/api/contracts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rentAmount").value(5000));
    }

    // -------------------- CREATE CONTRACT SUCCESS --------------------

    @Test
    void testCreateContract_success() throws Exception {

        Tenant tenant = new Tenant();
        tenant.setId(1);

        Property property = new Property();
        property.setId(10);
        property.setStatus(PropertyStatus.AVAILABLE);

        RentalContractDto dto = new RentalContractDto();
        dto.setTenantId(1);
        dto.setPropertyId(10);
        dto.setRentAmount(5000);
        dto.setStartDate(LocalDate.now());
        dto.setEndDate(LocalDate.now().plusMonths(6));

        RentalContracts saved = RentalContracts.builder()
                .Id(1)
                .rentAmount(5000)
                .status(ContractStatus.ACTIVE)
                .tenant(tenant)
                .property(property)
                .build();

        when(tenantService.getTenantById(1)).thenReturn(Optional.of(tenant));
        when(propertyService.getPropertyById(10)).thenReturn(Optional.of(property));
        when(rentalContractService.createRentalContract(any())).thenReturn(saved);

        mockMvc.perform(post("/api/contracts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rentAmount").value(5000));
    }

    // -------------------- CREATE CONTRACT FAILURE (occupied property) --------------------

    @Test
    void testCreateContract_propertyOccupied() throws Exception {

        Tenant tenant = new Tenant();
        tenant.setId(1);

        Property property = new Property();
        property.setId(10);
        property.setStatus(PropertyStatus.OCCUPIED);

        RentalContractDto dto = new RentalContractDto();
        dto.setTenantId(1);
        dto.setPropertyId(10);

        when(tenantService.getTenantById(1)).thenReturn(Optional.of(tenant));
        when(propertyService.getPropertyById(10)).thenReturn(Optional.of(property));

        mockMvc.perform(post("/api/contracts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    // -------------------- UPDATE CONTRACT --------------------

    @Test
    void testUpdateContract_success() throws Exception {

        Tenant tenant = new Tenant();
        tenant.setId(1);

        Property property = new Property();
        property.setId(10);

        RentalContractDto dto = new RentalContractDto();
        dto.setStartDate(LocalDate.now());
        dto.setTenantId(1);
        dto.setPropertyId(10);
        dto.setRentAmount(6000);

        RentalContracts updated = RentalContracts.builder()
                .Id(1)
                .rentAmount(6000)
                .startDate(LocalDate.now())
                .status(ContractStatus.ACTIVE)
                .tenant(tenant)
                .property(property)
                .build();

        when(tenantService.getTenantById(1)).thenReturn(Optional.of(tenant));
        when(propertyService.getPropertyById(10)).thenReturn(Optional.of(property));
        when(rentalContractService.updateRentalContract(any())).thenReturn(updated);

        mockMvc.perform(put("/api/contracts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rentAmount").value(6000));
    }

    // -------------------- ERROR CASE --------------------

    @Test
    void testGetContract_exception() throws Exception {

        when(rentalContractService.getRentalContractById(1))
                .thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(get("/api/contracts/1"))
                .andExpect(status().isInternalServerError());
    }
}