package com.oldfather.alfred.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by theoldfather on 4/14/17.
 */
public class Release extends RealTime{
    public int id;
    public String name;
    public boolean press_release;
    public String link;
    public String notes;

    public Release(){}

    /*public Release(@JsonProperty("realtime_start")String realtime_start,
                   @JsonProperty("realtime_end")String realtime_end,
                   @JsonProperty("name")String name,
                   @JsonProperty("press_release")boolean press_release,
                   @JsonProperty("link")String link
                   ){
        super(realtime_start,realtime_end);
        this.name = name;
        this.press_release = press_release;
        this.link = link;
    }*/

    public Release(@JsonProperty("realtime_start")String realtime_start,
                   @JsonProperty("realtime_end")String realtime_end,
                   @JsonProperty("name")String name,
                   @JsonProperty("press_release")boolean press_release,
                   @JsonProperty("link")String link,
                   @JsonProperty("notes")String notes
    ){
        super(realtime_start,realtime_end);
        this.name = name;
        this.press_release = press_release;
        this.link = link;
        this.notes = notes;
    }
}
