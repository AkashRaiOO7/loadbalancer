package com.atc.codecafe.loadbalancerservice.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ServerRegistry {
    private final List<String> servers = new CopyOnWriteArrayList<>();
    public List<String> getServers(){
        return servers;
    }
    public void registerServers(String backendServiceURL){
        if(!servers.contains(backendServiceURL)){
            servers.add(backendServiceURL);
            System.out.println("Server Registered: " + backendServiceURL);
        }
    }
    public void removeServer(String backendServiceURL){
        if(servers.contains(backendServiceURL)){
            servers.remove(backendServiceURL);
            System.out.println("Server Removed: " + backendServiceURL);
        }
    }
}
