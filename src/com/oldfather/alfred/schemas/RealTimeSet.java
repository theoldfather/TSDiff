package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by theoldfather on 4/14/17.
 */
public class RealTimeSet extends RealTime{
    public String order_by;
    public String sort_order;
    public int count;
    public int offset;
    public int limit;

    public RealTimeSet(){}
    public RealTimeSet(@JsonProperty("realtime_start")String realtime_start,
                       @JsonProperty("realtime_end")String realtime_end,
                       @JsonProperty("order_by")String order_by,
                       @JsonProperty("sort_order")String sort_order,
                       @JsonProperty("count")int count,
                       @JsonProperty("offset")int offset,
                       @JsonProperty("limit")int limit){
        super(realtime_start,realtime_end);
        this.order_by = order_by;
        this.sort_order = sort_order;
        this.count = count;
        this.offset = offset;
        this.limit = limit;
    }
}
