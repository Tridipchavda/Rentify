package com.project.rentify.repository;

import com.project.rentify.models.Payment;
import com.project.rentify.models.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PaymentRepo extends JpaRepository<Payment,Integer> {
    List<Payment> findAllByTenantId(int id);
    List<Payment> findAllByStatusContainsAndTenantId(PaymentStatus status, int id);

    List<Payment> findAllByPaymentDateBetweenAndTenantId(LocalDate start,LocalDate end,int id);
}
