package com.oldfather.alfred;

import com.oldfather.alfred.schemas.Observation;
import com.oldfather.alfred.schemas.Observations;
import com.oldfather.alfred.schemas.Series;
import com.oldfather.alfred.schemas.VintageDates;
import com.oldfather.datetime.DateParser;
import com.oldfather.tsdiff.CompressedAlignedVintageList;
import com.oldfather.tsdiff.CompressedAlignedVintageList.FrequencyMap;
import com.oldfather.tsdiff.CompressedAlignedVintageNode;
import jersey.repackaged.com.google.common.collect.Lists;

import javax.ws.rs.core.Response;
import java.util.*;

/**
 * Builds a CompressedAlignedVintageList from vintages of an ALFRED series
 */
public class VintageListBuilder {
    String series_id;
    Observations obsSet;
    List<String> vintage_dates;
    Series seriesMeta;
    CompressedAlignedVintageList vintageList = new CompressedAlignedVintageList();
    FrequencyMap<String> fm;

    public VintageListBuilder(String series_id){
        this.series_id = series_id;
        this.setFrequencyMap();
        this.getSeriesMeta();
        this.getObservations();
        this.getVintageDates();

        this.createVintageList();
    }

    private void setFrequencyMap(){
        FrequencyMap<String> fm = (new FrequencyMap<String>(5))
                .put("Y","Annual")
                .put("Q","Quarterly")
                .put("M","Monthly")
                .put("W","Weekly")
                .put("D","Daily");
        this.fm = fm;
    }

    /*private void getVintageDates(){
        Response res = (new Query.QueryBuilder())
                .setApiKey("6190fad6f8ed0bb43338ac0cbc56c51b")
                .setFileType("json")
                .addPath("series")
                .addPath("vintagedates")
                .addQueryParam("series_id",this.series_id)
                .createQuery().execute();

        this.vintage_dates  = res.readEntity(VintageDates.class).vintage_dates;
    }*/

    private void getVintageDates(){
        Set<String> vintageDatesSet = new HashSet<>(10);
        for(Observation ob : this.obsSet.observations){
            vintageDatesSet.add(ob.realtime_start);
        }
        List<String> vintage_dates = Lists.newArrayList(vintageDatesSet);
        Collections.sort(vintage_dates);
        System.out.printf("first: %s, last: %s\n", vintage_dates.get(0),vintage_dates.get(vintage_dates.size()-1));
        this.vintage_dates  = vintage_dates;
    }

    private void getSeriesMeta(){
        Response res = (new Query.QueryBuilder())
                .setApiKey("6190fad6f8ed0bb43338ac0cbc56c51b")
                .setFileType("json")
                .addPath("series")
                .addQueryParam("series_id",this.series_id)
                .createQuery().execute()
                ;
        this.seriesMeta = res.readEntity(Series.class);

    }

    private void getObservations(){
        Response res = (new Query.QueryBuilder())
             .setApiKey("6190fad6f8ed0bb43338ac0cbc56c51b")
             .setFileType("json")
             .addPath("series")
             .addPath("observations")
             .addQueryParam("series_id",this.series_id)
             .addQueryParam("realtime_start","1776-07-04")
             .addQueryParam("realtime_end","9999-12-31")
             .createQuery().execute()
             ;
        this.obsSet = res.readEntity(Observations.class);
    }

    private void createVintageList() {
        List<Observation> obsList = obsSet.observations;
        String freq = fm.toAligner(seriesMeta.seriess.get(0).frequency_short);
        for (String date : vintage_dates) {
            Vintage vintage = new Vintage(obsList, date);
            vintageList.insert((new DateParser(date)).getTime(), vintage.startDate, freq, vintage.series);
        }
    }

    public CompressedAlignedVintageList getVintageList(){
        return this.vintageList;
    }

}
