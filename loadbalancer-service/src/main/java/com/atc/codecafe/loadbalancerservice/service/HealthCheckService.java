package com.atc.codecafe.loadbalancerservice.service;

import com.atc.codecafe.loadbalancerservice.util.ServerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Service
public class HealthCheckService {
    @Autowired
    ServerRegistry serverRegistry;

    private final RestTemplate restTemplate = new RestTemplate();
    private final Set<String> unhealthy = new HashSet<>();
    @Scheduled(fixedRate = 10000)
    public void checkServers() {
        for(String server: serverRegistry.getServers()){ //loop to check for health servers
            try{
                String response = restTemplate.getForObject(server + "/ping", String.class);
                if ("OK".equals(response)) {
                    System.out.println("healthy server: "+ server);
                } else {
                    throw new RuntimeException("Unexpected ping response");
                }
            }catch (Exception e){
                System.out.println("Removing unhealthy server: "+ server);
                serverRegistry.removeServer(server);
                unhealthy.add(server);
            }
        }
        //loop to re-check unhealthy servers
        for(String server: new HashSet<>(unhealthy)){
            try{
                String response = restTemplate.getForObject(server + "/ping", String.class);
                if ("OK".equals(response)) {
                    System.out.println("Recovered server: " + server);
                    serverRegistry.registerServers(server);
                    unhealthy.remove(server);
                }
            }catch (Exception e){
            }
        }
    }
}
