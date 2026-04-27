package com.project.rentify.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TenantDto {
    @NotBlank(message = "Name field is mandatory")
    String name;

    @Email
    String email;

    @NotBlank(message = "Phone field is mandatory")
    String phone;

}
