package com.personal.common.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 * Created by saurabhagrawal on 21/06/16.
 */
public class DateCommonUtilities {
/*
Date patterns:
"dd-MMM-yyyy"  --7-Jun-2013   //If ‘M’ is 3 or more, then the month is interpreted as text, else number.
MMM dd, yyyy" --Jun 7, 2013
"E, MMM dd yyyy"   --Fri, June 7 2013
EEEE, MMM dd, yyyy HH:mm:ss a"   --Friday, Jun 7, 2013 12:10:56 PM
 */
   //add days to calendar
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
    //dd/M/yyyy"  , dd-M-yyyy hh:mm:ss ,"yyyy/MM/dd HH:mm:ss" ,"yyyy MMM dd"
    public static String getCurrentDateString(String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(new Date());
        return date;
    }
    public static Date convertStrintoDate(String format,String string_date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(string_date);
        return date;
    }
    public static Date convertCalendarTODate(Calendar calendar){
        Date date =  calendar.getTime();
        return date;
    }
    // CHECK THE IF DATE IS OF VALID FORMAT
    public static boolean isThisDateValid(String dateToValidate, String dateFormat) {

        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        //By default, SimpleDateFormat.setLenient() is set to true,
        // you should always turn it off to make your date validation more strictly.
        try {
            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
        } catch (ParseException e) {
            return false;
        }

        return true;
    }
}
