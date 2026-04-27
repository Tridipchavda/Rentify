package com.project.rentify.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.rentify.models.enums.ContractStatus;
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
public class RentalContracts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int Id;
    LocalDate startDate;
    LocalDate endDate;
    int rentAmount;
    int latePenaltyFees;
    ContractStatus status;


    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "property_id")
    Property property;


    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "tenant_id")
    Tenant tenant;

}
