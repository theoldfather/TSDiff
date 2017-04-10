package com.oldfather.alfred;

import com.oldfather.alfred.schemas.ObservationJaxb;
import com.oldfather.alfred.schemas.ObservationsSetJaxb;
import com.oldfather.alfred.schemas.VintageDates;
import com.oldfather.tsdiff.CompressedAlignedVintageList;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

/**
 * Builds a CompressedAlignedVintageList from vintages of an ALFRED series
 */
public class AlfredVintageListBuilder {
    String series_id;
    ObservationsSetJaxb obsSet;
    List<String> vintage_dates;
    CompressedAlignedVintageList vintageList = new CompressedAlignedVintageList();

    public AlfredVintageListBuilder(String series_id){
        this.series_id = series_id;
    }

    private void getObs(){
        AlfredQuery alfred = (new AlfredQuery.QueryBuilder())
             .setApiKey("6190fad6f8ed0bb43338ac0cbc56c51b")
             .setFileType("json")
             .addPath("series")
             .addPath("observations")
             .addQueryParam("series_id","GNPCA")
             .addQueryParam("realtime_start","1776-07-04")
             .addQueryParam("realtime_end","9999-12-31")
             .createQuery()
             ;
        Response res = alfred.execute();
        this.obsSet = res.readEntity(ObservationsSetJaxb.class);
    }

    private void getVintageDates(){
        Response vdates_res = (new AlfredQuery.QueryBuilder())
                .setApiKey("6190fad6f8ed0bb43338ac0cbc56c51b")
                .setFileType("json")
                .addPath("series")
                .addPath("vintagedates")
                .addQueryParam("series_id","GNPCA")
                .createQuery().execute();

        this.vintage_dates  = vdates_res.readEntity(VintageDates.class).vintage_dates;
    }

    private void createVintageList(){
        List<ObservationJaxb> obsList = obsSet.observations;
        for(String date : vintage_dates){
            AlfredVintage vintage = new AlfredVintage(obsList,date);
            vintageList.insert(vintage.startDate.getTime(),0,vintage.series);
        }
    }

    public CompressedAlignedVintageList getVintageList(){
        return this.vintageList;
    }
    
}
