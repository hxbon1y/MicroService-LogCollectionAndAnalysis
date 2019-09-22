package com.robert.microservice.config;

import com.robert.microservice.config.model.Node;
import com.robert.microservice.config.model.Service;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogCollectionConfig {
    public List<Service> getServices() {
        return services;
    }

    public LogCollectionConfig(String configPath){
        parse(configPath);
    }

    private void parse(String configPath) {
        File configFile = new File(configPath);
        StringBuilder configStr = new StringBuilder();
        if(configFile.isFile() && configFile.exists()){
            InputStreamReader read = null;
            try {
                read = new InputStreamReader(new FileInputStream(configFile), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            try {
                while((lineTxt=bufferedReader.readLine()) != null){
                    configStr.append(lineTxt);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        services = new ArrayList<Service>();
        JSONArray jsonArray = new JSONArray(configStr.toString());
        int size = jsonArray.length();
        System.out.println("Size: " + size);
        for(int  i = 0; i < size; i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Service service = new Service();
            if(jsonObject.has("services")){
                String serviceName = jsonObject.getString("services");
                service.setService(serviceName);
            }
            List<Node> nodes = new ArrayList<Node>();
            if(jsonObject.has("nodes")){
                Node node = new Node();
                JSONArray nodeJSONArray = jsonObject.getJSONArray("nodes");
                for(Object nodeObject : nodeJSONArray){
                    JSONObject jsonNode = (JSONObject) nodeObject;
                    if (jsonNode.has("ip")){
                        node.setIp(jsonNode.getString("ip"));
                    }
                    if(jsonNode.has("paths")){
                        JSONArray pathJSONArray = jsonNode.getJSONArray("paths");
                        List<String> paths = new ArrayList<String>();
                        for(Object pathObject : pathJSONArray){
                            String path = (String) pathObject;
                            paths.add(path);
                        }
                        node.setPaths(paths);
                    }

                }
                nodes.add(node);
                service.setNodes(nodes);
            }
            if(jsonObject.has("excepts")){
                JSONArray pathJSONArray = jsonObject.getJSONArray("excepts");
                List<String> excepts = new ArrayList<String>();
                for(Object exceptObject : pathJSONArray){
                    String except = (String) exceptObject;
                    excepts.add(except);
                }
                service.setExcepts(excepts);
            }
            services.add(service);
            System.out.println(jsonObject.toString());
//            System.out.println("[" + i + "]name=" + jsonObject.get("name"));
//            System.out.println("[" + i + "]package_name=" + jsonObject.get("package_name"));
//            System.out.println("[" + i + "]check_version=" + jsonObject.get("check_version"));
        }
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    private List<Service> services;

}
