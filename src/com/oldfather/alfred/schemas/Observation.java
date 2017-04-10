package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by theoldfather on 4/8/17.
 */
public class ObservationJaxb {
    public String realtime_start;
    public String realtime_end;
    public String date;
    public String value;

    public ObservationJaxb(){}
    public ObservationJaxb(@JsonProperty("realtime_start")String realtime_start,
                           @JsonProperty("realtime_end")String realtime_end,
                           @JsonProperty("date")String date,
                           @JsonProperty("value")String value){
        this.realtime_start = realtime_start;
        this.realtime_end = realtime_end;
        this.date = date;
        this.value = value;

    }
}

