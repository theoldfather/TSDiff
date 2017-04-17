package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oldfather.datetime.DateParser;
import sun.util.calendar.LocalGregorianCalendar;

import java.util.Date;
import java.util.List;

/**
 * Created by theoldfather on 4/8/17.
 */
public class Observation extends RealTime implements Comparable<Observation> {
    public String date;
    public String value;

    public Observation(){}
    public Observation(@JsonProperty("realtime_start")String realtime_start,
                       @JsonProperty("realtime_end")String realtime_end,
                       @JsonProperty("date")String date,
                       @JsonProperty("value")String value){
        super(realtime_start,realtime_end);
        this.date = date;
        this.value = value;
    }

    public int compareTo(Observation o){
        Date a = (new DateParser(this.date)).getDate();
        Date b = (new DateParser(o.date)).getDate();
        return a.compareTo(b);
    }
}

