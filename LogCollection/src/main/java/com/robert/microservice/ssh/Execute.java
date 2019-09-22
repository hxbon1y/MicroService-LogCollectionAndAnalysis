package com.robert.microservice.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Execute {

    private static Map<String, Session> sessionsMap = new HashMap<String, Session>();

    private static JSch jsch = new JSch();

    public static ChannelExec getChannel(String host, String user, String pwd, int port){
        Session session = null;
        if (sessionsMap.containsKey(host)){
            session = sessionsMap.get(host);
        }
        if (session == null) {
            try {
                session = jsch.getSession(user, host, port);
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setTimeout(1000);
                session.setConfig(config);
                session.setPassword(pwd);
                if(!session.isConnected()){
                    session.connect();
                }
                sessionsMap.put(host, session);
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }

        ChannelExec channelExec = null;
        try {
            channelExec = (ChannelExec) session.openChannel("exec");
        } catch (JSchException e) {
            e.printStackTrace();
        }

        return channelExec;

    }

    public static ChannelExec getChannel(String host, String user, String pwd){
        return getChannel(host,user,pwd,22);
    }

    public static String execute(ChannelExec channelExec, String command){
        channelExec.setCommand(command);
        try {
            channelExec.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        InputStream in = null;
        try {
            in = channelExec.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(in));

        String tmpStr = "";
        StringBuffer result = new StringBuffer();
        try {
            while ((tmpStr = reader.readLine()) != null) {
                try {
                    result.append(new String(tmpStr.getBytes("gbk"), "UTF-8")).append("\n");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 关闭连接
     */
    public static void releaseResource(){
        for(Map.Entry<String, Session> entry : sessionsMap.entrySet()){
            entry.getValue().disconnect();
        }
    }

    public static void main(String[] args){
        ChannelExec channelExec = getChannel("192.168.124.128","root","toor");
        System.out.println(execute(channelExec,"df"));
        System.out.println(execute(getChannel("192.168.124.128","root","toor"),"df"));
        System.out.println("run here");
        releaseResource();
    }
}
