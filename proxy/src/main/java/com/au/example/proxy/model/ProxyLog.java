package com.au.example.proxy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProxyLog {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "headers",
            joinColumns = {@JoinColumn(name = "header_id")},
            inverseJoinColumns = {@JoinColumn(name = "log_id")}
    )
    @NonNull
    private Set<Header> headers;

    @NonNull
    private String body;

    @NonNull
    private String requestUrl;

    @NonNull
    private HttpMethod httpMethod;


    @NonNull
    private HttpStatus status;
}
