package com.oldfather.alfred;

import com.oldfather.alfred.schemas.*;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by theoldfather on 4/14/17.
 */
public class ReleaseBuilder {

    String api_key;
    List<Release> releases;


    public ReleaseBuilder(){
        this.api_key = System.getenv("FRED_API_KEY");
        this.buildReleases();
    }

    public ReleaseBuilder(String api_key){
        this.api_key = api_key;
        this.buildReleases();
    }

    private void buildReleases(){
        this.fetchReleases();
    }

    private void fetchReleases(){
        if(this.api_key==null) throw new RuntimeException("Fred API Key not found. Try setting FRED_API_KEY.");
        Response res = (new Query.QueryBuilder())
                .setApiKey(this.api_key)
                .setFileType("json")
                .addPath("releases")
                .addQueryParam("realtime_end","9999-12-31")
                .createQuery().execute()
                ;
        this.releases = res.readEntity(Releases.class).releases;
    }

    public void showReleases(String date_str){
        for(Release release: this.releases){
            if(release.contains(date_str)){
                System.out.printf("%d: %s\n",release.id ,release.name);
            }
        }
    }

    public void showReleaseSeries(int release_id, String date_str){

        Response res = (new Query.QueryBuilder())
                .setApiKey(this.api_key)
                .setFileType("json")
                .addPath("releases")
                .addPath("series")
                .addQueryParam("release_id",51 )
                .addQueryParam("realtime_end","9999-12-31")
                .createQuery().execute()
                ;

        List<SeriesS> seriess = res.readEntity(ReleaseSeries.class).seriess;
        for(SeriesS series: seriess){
            if(series.contains(date_str)){
                System.out.printf("%d: %s\n",series.id ,series.title);
            }
        }
    }

}
