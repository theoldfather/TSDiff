package com.oldfather.alfred;

import com.oldfather.alfred.schemas.ObservationJaxb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by theoldfather on 4/8/17.
 */
public class AlfredVintage {
    List<ObservationJaxb> obsList;
    long hash;
    double[] series;
    Date startDate=null;

    public AlfredVintage(List<ObservationJaxb> obsList, String date){
        this.filterObs(obsList,date);
        this.extractSeries();
        this.extractStartDate();
    }

    public void filterObs(List<ObservationJaxb> obsList, String date){
        List<ObservationJaxb> _obsList = new ArrayList<>();
        for(ObservationJaxb ob: obsList){
            _obsList.add(ob);
        }
        _obsList.removeIf(o -> !((o.realtime_start.compareTo(date)<=0) &
                0<=(o.realtime_end.compareTo(date))) );
        this.obsList = _obsList;
    }

    public void extractStartDate(){
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
        try{
            Date date = format.parse(this.obsList.get(0).date);
            this.startDate = date;
        }catch(ParseException e){
            e.printStackTrace();
        }
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
