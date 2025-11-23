package com.atc.codecafe.service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {
    @Value("${server.port}")
    private String port;
    
    @GetMapping("/ping")
    public ResponseEntity<HttpStatus> health(){
        return ResponseEntity.ok(HttpStatus.valueOf(200));
    }
    
    @GetMapping("/")
    public String hello() {
        return "Hello from backend on port " + port;
    }
}
