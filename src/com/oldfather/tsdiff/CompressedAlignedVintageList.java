package com.oldfather.tsdiff;

import com.oldfather.datetime.DateParser;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by theoldfather on 4/8/17.
 */
public class CompressedAlignedVintageList {
    CompressedAlignedVintageNode head = null;
    double[] series = null;
    boolean keepState = true;
    public Aligner aligner = null;

    /**
     * Constructs an empty CompressedAlignedVintageList that tracks the head series.
     */
    public CompressedAlignedVintageList(){

    }

    /**
     * Constructs an empty CompressedAlignedVintageList with a defined Aligner.
     */
    public CompressedAlignedVintageList(Date startDate, String freq){
        this.aligner = new Aligner(startDate,freq);
    }

    /**
     * Constructs an empty CompressedAlignedVintageList (optionally tracks the head series).
     * @param keepState Should the list maintain the state of the series head?
     */
    public CompressedAlignedVintageList(boolean keepState){
        this.keepState = keepState;
    }

    /**
     * Constructs a CompressedAlignedVintagesList by fully initializing a root node.
     * @param s_hash the sortable hash identifying the vintage
     * @param startDate the start date for the vintage
     * @param freq the frequency of the series
     * @param series the observed values of the series for this vintage
     */
    public CompressedAlignedVintageList(long s_hash, Date startDate, String freq, double[] series){
        this.insert(s_hash, startDate, freq, series);
    }

    public boolean insert(long s_hash, Date startDate, String freq, double[] series){
        if(this.isEmpty()){
            this.aligner = new Aligner(startDate,freq);
            return this.insert(s_hash, series);
        }else if(this.isValidHash(s_hash)){
            return this.insert(s_hash,startDate,series);
        }
        return false;
    }

    public boolean insert(long s_hash, double[] series){
        CompressedAlignedVintageNode node;
        if(this.isEmpty()){
            node = new CompressedAlignedVintageNode(s_hash, series);
            return this.moveHead(node,series);
        }
        return false;
    }


    public boolean insert(long s_hash, Date startDate, double[] series){
        if(this.isValidHash(s_hash)){
            CompressedAlignedVintageNode node;
            int align = this.aligner.getAlignment(startDate);
            if(this.keepState){
                node = new CompressedAlignedVintageNode(s_hash, align, series, this.head, this.series);
            }else{
                node = new CompressedAlignedVintageNode(s_hash, align, series, this.head);
            }
            return this.moveHead(node,series);
        }
        return false;
    }

    /**
     *  Inserts a previously encoded node from DB and checks to make sure the Aligned is defined for the root node.
     * @param s_hash
     * @param align
     * @param offset
     * @param delta
     * @param isCompressed
     * @return
     */
    public void insert(long s_hash, int align, int offset, double[] delta, boolean isCompressed, Date startDate, String freq){
        if(isEmpty()){
            this.aligner = new Aligner(startDate,freq);
            this.head = new CompressedAlignedVintageNode(s_hash,align,offset,delta,isCompressed);
        }else{
            this.head = new CompressedAlignedVintageNode(s_hash,align,offset,delta,isCompressed,this.head);
        }
    }

    /**
     * Inserts a previously encoded node from DB
     * @param s_hash
     * @param align
     * @param offset
     * @param delta
     * @param isCompressed
     */
    public void insert(long s_hash, int align, int offset, double[] delta, boolean isCompressed){
        if(isEmpty()){
            this.head = new CompressedAlignedVintageNode(s_hash,align,offset,delta,isCompressed);
        }else{
            this.head = new CompressedAlignedVintageNode(s_hash,align,offset,delta,isCompressed,this.head);
        }
    }

    public boolean isValidHash(long s_hash){
        return s_hash >= head.s_hash;
    }

    public boolean moveHead(CompressedAlignedVintageNode node, double[] series){
        if(node!=null && node.hasChanges()){
            if(this.isEmpty()){
                this.setHead(node,series);
                return true;
            }else if(!this.getHead().equalTo(node)){
                this.setHead(node,series);
                return true;
            }
        }
        return false;
    }

    public void setHead(CompressedAlignedVintageNode node, double[] series){
        this.head = node;
        if(this.keepState){
            this.series = series;
        }
    }

    public void mergeVintage(long s_hash, Date startDate, String freq, double[] series){
        // get angry if this there is an attempt to mix frequencies
        if(freq!=aligner.freq) throw new RuntimeException("The frequency of the vintage to be merged does not match the current frequency.");

        if(isEmpty()){
            this.insert(s_hash,startDate,freq,series);
        }else if(s_hash >= head.s_hash){
            this.insert(s_hash,startDate,freq,series);
        }else{
            LinkedList<CompressedAlignedVintageNode> future = new LinkedList<>();
            CompressedAlignedVintageNode node = this.getHead();
            while(s_hash < node.s_hash){
                future.add(node);
                node = node.getParent();
                if(node==null) break;
            }

            if(node==null){
                // rebase with this as root
            }else{
                // 1. encode to node
                int align = this.aligner.getAlignment(startDate);
                CompressedAlignedVintageNode new_node = new CompressedAlignedVintageNode(s_hash, align, series, node);
                // 2. encode previous node to this one to this one
                future.getLast().encodeDelta(future.getLast().align,future.getLast().decodeDelta(),align,series);
                future.getLast().parent = new_node;
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

    public CompressedAlignedVintageNode getVintage(long queryHash){ return head.getVintage(queryHash); }

    public CompressedAlignedVintageNode getVintage(Date queryDate){ return this.getVintage(queryDate.getTime()); }

    public CompressedAlignedVintageNode getVintage(String queryDateStr){
        return this.getVintage(queryDateStr,"yyyy-MM-dd");
    }

    public CompressedAlignedVintageNode getVintage(String queryDateStr, String pattern){
        return this.getVintage((new DateParser(queryDateStr,pattern)).getTime());
    }

    public double[] getVintageSeries(long queryHash){
        return head.getVintageSeries(queryHash);
    }

    public double[] getVintageSeries(Date queryDate){
        return this.getVintageSeries(queryDate.getTime());
    }

    public double[] getVintageSeries(String queryDateStr){
        return this.getVintageSeries(queryDateStr,"yyyy-MM-dd");
    }

    public double[] getVintageSeries(String queryDateStr, String pattern){
        return this.getVintageSeries((new DateParser(queryDateStr,pattern)).getTime());
    }

    // work on this...
    public Date getVintageStartDate(String queryDateStr){
        return aligner.getStartDate(this.getVintage(queryDateStr).align);
    }

    public String getFreq(){
        return this.aligner.freq;
    }


    public boolean isEmpty(){
        return (this.head==null);
    }

    public class Aligner{

        Date rootStartDate;
        LocalDate rootStartLocalDate;
        String freq;


        Aligner(Date rootStartDate, String freq){
            this.rootStartDate = rootStartDate;
            this.rootStartLocalDate = dateToLocalDate(rootStartDate);
            this.freq = freq;
        }

        public int getAlignment(Date startDate){
            LocalDate startLocalDate = dateToLocalDate(startDate);
            int alignment;
            switch(this.freq){
                case "Annual": alignment = inYears(startLocalDate); break;
                case "Quarterly": alignment = inQuarters(startLocalDate); break;
                case "Monthly": alignment = inMonths(startLocalDate); break;
                case "Weekly": alignment = inWeeks(startLocalDate); break;
                case "Weekly (Mon)": alignment = inWeeks(startLocalDate); break;
                case "Weekly (Tue)": alignment = inWeeks(startLocalDate); break;
                case "Weekly (Wed)": alignment = inWeeks(startLocalDate); break;
                case "Weekly (Thu)": alignment = inWeeks(startLocalDate); break;
                case "Weekly (Fri)": alignment = inWeeks(startLocalDate); break;
                case "Weekly (Sat)": alignment = inWeeks(startLocalDate); break;
                case "Weekly (Sun)": alignment = inWeeks(startLocalDate); break;
                default: alignment = inDays(startLocalDate); break;
            }
            return alignment;
        }

        public Date getStartDate(int align){
            LocalDate startLocalDate;
            switch(this.freq){
                case "Annual": startLocalDate = inYears(align); break;
                case "Quarterly": startLocalDate = inQuarters(align); break;
                case "Monthly": startLocalDate = inMonths(align); break;
                case "Weekly": startLocalDate = inWeeks(align); break;
                case "Weekly (Mon)": startLocalDate = inWeeks(align); break;
                case "Weekly (Tue)": startLocalDate = inWeeks(align); break;
                case "Weekly (Wed)": startLocalDate = inWeeks(align); break;
                case "Weekly (Thu)": startLocalDate = inWeeks(align); break;
                case "Weekly (Fri)": startLocalDate = inWeeks(align); break;
                case "Weekly (Sat)": startLocalDate = inWeeks(align); break;
                case "Weekly (Sun)": startLocalDate = inWeeks(align); break;
                default: startLocalDate = inDays(align); break;
            }
            return java.sql.Date.valueOf(startLocalDate);
        }

        public int inDays(LocalDate startDate){
            return (int)ChronoUnit.DAYS.between(this.rootStartLocalDate, startDate);
        }

        public int inWeeks(LocalDate startDate){
            return (int)ChronoUnit.WEEKS.between(this.rootStartLocalDate, startDate);
        }

        public int inMonths(LocalDate startDate){
            return (int)ChronoUnit.MONTHS.between(this.rootStartLocalDate, startDate);
        }

        public int inQuarters(LocalDate startDate){
            return Math.round(inMonths(startDate)/3);
        }

        public int inYears(LocalDate startDate){
            return (int)ChronoUnit.YEARS.between(this.rootStartLocalDate, startDate);
        }


        public LocalDate inDays(int align){
            return rootStartLocalDate.plus(align,ChronoUnit.DAYS);
        }

        public LocalDate inWeeks(int align){
            return rootStartLocalDate.plus(align,ChronoUnit.WEEKS);
        }

        public LocalDate inMonths(int align){
            return rootStartLocalDate.plus(align,ChronoUnit.MONTHS);
        }

        public LocalDate inQuarters(int align){
            return this.inMonths(align*3);
        }

        public LocalDate inYears(int align){
            return rootStartLocalDate.plus(align,ChronoUnit.YEARS);
        }

        public LocalDate dateToLocalDate(Date date){
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

    }

    public static class FrequencyMap<T>{
        private HashMap<T,String> map ;
        private HashMap<String,T> revmap ;

        public FrequencyMap(int initialCapacity){
            this.map = new HashMap<>(initialCapacity);
            this.revmap = new HashMap<>(initialCapacity);
        }

        public FrequencyMap(HashMap<T,String> map){
            this.map = map;
            this.revmap = new HashMap<>(map.size());

            for(Map.Entry e: this.map.entrySet()){
                this.revmap.put(e.getValue().toString(),  (T)(e.getKey()));
            }
        }

        public FrequencyMap<T> put(T from, String to){
            this.map.put(from,to);
            this.revmap.put(to,from);
            return this;
        }

        public String toAligner(T freq){
            return this.map.get(freq);
        }

        public T fromAligner(String freq){
            return this.revmap.get(freq);
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
