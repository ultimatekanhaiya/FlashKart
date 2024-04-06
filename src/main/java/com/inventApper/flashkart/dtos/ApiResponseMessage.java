package com.inventApper.flashkart.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApiResponseMessage {

    private String message;
    private boolean success;
    private HttpStatus status;
}
