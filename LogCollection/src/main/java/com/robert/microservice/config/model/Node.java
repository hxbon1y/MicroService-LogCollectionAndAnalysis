package com.robert.microservice.config.model;

import java.util.List;

public class Node {
    private String ip;
    private List<String> paths;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

}
