package com.project.rentify.models.dto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RentalContractDto {

    @NotNull(message = "Start Date should be there in contract")
    LocalDate startDate;
    LocalDate endDate;

    @NotNull(message = "Rent Amount can't be null")
    int rentAmount;
    int latePenaltyFees;

    @NotNull(message = "Property should be assigned to Contract")
    int propertyId;

    @NotNull(message = "Tenant can't be null in Contract")
    int tenantId;


}
