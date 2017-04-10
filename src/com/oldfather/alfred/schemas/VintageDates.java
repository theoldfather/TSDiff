package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by theoldfather on 4/9/17.
 */
public class VintageDates {
    public String realtime_start;
    public String realtime_end;
    public String order_by;
    public String sort_order;
    public String count;
    public String offset;
    public String limit;
    public List<String> vintage_dates;

    public VintageDates(){}

    public VintageDates(@JsonProperty("realtime_start")String realtime_start,
                        @JsonProperty("realtime_end")String realtime_end,
                        @JsonProperty("order_by")String order_by,
                        @JsonProperty("sort_order")String sort_order,
                        @JsonProperty("count")String count,
                        @JsonProperty("offset")String offset,
                        @JsonProperty("limit")String limit,
                        @JsonProperty("observations")List<String> vintage_dates){
        this.realtime_start = realtime_start;
        this.realtime_end = realtime_end;
        this.order_by = order_by;
        this.sort_order = sort_order;
        this.count = count;
        this.offset = offset;
        this.limit = limit;
        this.vintage_dates = vintage_dates;
    }
}
