package com.project.rentify.models;
import com.project.rentify.models.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Payment {
    @Id
    int id;
    LocalDate paymentDate;
    int amount;
    PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    RentalContracts contract;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    Tenant tenant;
}
