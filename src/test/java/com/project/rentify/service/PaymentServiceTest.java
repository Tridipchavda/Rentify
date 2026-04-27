package com.project.rentify.service;

import com.project.rentify.models.Payment;
import com.project.rentify.models.enums.PaymentStatus;
import com.project.rentify.repository.PaymentRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepo paymentRepo;

    @InjectMocks
    private PaymentService paymentService;

    // ---------------- CREATE ----------------
    @Test
    void testCreatePayment() {
        Payment payment = new Payment();

        when(paymentRepo.save(payment)).thenReturn(payment);

        Payment result = paymentService.createPayment(payment);

        assertNotNull(result);
        verify(paymentRepo, times(1)).save(payment);
    }

    // ---------------- GET BY TENANT (ALL) ----------------
    @Test
    void testGetPaymentByTenantId_allPayments() {
        int tenantId = 1;

        when(paymentRepo.findAllByTenantId(tenantId))
                .thenReturn(List.of(new Payment()));

        List<Payment> result =
                paymentService.getPaymentByTenantId(tenantId, false);

        assertEquals(1, result.size());
        verify(paymentRepo).findAllByTenantId(tenantId);
        verify(paymentRepo, never())
                .findAllByStatusContainsAndTenantId(any(), anyInt());
    }

    // ---------------- GET BY TENANT (PENDING) ----------------
    @Test
    void testGetPaymentByTenantId_pendingPayments() {
        int tenantId = 1;

        when(paymentRepo.findAllByStatusContainsAndTenantId(
                PaymentStatus.DUE, tenantId))
                .thenReturn(List.of(new Payment()));

        List<Payment> result =
                paymentService.getPaymentByTenantId(tenantId, true);

        assertEquals(1, result.size());
        verify(paymentRepo).findAllByStatusContainsAndTenantId(
                PaymentStatus.DUE, tenantId);
        verify(paymentRepo, never())
                .findAllByTenantId(anyInt());
    }

    // ---------------- GET THIS MONTH ----------------
    @Test
    void testGetPaymentForThisMonth() {
        int tenantId = 1;

        // We don't hardcode dates because method uses LocalDate.now()
        when(paymentRepo.findAllByPaymentDateBetweenAndTenantId(
                any(LocalDate.class),
                any(LocalDate.class),
                eq(tenantId)))
                .thenReturn(List.of(new Payment()));

        List<Payment> result =
                paymentService.getPaymentForThisMonth(tenantId);

        assertEquals(1, result.size());

        verify(paymentRepo, times(1))
                .findAllByPaymentDateBetweenAndTenantId(
                        any(LocalDate.class),
                        any(LocalDate.class),
                        eq(tenantId));
    }
}