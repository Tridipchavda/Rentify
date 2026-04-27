package com.project.rentify.models.dto;

import com.project.rentify.models.enums.MaintainanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestDto {
    @NotNull(message = "while updating status can't be null")
    MaintainanceStatus status;
}
