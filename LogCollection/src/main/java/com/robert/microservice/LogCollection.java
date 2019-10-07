package com.robert.microservice;

import com.jcraft.jsch.ChannelExec;
import com.robert.microservice.commandline.CommandLine;
import com.robert.microservice.commandline.CommandLineLightClone;
import com.robert.microservice.config.LogCollectionConfig;
import com.robert.microservice.config.model.Node;
import com.robert.microservice.config.model.Service;
import com.robert.microservice.sort.BucketSort;
import com.robert.microservice.sort.CountSort;
import com.robert.microservice.ssh.Execute;
import com.robert.microservice.time.TimeRange;
import com.robert.microservice.time.TimeRangeException;

import javax.naming.SizeLimitExceededException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LogCollection {

    public static void main(String[] args) {

        // 解析命令行: parse command line
        CommandLine commandLine = new CommandLine(args);

        // 解析配置文件: parse config file
        LogCollectionConfig logCollectionConfig = new LogCollectionConfig(commandLine.getValueOfConfigFile());

        if (commandLine.getValueOfCurrentTime() == null
                && commandLine.getValueOfStartTime() != null
                && commandLine.getValueOfEndTime() != null){
            fetchLogPerDay(commandLine, logCollectionConfig);
        } else {
            new CollectLogPerDay(commandLine,logCollectionConfig).start();
        }

    }

    private static void fetchLogPerDay(CommandLine commandLine, LogCollectionConfig logCollectionConfig) {
        List<CollectLogPerDay> collectLogPerDays = new ArrayList<CollectLogPerDay>();
        try {
            Map<String,List<String>> timeListPerDays = TimeRange.timeRange(commandLine.getValueOfStartTime(),
                    commandLine.getValueOfEndTime());
            for(Map.Entry<String,List<String>> entry : timeListPerDays.entrySet()){
                String date = entry.getKey();
                List<String> times = entry.getValue();
                CommandLineLightClone commandLineLightClone = new CommandLineLightClone(commandLine, date);
                StringBuilder sb = new StringBuilder();
                for(String time : times){
                    sb.append("\"");
                    sb.append(time);
                    sb.append("\" | ");
                }
                commandLineLightClone.setValueOfSearchStr(sb.substring(0,sb.length() - 3));
                System.out.println("commandLineLightClone.getValueOfSearchStr() = " + commandLineLightClone.getValueOfSearchStr());
                CollectLogPerDay collectLogPerDay = new CollectLogPerDay(commandLineLightClone,logCollectionConfig);
                collectLogPerDays.add(collectLogPerDay);
            }
            Set<CollectLogPerDay> busySet = new HashSet<CollectLogPerDay>();
            boolean finished = false;
            while(!finished){
                finished = true;
                for (CollectLogPerDay collectLogPerDay : collectLogPerDays){
                    if(!collectLogPerDay.isFinished()){
                        finished = false;
                        if (busySet.size() <2 && !busySet.contains(collectLogPerDay)){
                            collectLogPerDay.start();
                            busySet.add(collectLogPerDay);
                        }
                    } else if (busySet.contains(collectLogPerDay)){
                        busySet.remove(collectLogPerDay);
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (true){
                for(CollectLogPerDay collectLogPerDay : busySet){
                    if(collectLogPerDay.isFinished()){
                        busySet.remove(collectLogPerDay);
                        break;
                    }
                }
                if (busySet.size() == 0){
                    break;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " of main run here");
            Execute.releaseResource();
        } catch (TimeRangeException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    static class CollectLogPerDay extends Thread{
        private CommandLine commandLine;
        private LogCollectionConfig logCollectionConfig;
        private boolean finished;

        public CollectLogPerDay(CommandLine commandLine, LogCollectionConfig logCollectionConfig){
            this.commandLine = commandLine;
            this.logCollectionConfig = logCollectionConfig;
            this.finished = false;
        }

        public void run(){
            // 从目标机器获取原始日志形成一个list: get the original log from target machine and make it as a list
            String[] logs = fetchLogs(commandLine, logCollectionConfig);

            // 将原始日志的时间和所在行形成一个map: map the time of original log to itself as a map
            Map<Long, List<String>> time2Log = mapLogs(commandLine, logs);

            // 对map中的key进行从小到大的排序形成时间的List:sort the keys(time of log) of above map with AES and make it as a list
            List<Long> timeList = sortTimeOfLogs(time2Log);

            // 依照上述的时序list重新排序原始日志形成有序的日志list:resort the list of original log in according to the sort of list of time
            List<String> sortedLog = sortLogs(time2Log, timeList);

            // 依照上述有序的list输出日志到文件:output the log in sorted list of original log to a file
            outputLogs(commandLine, sortedLog);

            finished = true;

            System.out.println(Thread.currentThread().getName() + " of CollectLogPerDay run here");
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }
    }

    private static void outputLogs(CommandLine commandLine, List<String> sortedLog) {
        File writename = new File(commandLine.getValueOfOutputFile()); // 相对路径，如果没有则要建立一个新的output。txt文件
        System.out.println(Thread.currentThread().getName() + " output run here " + commandLine.getValueOfOutputFile());
        BufferedWriter out = null;
        try {
            writename.createNewFile(); // 创建新文件
            out = new BufferedWriter(new FileWriter(writename));
            for(String log : sortedLog) {
                out.write(log + "\n"); // \r\n即为换行
            }
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
            System.out.println(Thread.currentThread().getName() + " output run here");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writename != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static List<String> sortLogs(Map<Long, List<String>> time2Log, List<Long> timeList) {
        List<String> sortedLog = new ArrayList<String>();
        for(Long time : timeList){
            List<String> sameTimeLogs = time2Log.get(time);
            for(String log : sameTimeLogs){
                sortedLog.add(log);
            }
        }
        return sortedLog;
    }

    private static List<Long> sortTimeOfLogs(Map<Long, List<String>> time2Log) {
        Set<Long> timeSet =  time2Log.keySet();
        Long[] timeArray = new Long[timeSet.size()];
        timeArray = timeSet.toArray(timeArray);
        List<Long> timeList = null;
        try {
            timeArray = CountSort.sort(timeArray);
        } catch (SizeLimitExceededException e) {
            e.printStackTrace();
            timeList = new ArrayList<Long>();
            for(Long time : timeArray){
                timeList.add(time);
            }
            timeArray = null;
            timeList = BucketSort.sort(timeList,100);
        }
        if(timeList == null){
            timeList = new ArrayList<Long>();
            for(Long time : timeArray){
                timeList.add(time);
            }
            timeArray = null;
        }
        return timeList;
    }

    private static Map<Long, List<String>> mapLogs(CommandLine commandLine, String[] logs) {
        String defaultFormat = "yyyy-MM-dd HH:mm:ss,SSS";
        if(commandLine.getValueOfTimeFormat() != null){
            defaultFormat = commandLine.getValueOfTimeFormat();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(defaultFormat);
        Map<Long,List<String>> time2Log = new HashMap<Long, List<String>>();
        String sourceFilePosition = commandLine.getValueOfSourceFile();
        System.out.println("logs.length = " + logs.length);
        for(String log : logs){
//            System.out.println("log = " + log);
            String[] splitedLog = log.split(".log:|.zip:");
            String time;
            try {
                time = splitedLog[1].substring(0, defaultFormat.length());
            }catch (ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
                continue;
            }
            if("a".equals(sourceFilePosition)){
                log = splitedLog[1] + " - " + splitedLog[0] + (log.contains(".log:") ? ".log" : ".zip");
            } else if ("n".equals(sourceFilePosition)){
                log = splitedLog[1];
            }
            try {
                Date date = sdf.parse(time);
                Long timeLong = date.getTime();
                if(time2Log.get(timeLong) != null){
                    time2Log.get(timeLong).add(log);
                }else{
                    List<String> sameTimeLogs = new ArrayList<String>();
                    sameTimeLogs.add(log);
                    time2Log.put(timeLong,sameTimeLogs);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return time2Log;
    }

    private static String[] fetchLogs(CommandLine commandLine, LogCollectionConfig logCollectionConfig) {
        List<Service> services = logCollectionConfig.getServices();
        StringBuilder result = new StringBuilder();
        if (commandLine.getValueOfCurrentTime() != null){
            preHandleSearchStr(commandLine,services);
        }
        String serviceName = commandLine.getValueOfServiceName();
        if (serviceName != null && !serviceName.isEmpty()){
            for(Service service : services){
                if (!service.getService().equals(serviceName)){
                    System.out.println(service.getService() + " not matched the service "
                            + serviceName + ", will not collect the log for it");
                    continue;
                }
                collectLogPerService(commandLine, result, service);
            }
        } else {
            for(Service service : services){
                collectLogPerService(commandLine, result, service);
            }
        }

        return result.toString().split("\n");
    }

    private static void preHandleSearchStr(CommandLine commandLine, List<Service> services) {
        Service service = services.get(0);
        List<Node> nodes = service.getNodes();
        Node node = nodes.get(0);
        ChannelExec channelExec = Execute.getChannel(node.getIp(), commandLine.getValueOfUsername(), commandLine.getValueOfPwd());
        String currentTime = Execute.execute(channelExec, "date \"+%F %T\"");
        System.out.println("currentTime = "  + currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date currentDateTime = sdf.parse(currentTime);
            Calendar currentCalendarTime = Calendar.getInstance();
            currentCalendarTime.setTime(currentDateTime);
            commandLine.setValueOfSearchStr(sdf.format(currentDateTime).substring(0,15));
            int minute = currentCalendarTime.get(Calendar.MINUTE);
            if (minute % 10 < 5){
                Calendar earlierTime = (Calendar)currentCalendarTime.clone();
                earlierTime.add(Calendar.MINUTE, -10);
                commandLine.setValueOfSearchStr("\""  + sdf.format(earlierTime.getTime()).substring(0,15)
                 + "\"|" + "\"" + sdf.format(currentDateTime).substring(0,15) + "\"");
            }
            System.out.println("searchStr = " + commandLine.getValueOfSearchStr());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private static void collectLogPerService(CommandLine commandLine, StringBuilder result,
                                             Service service) {
        List<Node> nodes = service.getNodes();
        List<FetchLogPreNode> fetchLogPreNodes = new ArrayList<FetchLogPreNode>();
        for (Node node : nodes) {
            FetchLogPreNode fetchLogPreNode = new FetchLogPreNode(node,commandLine,service,result);
            fetchLogPreNodes.add(fetchLogPreNode);
            fetchLogPreNode.start();
        }
        boolean finished = false;
        while(!finished) {
            finished = true;
            for (FetchLogPreNode fetchLogPreNode : fetchLogPreNodes) {
                if (fetchLogPreNode.isBusy()){
                    finished = false;
                    break;
                }
            }
            if (!finished) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static StringBuilder constructCommand(CommandLine commandLine, String path,
                                                  Service service) {
        StringBuilder command = new StringBuilder();
        command.append("grep ");
        command.append("\"");
        command.append(commandLine.getValueOfSearchStr());
        command.append("\"");
        command.append(" ");
        command.append(path);
        command.append("*");
        String disableExcept = commandLine.getValueOfDisableExcept();
        if (disableExcept == null){
            List<String> excepts = service.getExcepts();
            if (excepts != null && !excepts.isEmpty()) {
                for (String except : excepts) {
                    command.append(" | grep -v \"" + except + "\"");
                }
            }
        }
        return command;
    }

    static class FetchLogPreNode extends Thread{
        private Node node;
        private CommandLine commandLine;
        private Service service;
        private StringBuilder result;
        private volatile boolean busy = true;

        public boolean isBusy() {
            return busy;
        }

        public FetchLogPreNode(Node node, CommandLine commandLine, Service service, StringBuilder result){
            this.node = node;
            this.commandLine = commandLine;
            this.service = service;
            this.result = result;
            this.busy = true;
        }

        public void run(){
            String ip = node.getIp();
            List<String> paths = node.getPaths();
            for (String path : paths) {
                StringBuilder command = constructCommand(commandLine, path, service);
//                System.out.println("command = " + command);
                ChannelExec channelExec = Execute.getChannel(ip, commandLine.getValueOfUsername(), commandLine.getValueOfPwd());
                result.append(Execute.execute(channelExec, command.toString()));
            }
            busy = false;
            System.out.println(Thread.currentThread().getName() + " of FetchLogPreNode run here");
        }
    }
}
