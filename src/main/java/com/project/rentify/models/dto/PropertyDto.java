package com.project.rentify.models.dto;

import com.project.rentify.models.enums.PropertyStatus;
import com.project.rentify.models.enums.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PropertyDto {
    @NotBlank(message = "Property Name can't be blank")
    @NotNull(message = "Property name can't be null")
    String name;

    @Length(min = 3, message = "Address should be described properly")
    String address;

    @NotNull(message = "City is mandatory field")
    String city;

    PropertyType type;
    PropertyStatus status;
    int rentAmount;

    @NotNull(message = "Owner id is required")
    private int ownerId;

}
