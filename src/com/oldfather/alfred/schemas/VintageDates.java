package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by theoldfather on 4/9/17.
 */
public class VintageDates extends RealTimeSet {
    public List<String> vintage_dates;

    public VintageDates(){}
    public VintageDates(@JsonProperty("realtime_start")String realtime_start,
                        @JsonProperty("realtime_end")String realtime_end,
                        @JsonProperty("order_by")String order_by,
                        @JsonProperty("sort_order")String sort_order,
                        @JsonProperty("count")int count,
                        @JsonProperty("offset")int offset,
                        @JsonProperty("limit")int limit,
                        @JsonProperty("observations")List<String> vintage_dates){
        super(realtime_start,realtime_end,order_by,sort_order,count,offset,limit);
        this.vintage_dates = vintage_dates;
    }
}
