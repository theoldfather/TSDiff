package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by theoldfather on 4/9/17.
 */
public class Series {
    public String realtime_start;
    public String realtime_end;
    public List<SeriesS> seriess;

    public Series(){

    }

    public Series(@JsonProperty("realtime_start")String realtime_start,
                  @JsonProperty("realtime_end")String realtime_end,
                  @JsonProperty("seriess")List<SeriesS> seriess){
        this.realtime_end = realtime_start;
        this.realtime_end = realtime_end;
        this.seriess = seriess;
    }
}
