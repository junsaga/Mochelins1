package com.musthave0145.mochelins.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {
    //                               초, 분, 시,  일,  월, 년
    long basedTime[] = new long[]{1000, 60, 60, 24, 30, 12, 99};
    String basedTimeStr[] = new String[]{"초", "분", "시간", "일", "월", "년", ""};

    public String getDateCalculate(String UTC_Datetime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calendar = Calendar.getInstance();
        Date date = sdf.parse(UTC_Datetime);
        calendar.setTime(date);

        // 한국 시간으로 변환 (9시간 추가)
        calendar.add(Calendar.HOUR_OF_DAY, 9);

        // 현재 시간 가져오기
        Calendar currentCal = Calendar.getInstance();

        // 시간 차이 계산 (밀리초 단위)
        long timeDifferenceMillis = currentCal.getTimeInMillis() - calendar.getTimeInMillis();


        for(int i = 0; i < basedTime.length;i++){
            timeDifferenceMillis /= basedTime[i];
            if(basedTime[i+1] > timeDifferenceMillis){
                return (timeDifferenceMillis + 1) + basedTimeStr[i] + " 전";
            }
        }

        return "";
    }
}
