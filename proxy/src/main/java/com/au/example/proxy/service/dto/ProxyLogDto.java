package com.au.example.proxy.service.dto;

import com.au.example.proxy.model.Status;
import lombok.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProxyLogDto {

    private Long id;

    private Set<HeaderDto> headers;

    private String body;

    private String queryString;

    private String requestUrl;

    private HttpMethod httpMethod;

    private HttpStatus status;
}
