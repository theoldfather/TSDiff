package com.oldfather.alfred;

import com.oldfather.alfred.schemas.Observation;
import com.oldfather.datetime.DateParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by theoldfather on 4/8/17.
 */
public class Vintage {
    List<Observation> obsList;
    long hash;
    double[] series;
    Date startDate=null;

    public Vintage(List<Observation> obsList, String date){
        this.filterObs(obsList,date);
        this.extractSeries();
        this.extractStartDate();
    }

    public void filterObs(List<Observation> obsList, String date){
        List<Observation> _obsList = new ArrayList<>();
        for(Observation ob: obsList){
            _obsList.add(ob);
        }
        _obsList.removeIf(o -> !((o.realtime_start.compareTo(date)<=0) &
                0<=(o.realtime_end.compareTo(date))) );
        this.obsList = _obsList;
    }

    public void extractStartDate(){
        this.startDate = (new DateParser(this.obsList.get(0).date)).getDate();
        this.hash = this.startDate.getTime();
    }

    public void extractSeries(){
        double[] series = new double[obsList.size()];
        String str_val;
        for(int i=0; i< obsList.size(); i++){
            str_val = obsList.get(i).value;
            if(!str_val.equals(".")){
                series[i]=Double.parseDouble(obsList.get(i).value);
            }else{
                series[i]=1e9;
            }

        }
        this.series = series;
    }
}
