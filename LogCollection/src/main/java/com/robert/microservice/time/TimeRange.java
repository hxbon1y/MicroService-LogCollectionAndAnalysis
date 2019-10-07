package com.robert.microservice.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeRange {

    private static long MILLSECONDS_PER_DAY = 1000 * 60 * 60 * 24;

    private static SimpleDateFormat yyyy_MM_dd_HH_mm_ss_Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat yyyy_MM_dd_HH_mm_Format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat yyyy_MM_dd_HH_Format = new SimpleDateFormat("yyyy-MM-dd HH");
    private static SimpleDateFormat yyyy_MM_dd_Format = new SimpleDateFormat("yyyy-MM-dd");

    public static Map<String, List<String>> timeRange(String startTime, String endTime) throws TimeRangeException{

        List<String> timeList = new ArrayList<String>();
        Set<String> timeSet = new HashSet<String>();

        Date startDate = parseTime(startTime);
        Date endDate = parseTime(endTime);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        String timeStr = yyyy_MM_dd_HH_mm_ss_Format.format(startCalendar.getTime());
        timeList.add(timeStr);
        timeSet.add(timeStr);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        timeStr = yyyy_MM_dd_HH_mm_ss_Format.format(endCalendar.getTime());
        timeList.add(timeStr);
        timeSet.add(timeStr);

        Calendar startCalendarClone = Calendar.getInstance();
        startCalendarClone.setTime(startCalendar.getTime());
        handleStartTime(timeList, timeSet, startCalendar, endCalendar);
        handleEndTime(timeList, timeSet, startCalendarClone, endCalendar);

        Map<String,List<String>> dateToTimeMap = new HashMap<String,List<String>>();
        Set<String> dates = dateToTimeMap.keySet();
        for(String time : timeList){
            try {
                String date = yyyy_MM_dd_Format.format(yyyy_MM_dd_Format.parse(time));
                if (dates.contains(date)){
                    dateToTimeMap.get(date).add(time);
                }else{
                    List<String> timeListPerDate = new ArrayList<String>();
                    timeListPerDate.add(time);
                    dateToTimeMap.put(date,timeListPerDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                throw new TimeRangeException("failed to parse " + time);
            }
        }

        for(Map.Entry<String, List<String>> entry : dateToTimeMap.entrySet()){
            System.out.print("date = " + entry.getKey());
            System.out.print(" elements are [");
            for(String time : entry.getValue()){
                System.out.print(time + ",");
            }
            System.out.println("]");
        }

        return dateToTimeMap;
    }

    private static void handleEndTime(List<String> timeList, Set<String> timeSet, Calendar startCalendar, Calendar endCalendar) {
        String timeStr;
        int endSecond = endCalendar.get(Calendar.SECOND);
        while (endSecond != 59 && endCalendar.compareTo(startCalendar) > 0){
            timeStr = yyyy_MM_dd_HH_mm_ss_Format.format(endCalendar.getTime());
            if (!timeSet.contains(timeStr)) {
                timeList.add(timeStr);
                timeSet.add(timeStr);
            }
            endCalendar.add(Calendar.SECOND,-1);
            endSecond = endCalendar.get(Calendar.SECOND);
        }

        int endMinute;
        do{
            timeStr = yyyy_MM_dd_HH_mm_Format.format(endCalendar.getTime());
            if (!timeSet.contains(timeStr)) {
                timeList.add(timeStr);
                timeSet.add(timeStr);
            }
            endCalendar.add(Calendar.MINUTE,-1);
            endMinute = endCalendar.get(Calendar.MINUTE);
        } while (endMinute != 59 && endCalendar.compareTo(startCalendar) > 0);

        int endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        do{
            timeStr = yyyy_MM_dd_HH_Format.format(endCalendar.getTime());
            if (!timeSet.contains(timeStr)) {
                timeList.add(timeStr);
                timeSet.add(timeStr);
            }
            endCalendar.add(Calendar.HOUR_OF_DAY,-1);
            endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
        } while (endHour != 23 && endCalendar.compareTo(startCalendar) > 0);

        int endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
        while (endCalendar.compareTo(startCalendar) > 0
                && (endCalendar.getTime().getTime() - startCalendar.getTime().getTime()) > MILLSECONDS_PER_DAY){
            timeStr = yyyy_MM_dd_Format.format(endCalendar.getTime());
            if (!timeSet.contains(timeStr)) {
                timeList.add(timeStr);
                timeSet.add(timeStr);
            }
            endCalendar.add(Calendar.DAY_OF_MONTH,-1);
        }
    }

    private static void handleStartTime(List<String> timeList, Set<String> timeSet, Calendar startCalendar, Calendar endCalendar) {
        String timeStr;
        int startSecond = startCalendar.get(Calendar.SECOND);
        while (startSecond != 0 && startCalendar.compareTo(endCalendar) < 0){
            timeStr = yyyy_MM_dd_HH_mm_ss_Format.format(startCalendar.getTime());
            if (!timeSet.contains(timeStr)) {
                timeList.add(timeStr);
                timeSet.add(timeStr);
            }
            startCalendar.add(Calendar.SECOND,1);
            startSecond = startCalendar.get(Calendar.SECOND);
        }

        int startMinute;
        do {
            timeStr = yyyy_MM_dd_HH_mm_Format.format(startCalendar.getTime());
            timeList.add(timeStr);
            timeSet.add(timeStr);
            startCalendar.add(Calendar.MINUTE,1);
            startMinute = startCalendar.get(Calendar.MINUTE);
        }while (startMinute != 0 && startCalendar.compareTo(endCalendar) < 0);

        int startHour;
        do{
            timeStr = yyyy_MM_dd_HH_Format.format(startCalendar.getTime());
            timeList.add(timeStr);
            timeSet.add(timeStr);
            startCalendar.add(Calendar.HOUR_OF_DAY,1);
            startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
        } while (startHour != 0 && startCalendar.compareTo(endCalendar) < 0);

        while (startCalendar.compareTo(endCalendar) < 0
                && (endCalendar.getTime().getTime() - startCalendar.getTime().getTime()) > MILLSECONDS_PER_DAY){
            timeStr = yyyy_MM_dd_Format.format(startCalendar.getTime());
            timeList.add(timeStr);
            timeSet.add(timeStr);
            startCalendar.add(Calendar.DAY_OF_MONTH,1);
        }
    }

    private static Date parseTime(String time) throws TimeRangeException {
        String defaultFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(defaultFormat);
        Date date;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new TimeRangeException("failed to parse " + time
                    + ",  it's not follow the format " + defaultFormat);
        }
        return date;
    }
}
