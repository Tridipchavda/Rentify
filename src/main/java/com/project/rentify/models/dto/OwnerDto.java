package com.project.rentify.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OwnerDto {

    @Email(message = "Invalid Email Input")
    String email;

    @NotBlank(message = "Name of Owner is Mandatory")
    @NotNull(message = "Field Name of Owner is missing")
    String name;

    @NotBlank(message = "Phone is mandatory")
    String phone;
}
