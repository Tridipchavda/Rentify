package com.project.rentify.service;

import com.project.rentify.models.Payment;
import com.project.rentify.models.enums.PaymentStatus;
import com.project.rentify.repository.PaymentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    PaymentRepo paymentRepo;

    public Payment createPayment(Payment p) {
        return paymentRepo.save(p);
    }

    public List<Payment> getPaymentByTenantId(int id,boolean pending) {
        if (pending) {
            return paymentRepo.findAllByStatusContainsAndTenantId(PaymentStatus.DUE,id);
        }
        return paymentRepo.findAllByTenantId(id);
    }

    public List<Payment> getPaymentForThisMonth(int id) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(endDate.getDayOfMonth()).plusDays(1);

        return paymentRepo.findAllByPaymentDateBetweenAndTenantId(startDate,endDate,id);
    }
}
