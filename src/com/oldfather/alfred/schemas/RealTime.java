package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by theoldfather on 4/14/17.
 */
public class RealTime {
    public String realtime_start;
    public String realtime_end;

    public RealTime(){}
    public RealTime(@JsonProperty("realtime_start")String realtime_start,
                    @JsonProperty("realtime_end")String realtime_end){
        this.realtime_start = realtime_start;
        this.realtime_end = realtime_end;
    }

    public boolean contains(String date){
        return (this.realtime_start.compareTo(date)<=0) & (0<=this.realtime_end.compareTo(date));
    }
}
