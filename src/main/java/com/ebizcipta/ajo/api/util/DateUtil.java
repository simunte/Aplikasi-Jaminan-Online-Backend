package com.ebizcipta.ajo.api.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
public class DateUtil {
    public String instantToString(Instant date, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format)
                .withZone(ZoneId.systemDefault());
        return dateTimeFormatter.format(date);
    }

    public Long stringToDate(String dateInput){
        String dateFinal;
        if (dateInput == null){
            dateFinal = "19900101";
        }else {
            dateFinal = dateInput;
        }
        Date result = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = formatter.parse(dateFinal);
            result = date;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result.getTime();
    }

    public Instant longToInstant(Long longValue){
        return Instant.ofEpochMilli(longValue);
    }

    public long countDays(Instant start , Instant end){
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String endStr = instantToString(end ,"MM/dd/yyyy HH:mm:ss");
        String startStr = instantToString(start, "MM/dd/yyyy HH:mm:ss");
        Date endDate = null;
        Date startDate = null;
        long diffDays = 0;
        try {
            endDate = format.parse(endStr);
            startDate = format.parse(startStr);
            long diff = startDate.getTime() - endDate.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return diffDays;
    }

    public Integer countDaysDif(Long startDate, Long endDate){
        long differences = startDate - endDate;
        long days = differences / (24 * 60 * 60 * 1000);
        return Math.toIntExact(days);
    }

    public Date addDays(Date date, Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return DateUtils.truncate(calendar.getTime(), Calendar.DATE);
    }
}
