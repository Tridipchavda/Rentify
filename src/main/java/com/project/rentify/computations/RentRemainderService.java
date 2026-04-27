package com.project.rentify.computations;

import com.project.rentify.models.Payment;
import com.project.rentify.models.RentalContracts;
import com.project.rentify.models.enums.ContractStatus;
import com.project.rentify.service.PaymentService;
import com.project.rentify.service.RentalContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RentRemainderService {

    @Autowired
    RentalContractService rentalContractService;

    @Autowired
    PaymentService paymentService;

//    @Scheduled(cron = "0 0 8 5 * *")
    @Scheduled(cron = "*/30 * * * * *")
    private void SendGentleRentRemainder() {

        try {
            List<RentalContracts> contracts = rentalContractService.getAllRentalContracts();

            for(RentalContracts c: contracts){
                if(c.getStatus() == ContractStatus.ACTIVE) {
                    List<Payment> p = paymentService.getPaymentForThisMonth(c.getTenant().getId());

                    if(!p.isEmpty() && p.getFirst().getAmount() == c.getRentAmount()) {

                    }else {
                        log.info("Sending Email for Rent Remainder to :"+c.getTenant().getEmail());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while sending remainders :"+e.getMessage());
        }
    }
}
