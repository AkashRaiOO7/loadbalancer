package com.atc.codecafe.service.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class StartupRegistration {

    @Value("${server.port}")
    private String port;

    @Value("${loadbalancer.url}")
    private String lbUrl;

    @PostConstruct
    public void registerWithLoadBalancer() {
        // backend server url
        String backendServiceUrl = "http://localhost:" + port;
        try{
            // Register with LB
            new RestTemplate().postForObject(lbUrl, backendServiceUrl, String.class);
            System.out.println("Registered with Load Balancer " + backendServiceUrl);
        }catch (Exception e){
            System.out.println("Failed to register with Load Balancer " + backendServiceUrl);
        }
    }
}
