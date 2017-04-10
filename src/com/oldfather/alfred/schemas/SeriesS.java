package com.oldfather.alfred.schemas ; 


import com.fasterxml.jackson.annotation.JsonProperty ; 

import java.util.List ; 

/**
 * Created by theoldfather on 4/9/17.
 */
public class SeriesS {
    public String id; 
    public String realtime_start; 
    public String realtime_end; 
    public String title; 
    public String observation_start; 
    public String observation_end; 
    public String frequency; 
    public String frequency_short; 
    public String units; 
    public String units_short; 
    public String seasonal_adjustment; 
    public String seasonal_adjustement_short; 
    public String last_updated; 
    public int popularity; 
    public String notes; 

    public SeriesS(){

    }

    public SeriesS(@JsonProperty("id")String id,
                   @JsonProperty("realtime_start")String realtime_start,
                   @JsonProperty("realtime_end")String realtime_end,
                   @JsonProperty("title")String title,
                   @JsonProperty("observation_start")String observation_start,
                   @JsonProperty("observation_end")String observation_end,
                   @JsonProperty("frequency")String frequency,
                   @JsonProperty("frequency_short")String frequency_short,
                   @JsonProperty("units")String units,
                   @JsonProperty("units_short")String units_short,
                   @JsonProperty("seasonal_adjustment")String seasonal_adjustment,
                   @JsonProperty("seasonal_adjustment_short")String seasonal_adjustment_short,
                   @JsonProperty("last_updated")String last_updated,
                   @JsonProperty("popularity")int popularity,
                   @JsonProperty("notes")String notes
                   ){
        this.id =                          id ;                         
        this.realtime_start =              realtime_start ;             
        this.realtime_end =                realtime_end ;               
        this.title =                       title ;                      
        this.observation_start =           observation_start ;          
        this.observation_end =             observation_end ;            
        this.frequency =                   frequency ;                  
        this.frequency_short =             frequency_short ;            
        this.units =                       units ;                      
        this.units_short =                 units_short ;                
        this.seasonal_adjustment =         seasonal_adjustment ;        
        this.seasonal_adjustement_short =  seasonal_adjustement_short ; 
        this.last_updated =                last_updated ;               
        this.popularity =                  popularity ;                 
        this.notes =                       notes ;                      
    }

}
