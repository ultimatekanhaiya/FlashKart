package com.inventApper.flashkart.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiResponseMessage {

    private String message;
    private boolean success;
    private HttpStatus status;
}
