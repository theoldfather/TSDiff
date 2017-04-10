package com.oldfather.tsdiff;

import com.oldfather.datetime.DateParser;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by theoldfather on 4/8/17.
 */
public class CompressedAlignedVintageList {
    CompressedAlignedVintageNode head = null;
    double[] series = null;
    boolean keepState = true;

    /**
     * Constructs an empty CompressedAlignedVintageList that tracks the head series.
     */
    public CompressedAlignedVintageList(){

    }

    public CompressedAlignedVintageList(long s_hash, double[] series){
        this.insert(s_hash,series);
    }

    /**
     * Constructs an empty CompressedAlignedVintageList (optionally tracks the head series).
     * @param keepState Should the list maintain the state of the series head?
     */
    public CompressedAlignedVintageList(boolean keepState){
        this.keepState = keepState;
    }


    public void insert(long s_hash, double[] series) throws AlignmentException{
        this.insert(s_hash, 0 , series);
    }

    public void insert(long s_hash, int align, double[] series) throws AlignmentException{
        CompressedAlignedVintageNode node;

        if(head==null){
            if(align!=0) throw new AlignmentException(align);
            node = new CompressedAlignedVintageNode(s_hash, series);
        }else{
            if(this.keepState){
                node = new CompressedAlignedVintageNode(s_hash, align, series, this.head, this.series);
            }else{
                node = new CompressedAlignedVintageNode(s_hash, align, series, this.head);
            }
        }
        this.moveHead(node,series);
    }

    public void moveHead(CompressedAlignedVintageNode node, double[] series){
        if(node!=null){
            if(node.hasChanges()){
                this.head = node;
                if(this.keepState){
                    this.series = series;
                }
            }
        }

    }

    public CompressedAlignedVintageNode getHead(){
        return this.head;
    }

    public double[] getHeadSeries(){
        if(!isEmpty()){
            if(this.keepState){
                return this.series;
            }else{
                return this.head.decodeDelta();
            }
        }
        return null;
    }

    public double[] getVintageSeries(long queryHash){
        return head.getVintageSeries(queryHash);
    }

    public double[] getVintageSeries(Date queryDate){
        return this.getVintageSeries(queryDate.getTime());
    }

    public double[] getVintageSeries(String queryDateStr){
        return this.getVintageSeries(queryDateStr,"YYYY-MM-DD");
    }

    public double[] getVintageSeries(String queryDateStr, String pattern){
        return this.getVintageSeries((new DateParser(queryDateStr)).getTime());
    }

    public boolean isEmpty(){
        return (this.head==null);
    }

    public class Aligner{

        public Date rootStartDate;
        public String freq;

        public Aligner(Date rootStartDate, String freq){
            this.rootStartDate = rootStartDate;
            this.freq = freq;
        }

    }

    public class AlignmentException extends RuntimeException{
        private int alignment;

        public AlignmentException(int alignment){
            this.alignment = alignment;
        }

        public int getAlignment(){
            return this.alignment;
        }
    }

}
