package com.project.rentify.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MaintenanceRequestDto {
    String description;

    @NotNull(message = "Property Id required ")
    int propertyId;

    @NotNull(message = "Tenant Id required")
    int tenantId;

}
