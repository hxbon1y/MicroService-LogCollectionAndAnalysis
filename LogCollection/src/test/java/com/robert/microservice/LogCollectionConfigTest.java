package com.robert.microservice;

import com.robert.microservice.config.LogCollectionConfig;

public class LogCollectionConfigTest {

    private LogCollectionConfig logCollectionConfig;

    @org.junit.Before
    public void setUp() throws Exception {
        logCollectionConfig = new LogCollectionConfig("C:\\Users\\bailu\\IdeaProjects\\LogCollection\\src\\test\\java\\com\\robert\\microservice\\logCollection.config");
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void getServices1() {
        logCollectionConfig.toString();

    }

    @org.junit.Test
    public void setServices1() {
    }
}