package com.oldfather.alfred;

/**
 * Created by theoldfather on 4/9/17.
 */
public class Scratch {

    public static void main(String[] args){
        VintageListBuilder vlb = new VintageListBuilder("GDPC1");
        System.out.println( vlb.seriesMeta.seriess.get(0).frequency );
    }
}
