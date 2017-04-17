package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by theoldfather on 4/8/17.
 */
public class Observations extends RealTimeSet {
    public String observation_start;
    public String observation_end;
    public String units;
    public int output_type;
    public String file_type;
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
        super(realtime_start,realtime_end,order_by,sort_order,count,offset,limit);
        this.observation_start = observation_start;
        this.observation_end = observation_end;
        this.units = units;
        this.output_type = output_type;
        this.file_type = file_type;
        this.observations = observations;
    }

}