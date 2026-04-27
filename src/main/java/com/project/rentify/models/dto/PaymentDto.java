package com.project.rentify.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDto {

    @NotNull(message = "payment Id can't be null")
    int paymentId;

    @NotNull(message = "Amount can't be null")
    int amount;

    @NotNull(message = "Valid Contract should be provided")
    int contractId;

    @NotNull(message = "Valid Tenant should be provided")
    int tenantId;
}

