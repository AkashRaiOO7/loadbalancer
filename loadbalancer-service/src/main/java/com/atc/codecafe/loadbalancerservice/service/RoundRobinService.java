package com.atc.codecafe.loadbalancerservice.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RoundRobinService {
    private final AtomicInteger counter = new AtomicInteger(0);
    public String getNextServer(List<String> servers){
        if(servers.isEmpty()) {
            return null;
        }
        int index = Math.abs(counter.getAndIncrement() % servers.size());
        return servers.get(index);
    }
}
