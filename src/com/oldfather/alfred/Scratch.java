package com.oldfather.alfred;

import com.oldfather.datetime.DateParser;
import com.oldfather.tsdiff.CompressedAlignedVintageList;
import com.oldfather.tsdiff.CompressedAlignedVintageNode;
import com.oldfather.tsdiff.RLEDropMode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

/**
 * Created by theoldfather on 4/9/17.
 */
public class Scratch {

    public static void main(String[] args){
        RLEDropMode rle = new RLEDropMode();
        double[] s = new double[]{1,1,0,0,2,2,5,6};
        int[] n_runs = rle.countRuns(s);
        System.out.println(Arrays.toString(n_runs));
        System.out.println(rle.shouldCompress(s.length,n_runs));
        double[] comp = rle.compress(s,n_runs);
        System.out.println(Arrays.toString(comp));
        double[] decomp = rle.decompress(comp);
        System.out.println(Arrays.toString(decomp));
    }
}
