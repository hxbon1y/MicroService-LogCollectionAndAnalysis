package com.robert.microservice.commandline;

public class CommandLine {
    private static final String PARAMETER_CONFIG_FILE = "-c";
    private static final String PARAMETER_SEARCH_STR = "-s";
    private static final String PARAMETER_USERNAME = "-u";
    private static final String PARAMETER_PWD = "-p";
    private static final String PARAMETER_OUTPUT_FILE = "-o";
    private static final String PARAMETER_CURRENT_TIME = "-cu";
    private static final String PARAMETER_START_TIME = "-st";
    private static final String PARAMETER_END_TIME = "-et";
    private static final String PARAMETER_DISABLE_EXCEPT = "-disex";
    private static final String PARAMETER_SERVICE_NAME = "-sv";
    private static final String PARAMETER_TIME_FORMAT = "-tf";
    private static final String PARAMETER_SOURCE_FILE = "-sf";

    private String valueOfConfigFile;
    private String valueOfSearchStr;
    private String valueOfUsername;
    private String valueOfPwd;
    private String valueOfOutputFile;
    private String valueOfCurrentTime;
    private String valueOfStartTime;
    private String valueOfEndTime;
    private String valueOfDisableExcept;
    private String valueOfServiceName;
    private String valueOfTimeFormat;
    private String valueOfSourceFile;

    public CommandLine(String[] args){
        parse(args);
    }

    private void parse(String[] args){


        for(int i = 0;i < args.length; i++)
        {
            String valueOfParameter = args[i];

            switch (valueOfParameter) {
                case PARAMETER_CONFIG_FILE:
                    valueOfConfigFile = args[i + 1];
                    break;
                case PARAMETER_USERNAME:
                    valueOfUsername = args[i + 1];
                    break;
                case PARAMETER_PWD:
                    valueOfPwd = args[i + 1];
                    break;
                case PARAMETER_OUTPUT_FILE:
                    valueOfOutputFile = args[i + 1];
                    break;
                case PARAMETER_SEARCH_STR:
                    valueOfSearchStr = args[i + 1];
                    break;
                case PARAMETER_START_TIME:
                    valueOfStartTime = args[i + 1];
                    break;
                case PARAMETER_END_TIME:
                    valueOfEndTime = args[i + 1];
                    break;
                case PARAMETER_TIME_FORMAT:
                    valueOfTimeFormat = args[i + 1];
                    break;
                case PARAMETER_CURRENT_TIME:
                    valueOfCurrentTime= PARAMETER_CURRENT_TIME;
                    break;
                case PARAMETER_DISABLE_EXCEPT:
                    valueOfDisableExcept = PARAMETER_DISABLE_EXCEPT;
                    break;
                case PARAMETER_SERVICE_NAME:
                    valueOfServiceName = args[i + 1];
                    break;
                case PARAMETER_SOURCE_FILE:
                    valueOfSourceFile = args[i + 1];
                    break;
                default:
                    if(valueOfParameter.startsWith("-"))
                    {
                        System.out.println("Unknown parameter \"" + valueOfParameter + "\"");
                        toolUsage();
                    }

            }


        }
    }

    private static void toolUsage(){
        System.out.println("Copyright @ Robert Hou, any issue, please contact me with 634935877@qq.com");

        System.out.println("Please use this tool on the machine that have SSH access permission to the target machine " +
                "on which the service was deployed, and use it like below:");
        System.out.println();
        System.out.println("LogCollecttion -c \"d:\\logCollection\\logCollection.config\" -u xxx -p xxx -s \"2019-03-09\" " +
                "-o \"d:\\logCollection\\log20190309.log\"");

        System.out.println();
        System.out.println("-c     the path of config file");
        System.out.println("-u     the username to have SSH access permission to target machine on which the service was deployed");
        System.out.println("-p     the pwd of the username");
        System.out.println("-s     the searched string, should be the date string which follow the data in original log");
        System.out.println("-o     the path of the file in which the log will be outputted");
        System.out.println("-cu    if to get the log happend on current time, please use this parameter");
        System.out.println("-st    startTime for log");
        System.out.println("-et    endTime for log");
        System.out.println("-disex disable the except strings in the config");
        System.out.println("-sv    service that will be only one to collect the log");
        System.out.println("-tf    time format in the log files that will be collected, the default format is YYYY-MM-DD HHmmss,SSS");
        System.out.println("-sf    the path of config file");
    }

    public String getValueOfConfigFile() {
        return valueOfConfigFile;
    }

    public void setValueOfConfigFile(String valueOfConfigFile) {
        this.valueOfConfigFile = valueOfConfigFile;
    }

    public String getValueOfSearchStr() {
        return valueOfSearchStr;
    }

    public void setValueOfSearchStr(String valueOfSearchStr) {
        this.valueOfSearchStr = valueOfSearchStr;
    }

    public String getValueOfUsername() {
        return valueOfUsername;
    }

    public void setValueOfUsername(String valueOfUsername) {
        this.valueOfUsername = valueOfUsername;
    }

    public String getValueOfPwd() {
        return valueOfPwd;
    }

    public void setValueOfPwd(String valueOfPwd) {
        this.valueOfPwd = valueOfPwd;
    }

    public String getValueOfOutputFile() {
        return valueOfOutputFile;
    }

    public void setValueOfOutputFile(String valueOfOutputFile) {
        this.valueOfOutputFile = valueOfOutputFile;
    }

    public String getValueOfCurrentTime() {
        return valueOfCurrentTime;
    }

    public void setValueOfCurrentTime(String valueOfCurrentTime) {
        this.valueOfCurrentTime = valueOfCurrentTime;
    }

    public String getValueOfStartTime() {
        return valueOfStartTime;
    }

    public void setValueOfStartTime(String valueOfStartTime) {
        this.valueOfStartTime = valueOfStartTime;
    }

    public String getValueOfEndTime() {
        return valueOfEndTime;
    }

    public void setValueOfEndTime(String valueOfEndTime) {
        this.valueOfEndTime = valueOfEndTime;
    }

    public String getValueOfDisableExcept() {
        return valueOfDisableExcept;
    }

    public void setValueOfDisableExcept(String valueOfDisableExcept) {
        this.valueOfDisableExcept = valueOfDisableExcept;
    }

    public String getValueOfServiceName() {
        return valueOfServiceName;
    }

    public void setValueOfServiceName(String valueOfServiceName) {
        this.valueOfServiceName = valueOfServiceName;
    }

    public String getValueOfTimeFormat() {
        return valueOfTimeFormat;
    }

    public void setValueOfTimeFormat(String valueOfTimeFormat) {
        this.valueOfTimeFormat = valueOfTimeFormat;
    }

    public String getValueOfSourceFile() {
        return valueOfSourceFile;
    }

    public void setValueOfSourceFile(String valueOfSourceFile) {
        this.valueOfSourceFile = valueOfSourceFile;
    }

}
