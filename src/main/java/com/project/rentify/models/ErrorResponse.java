package com.project.rentify.models;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;

@Data
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private LocalTime timestamp;
}
