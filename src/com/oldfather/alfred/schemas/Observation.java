package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oldfather.datetime.DateParser;
import sun.util.calendar.LocalGregorianCalendar;

import java.util.Date;
import java.util.List;

/**
 * Created by theoldfather on 4/8/17.
 */
public class Observation implements Comparable<Observation> {
    public String realtime_start;
    public String realtime_end;
    public String date;
    public String value;

    public Observation(){}
    public Observation(@JsonProperty("realtime_start")String realtime_start,
                       @JsonProperty("realtime_end")String realtime_end,
                       @JsonProperty("date")String date,
                       @JsonProperty("value")String value){
        this.realtime_start = realtime_start;
        this.realtime_end = realtime_end;
        this.date = date;
        this.value = value;

    }

    public boolean contains(String date){
        return (this.realtime_start.compareTo(date)<=0) & (0<=this.realtime_end.compareTo(date));
    }

    public int compareTo(Observation o){
        Date a = (new DateParser(this.date)).getDate();
        Date b = (new DateParser(o.date)).getDate();
        return a.compareTo(b);
    }
}

