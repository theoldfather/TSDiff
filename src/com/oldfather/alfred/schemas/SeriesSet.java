package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by theoldfather on 4/9/17.
 */
public class SeriesSet extends RealTime {
    public List<SeriesS> seriess;

    public SeriesSet(){}
    public SeriesSet(@JsonProperty("realtime_start")String realtime_start,
                     @JsonProperty("realtime_end")String realtime_end,
                     @JsonProperty("seriess")List<SeriesS> seriess){
        super(realtime_start,realtime_end);
        this.seriess = seriess;
    }
}
