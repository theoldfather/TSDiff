package com.oldfather.alfred;

import com.oldfather.datetime.DateParser;
import com.oldfather.tsdiff.CompressedAlignedVintageList;
import com.oldfather.tsdiff.CompressedAlignedVintageNode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

/**
 * Created by theoldfather on 4/9/17.
 */
public class Scratch {

    public static void main(String[] args){
        VintageListBuilder vlb = new VintageListBuilder("GDPC1");
        CompressedAlignedVintageList vl = vlb.getVintageList();
        String date_str = "2017-04-13";
        CompressedAlignedVintageNode vn = vl.getVintage(date_str);
        System.out.println(Arrays.toString(vn.decodeDelta()));
        System.out.println(vl.getVintageStartDate(date_str));



       /* CompressedAlignedVintageList cavl = new CompressedAlignedVintageList();
        cavl.insert(1,(new DateParser("2012-12-31")).getDate(),"Annual",new double[]{1,2,3,4});
        cavl.insert(2,(new DateParser("2013-12-31")).getDate(),new double[]{2,3});
        System.out.println(Arrays.toString(cavl.getHead().getRootNode().decodeDelta()));
        System.out.println(Arrays.toString(cavl.getHead().decodeDelta()));
        System.out.println(Arrays.toString(cavl.getHeadSeries()));*/

    }
}
