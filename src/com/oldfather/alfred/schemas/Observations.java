package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by theoldfather on 4/8/17.
 */
public class Observations {
    public String realtime_start;
    public String realtime_end;
    public String observation_start;
    public String observation_end;
    public String units;
    public int output_type;
    public String file_type;
    public String order_by;
    public String sort_order;
    public int count;
    public int offset;
    public int limit;
    public List<Observation> observations;


    public Observations(){}
    public Observations(@JsonProperty("realtime_start")String realtime_start,
                        @JsonProperty("realtime_end")String realtime_end,
                        @JsonProperty("observation_start")String observation_start,
                        @JsonProperty("observation_end")String observation_end,
                        @JsonProperty("units")String units,
                        @JsonProperty("output_type")int output_type,
                        @JsonProperty("file_type")String file_type,
                        @JsonProperty("order_by")String order_by,
                        @JsonProperty("sort_order")String sort_order,
                        @JsonProperty("count")int count,
                        @JsonProperty("offset")int offset,
                        @JsonProperty("limit")int limit,
                        @JsonProperty("observations")List<Observation> observations){
        this.realtime_start = realtime_start;
        this.realtime_end = realtime_end;
        this.observation_start = observation_start;
        this.observation_end = observation_end;
        this.units = units;
        this.output_type = output_type;
        this.file_type = file_type;
        this.order_by = order_by;
        this.sort_order = sort_order;
        this.count = count;
        this.offset = offset;
        this.limit = limit;
        this.observations = observations;
    }

}