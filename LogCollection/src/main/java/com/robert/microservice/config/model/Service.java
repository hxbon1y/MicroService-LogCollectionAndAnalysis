package com.robert.microservice.config.model;

import java.util.List;

public class Service {
    private String service;
    private List<Node> nodes;
    private List<String> excepts;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<String> getExcepts() {
        return excepts;
    }

    public void setExcepts(List<String> excepts) {
        this.excepts = excepts;
    }
}
