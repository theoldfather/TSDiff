package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by theoldfather on 4/14/17.
 */
public class ReleaseSeries extends RealTimeSet {
    public List<SeriesS> seriess;

    public ReleaseSeries(){}
    public ReleaseSeries(@JsonProperty("realtime_start")String realtime_start,
                         @JsonProperty("realtime_end")String realtime_end,
                         @JsonProperty("order_by")String order_by,
                         @JsonProperty("sort_order")String sort_order,
                         @JsonProperty("count")int count,
                         @JsonProperty("offset")int offset,
                         @JsonProperty("limit")int limit,
                         @JsonProperty("seriess")List<SeriesS> seriess){
        super(realtime_start,realtime_end,order_by,sort_order,count,offset,limit);
        this.seriess = seriess;
    }
}
