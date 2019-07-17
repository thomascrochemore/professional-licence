package com.api.p52.utilities;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date plus(Date date,int field,int amount){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field,amount);
        return c.getTime();
    }
}
