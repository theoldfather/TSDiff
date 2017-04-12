package com.oldfather.alfred;

import com.oldfather.datetime.DateParser;
import com.oldfather.tsdiff.CompressedAlignedVintageList;
import com.oldfather.tsdiff.CompressedAlignedVintageNode;

import java.util.Arrays;

/**
 * Created by theoldfather on 4/9/17.
 */
public class Scratch {

    public static void main(String[] args){
        VintageListBuilder vlb = new VintageListBuilder("GDPC1");
        CompressedAlignedVintageList vl = vlb.getVintageList();
        CompressedAlignedVintageNode vn = vl.getVintage("1992-12-23");
        System.out.println(Arrays.toString(vlb.getVintageList().getHeadSeries()));
        System.out.println(Arrays.toString(vlb.getVintageList().getVintageSeries("2014-07-28")));

        System.out.println((new DateParser("2014-07-28")).getDate().toString());

    }
}
