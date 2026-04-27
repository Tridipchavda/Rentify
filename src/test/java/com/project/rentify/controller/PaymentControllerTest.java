package com.project.rentify.controller;
import com.project.rentify.models.*;
import com.project.rentify.models.dto.PaymentDto;
import com.project.rentify.models.enums.ContractStatus;
import com.project.rentify.models.enums.PaymentStatus;
import com.project.rentify.service.PaymentService;
import com.project.rentify.service.RentalContractService;
import com.project.rentify.service.TenantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private TenantService tenantService;

    @MockitoBean
    private RentalContractService rentalContractService;

    @Autowired
    private tools.jackson.databind.ObjectMapper objectMapper;

    @ParameterizedTest
    @CsvSource({
            "1000,1000,true",   // equal → success
            "800,1000,false",   // less → fail
            "1200,1000,false"   // greater → fail
    })
    @DisplayName("Verify Rent Amount and Payment Amount Logic while payment")
    void testCreatePaymentForRentAmountLogic(int paymentAmount,int rentAmount,boolean shouldSucceed) throws Exception {

        // Given
        Tenant tenant = new Tenant();
        tenant.setId(100);

        RentalContracts contract = new RentalContracts();
        contract.setId(10);
        contract.setTenant(tenant);
        contract.setStatus(ContractStatus.ACTIVE);
        contract.setRentAmount(rentAmount);

        Payment payment = Payment.builder()
                .id(100)
                .amount(paymentAmount)
                .paymentDate(LocalDate.now())
                .status(PaymentStatus.DUE)
                .tenant(tenant)
                .contract(contract)
                .build();

        PaymentDto dto = new PaymentDto();
        dto.setTenantId(100);
        dto.setContractId(10);
        dto.setAmount(paymentAmount);
        dto.setPaymentId(100);

        // Mocking
        when(tenantService.getTenantById(tenant.getId())).thenReturn(Optional.of(tenant));
        when(rentalContractService.getRentalContractById(contract.getId())).thenReturn(Optional.of(contract));

        if (shouldSucceed) {
            when(paymentService.createPayment(any(Payment.class))).thenReturn(payment);
        }

        // Perform request
        ResultActions result = mockMvc.perform(post("/api/payments/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // Assertions
        if (shouldSucceed) {
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.amount").value(paymentAmount))
                    .andExpect(jsonPath("$.status").value("DUE"));
        } else {
            result.andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.message").isNotEmpty());
        }
    }

    @Test
    void testGetAllPaymentByTenantId_success() throws Exception {

        // Given
        final int tenantId = 1;
        final boolean status = false;

        Tenant tenant = new Tenant();
        tenant.setId(tenantId);

        RentalContracts contract = new RentalContracts();
        contract.setId(10);
        contract.setTenant(tenant);

        Payment payment = Payment.builder()
                .id(1)
                .amount(5000)
                .status(PaymentStatus.DUE)
                .tenant(tenant)
                .contract(contract)
                .build();

        List<Payment> payments = List.of(payment);

        // Mocking
        when(paymentService.getPaymentByTenantId(tenantId, status))
                .thenReturn(payments);

        // When & Then
        mockMvc.perform(get("/api/payments/")
                        .param("tenant", String.valueOf(tenantId))
                        .param("pending", String.valueOf(status)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(5000))
                .andExpect(jsonPath("$[0].status").value("DUE"));
    }
}
