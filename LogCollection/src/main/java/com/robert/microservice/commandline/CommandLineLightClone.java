package com.robert.microservice.commandline;

public class CommandLineLightClone extends CommandLine{
    private String valueOfSearchStr;

    public CommandLineLightClone(String[] args) {
        super(args);
    }

    public CommandLineLightClone(CommandLine commmandLine, String dateString) {
        super(new String[]{});
        super.setValueOfConfigFile(commmandLine.getValueOfConfigFile());
        super.setValueOfDisableExcept(commmandLine.getValueOfDisableExcept());
        super.setValueOfEndTime(commmandLine.getValueOfEndTime());
        super.setValueOfOutputFile(commmandLine.getValueOfOutputFile().split("\\.log|\\.txt")[0]
                + "_" + dateString + ".log");
        super.setValueOfPwd(commmandLine.getValueOfPwd());
        super.setValueOfServiceName(commmandLine.getValueOfServiceName());
        super.setValueOfSourceFile(commmandLine.getValueOfSourceFile());
        super.setValueOfStartTime(commmandLine.getValueOfStartTime());
        super.setValueOfTimeFormat(commmandLine.getValueOfTimeFormat());
        super.setValueOfUsername(commmandLine.getValueOfUsername());
    }

    @Override
    public String getValueOfSearchStr() {
        return valueOfSearchStr;
    }

    @Override
    public void setValueOfSearchStr(String valueOfSearchStr) {
        this.valueOfSearchStr = valueOfSearchStr;
    }

}
