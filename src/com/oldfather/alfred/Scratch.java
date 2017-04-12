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
        VintageListBuilder vlb = new VintageListBuilder("A191RL1A225NBEA");
        CompressedAlignedVintageList vl = vlb.getVintageList();
        CompressedAlignedVintageNode vn = vl.getVintage("2014-12-12");

        System.out.println(Arrays.toString(vn.decodeDelta()));
    }
}
