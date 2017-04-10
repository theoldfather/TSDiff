package com.oldfather.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by theoldfather on 4/9/17.
 */
public class DateParser {
    Date date = null;

    public DateParser(String dateStr, String pattern){
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try{
            this.date = format.parse(dateStr);
        }catch(ParseException e){
            e.printStackTrace();
        }
    }

    public DateParser(String dateStr){
        this(dateStr,"YYYY-MM-DD");
    }

    public Date getDate(){
        return this.date;
    }

    public long getTime(){
        return this.date.getTime();
    }
}
