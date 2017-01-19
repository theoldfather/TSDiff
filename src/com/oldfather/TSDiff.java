package com.oldfather;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by theoldfather on 1/7/17.
 */
public class TSDiff {



    public static class VintageNode{

        public VintageNode parent=null;
        public long s_hash;
        public int offset=0;
        public double[] delta;

        public VintageNode(long s_hash, double[] s){
            this.delta = s;
            this.s_hash = s_hash;
        }
        public VintageNode(long s_hash, double[] s, VintageNode parent){
            this.parent=parent;
            this.s_hash = s_hash;
            encodeDelta(s,parent.decodeDelta());
        }
        public VintageNode(long s_hash, int offset, double[] delta){
            this.s_hash=s_hash;
            this.offset=offset;
            this.delta=delta;
        }
        public VintageNode(long s_hash, int offset, double[] delta, VintageNode parent){
            this.s_hash=s_hash;
            this.offset=offset;
            this.delta=delta;
            this.parent=parent;
        }

        public boolean hasChanges(){
            return !(this.offset==-1 & this.delta==null);
        }

        public VintageNode cleanup(){
            if(this.hasChanges()){
                return this.parent;
            }else{
                return this;
            }
        }

        public void encodeDelta(double[] s2, double[] s1){

            try {
                assert s2.length >= s1.length;
            }catch(AssertionError e){
                System.err.println("The new vintage should be at least as long as the previous one.");
            }

            int n = s2.length;
            double[] delta = null;
            int offset=-1;
            for(int i=0; i<n; i++){
                if(i<s1.length){
                    if(s2[i]!=s1[i]){
                        if(offset==-1) {
                            offset = i;
                            delta = new double[n-i];
                        }
                        delta[i-offset]=s2[i]-s1[i];
                    }
                }else{
                    if(offset==-1) {
                        offset = i;
                        delta = new double[n-i];
                    }
                    delta[i-offset]=s2[i];
                }
            }
            this.delta=delta;
            this.offset=offset;
        }

        public double[] decodeDelta(){

            if(this.isRootNode()){
                return this.delta;
            }else{
                double[] s1 = this.parent.decodeDelta();
                if(this.offset==-1){
                    return s1;
                }else{
                    int n = this.delta.length + this.offset;
                    double[] s2 = new double[n];
                    for(int i=0; i<n; i++){
                        if(i<this.offset){
                            s2[i]=s1[i];
                        }else if(i>=this.offset & i<s1.length){
                            s2[i]=s1[i]+this.delta[i-this.offset];
                        }else{
                            s2[i]=this.delta[i-this.offset];
                        }
                    }
                    return s2;
                }
            }
        }

        public int getVintageNumber(){
            return 1 + (this.isRootNode() ? 0 : this.parent.getVintageNumber());
        }

        public VintageNode getRootNode(){
            return (this.isRootNode() ? this : this.parent.getRootNode() );
        }

        public boolean isRootNode(){
            return (this.parent==null);
        }

        public boolean hasParent(){ return !(this.isRootNode()); }

        public VintageNode getParent(){ return this.parent;  }

        public Double[] deltaToDoubleArray(){
            Double[] delta = new Double[this.delta.length];
            for(int i=0; i<this.delta.length; i++){
                delta[i] = this.delta[i];
            }
            return delta;
        }
    }



    // testing the waters
    public static void main(String[] args){

    }
}
