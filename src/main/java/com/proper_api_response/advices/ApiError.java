package com.proper_api_response.advices;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ApiError {

    private String message;
    private HttpStatus status;
}
