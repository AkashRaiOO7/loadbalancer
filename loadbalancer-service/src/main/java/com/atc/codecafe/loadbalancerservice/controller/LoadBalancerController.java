package com.atc.codecafe.loadbalancerservice.controller;

import com.atc.codecafe.loadbalancerservice.service.RoundRobinService;
import com.atc.codecafe.loadbalancerservice.util.ServerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class LoadBalancerController {
    @Autowired
    private ServerRegistry serverRegistry;
    @Autowired
    private RoundRobinService roundRobinService;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/register")
    public String registerService(@RequestBody String backendServiceURL) { //will be called by service to register the new backend servers(dynamic registration)
        serverRegistry.registerServers(backendServiceURL);
        return "Server Registered: " + backendServiceURL;
    }
    @GetMapping("/")
    public ResponseEntity<String> handleRequest() {
        List<String> servers = serverRegistry.getServers();
        String server = roundRobinService.getNextServer(servers);
        if(server == null)
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("No backend servers available");
        System.out.println("request will be handle by the server: "+ server);
        try {
            String response = restTemplate.getForObject(server, String.class);
            return ResponseEntity.ok(response);
        }catch (Exception exception){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while forwarding request to server: " + server);
        }
    }

}
