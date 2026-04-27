package com.project.rentify.controller;

import com.project.rentify.models.Payment;
import com.project.rentify.models.RentalContracts;
import com.project.rentify.models.Tenant;
import com.project.rentify.models.dto.PaymentDto;
import com.project.rentify.models.enums.ContractStatus;
import com.project.rentify.models.enums.PaymentStatus;
import com.project.rentify.service.PaymentService;
import com.project.rentify.service.RentalContractService;
import com.project.rentify.service.TenantService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @Autowired
    TenantService tenantService;

    @Autowired
    RentalContractService rentalContractService;

    // ---------------- CREATE PAYMENT ----------------
    @PostMapping("/")
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentDto dto) {

        log.info("POST /api/payments createPayment tenantId={}, contractId={}, amount={}",
                dto.getTenantId(), dto.getContractId(), dto.getAmount());

        Payment create;

        try {
            Tenant t = tenantService.getTenantById(dto.getTenantId())
                    .orElseThrow(() -> {
                        log.warn("Tenant not found id={}", dto.getTenantId());
                        return new IllegalArgumentException("Tenant not found with id :" + dto.getTenantId());
                    });

            RentalContracts rc = rentalContractService.getRentalContractById(dto.getContractId())
                    .orElseThrow(() -> {
                        log.warn("Contract not found id={}", dto.getContractId());
                        return new IllegalArgumentException("Contract not found with id :" + dto.getContractId());
                    });

            // ---------------- BUSINESS VALIDATIONS ----------------

            if (rc.getStatus() != ContractStatus.ACTIVE) {
                log.warn("Payment blocked: contract not active contractId={}", rc.getId());
                throw new RuntimeException("Contract is not currently active");
            }

            if (rc.getTenant().getId() != t.getId()) {
                log.warn("Payment blocked: tenant mismatch tenantId={}, contractId={}",
                        t.getId(), rc.getId());
                throw new RuntimeException("Contract has not being generated between Tenant and Property");
            }

            if (dto.getAmount() != rc.getRentAmount()) {
                log.warn("Payment amount mismatch expected={}, actual={}",
                        rc.getRentAmount(), dto.getAmount());

                throw new RuntimeException(
                        "Rent Amount for Payment didn't match Expected :" + rc.getRentAmount()
                                + " Actual :" + dto.getAmount());
            }

            Payment p = Payment.builder()
                    .id(dto.getPaymentId())
                    .paymentDate(LocalDate.now())
                    .amount(dto.getAmount())
                    .status(PaymentStatus.DUE)
                    .tenant(t)
                    .contract(rc)
                    .build();

            create = paymentService.createPayment(p);

            log.info("Payment created successfully paymentId={}, tenantId={}, contractId={}",
                    create.getId(), t.getId(), rc.getId());

        } catch (Exception e) {
            log.error("Error while performing payment tenantId={}, contractId={}",
                    dto.getTenantId(), dto.getContractId(), e);

            throw new RuntimeException("error while performing payment :" + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(create);
    }

    // ---------------- GET PAYMENTS BY TENANT ----------------
    @GetMapping("/")
    public ResponseEntity<List<Payment>> getAllPaymentByTenantId(
            @RequestParam(name = "tenant") int tenant,
            @RequestParam(name = "pending", required = false) boolean status) {

        log.info("GET /api/payments tenantId={}, pending={}", tenant, status);

        List<Payment> payments;

        try {
            payments = paymentService.getPaymentByTenantId(tenant, status);

            log.info("Payments fetched tenantId={}, count={}", tenant, payments.size());

        } catch (Exception e) {
            log.error("Error while fetching payments tenantId={}", tenant, e);
            throw new RuntimeException("error while getting payment records :" + e.getMessage());
        }

        return ResponseEntity.ok(payments);
    }
}