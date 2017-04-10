package com.oldfather.alfred;

import com.oldfather.alfred.schemas.Observation;
import com.oldfather.alfred.schemas.Observations;
import com.oldfather.alfred.schemas.Series;
import com.oldfather.alfred.schemas.VintageDates;
import com.oldfather.datetime.DateParser;
import com.oldfather.tsdiff.CompressedAlignedVintageList;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Builds a CompressedAlignedVintageList from vintages of an ALFRED series
 */
public class VintageListBuilder {
    String series_id;
    Observations obsSet;
    List<String> vintage_dates;
    Series seriesMeta;
    CompressedAlignedVintageList vintageList = new CompressedAlignedVintageList();


    public VintageListBuilder(String series_id){
        this.series_id = series_id;
        this.getSeriesMeta();
        this.getVintageDates();
        this.getObs();
        this.createVintageList();
    }

    private void getVintageDates(){
        Response res = (new Query.QueryBuilder())
                .setApiKey("6190fad6f8ed0bb43338ac0cbc56c51b")
                .setFileType("json")
                .addPath("series")
                .addPath("vintagedates")
                .addQueryParam("series_id",this.series_id)
                .createQuery().execute();

        this.vintage_dates  = res.readEntity(VintageDates.class).vintage_dates;
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

    private void getObs(){
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

    private void createVintageList(){
        List<Observation> obsList = obsSet.observations;
        for(String date : vintage_dates){
            Vintage vintage = new Vintage(obsList,date);
            vintageList.insert((new DateParser(date)).getTime(),0,vintage.series);
        }
    }

    public CompressedAlignedVintageList getVintageList(){
        return this.vintageList;
    }

}
