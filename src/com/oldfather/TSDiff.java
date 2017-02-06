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

        public VintageNode(){
            // nothing by default
        }

        public VintageNode(long s_hash, double[] s){
            this.delta = s;
            this.s_hash = s_hash;
        }
        public VintageNode(long s_hash, double[] s, VintageNode parent){
            if(parent.hasChanges()) this.parent=parent;
            this.s_hash = s_hash;
            this.encodeDelta(s,parent.decodeDelta());
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
            if(parent.hasChanges()) this.parent=parent;
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

        public VintageNode getRootNode(){
            return (this.isRootNode() ? this : this.parent.getRootNode() );
        }

        public VintageNode getParent(){ return this.parent;  }

        // O(n)
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

        // k*n => O(k), where k is the number of prior vintages
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


        public boolean isRootNode(){
            return (this.parent==null);
        }

        public boolean hasParent(){ return !(this.isRootNode()); }

        public Double[] deltaToDoubleArray(){
            Double[] delta = new Double[this.delta.length];
            for(int i=0; i<this.delta.length; i++){
                delta[i] = this.delta[i];
            }
            return delta;
        }

    }


    public static class CompressedVintageNode extends VintageNode{

        public boolean isCompressed=false;
        public CompressedVintageNode parent=null;

        public CompressedVintageNode(){
            // nothing by default
        }

        public CompressedVintageNode(long s_hash, double[] s){
            super(s_hash,s);
        }
        public CompressedVintageNode(long s_hash, double[] s, CompressedVintageNode parent){
            if(parent.hasChanges()) this.parent=parent;
            this.s_hash = s_hash;
            this.encodeDelta(s,parent.decodeDelta());
        }
        public CompressedVintageNode(long s_hash, int offset, double[] delta){
            super(s_hash,offset,delta);
        }
        public CompressedVintageNode(long s_hash, int offset, double[] delta, boolean isCompressed, CompressedVintageNode parent){
            this.s_hash=s_hash;
            this.offset=offset;
            this.delta=delta;
            this.isCompressed=isCompressed;
            if(parent.hasChanges()) this.parent=parent;
        }

        //---- OVERRIDE FROM SUPER -----

        public CompressedVintageNode cleanup(){
            if(this.hasChanges()){
                return this.parent;
            }else{
                return this;
            }
        }

        public CompressedVintageNode getRootNode(){
            return (this.isRootNode() ? this : this.parent.getRootNode() );
        }

        public CompressedVintageNode getParent(){ return this.parent;  }

        //---- END OVERRIDES ------------

        // O(n)
        public static int countRepeated(double[] a){
            if(a.length<=1){
                return 0;
            }else{
                int sum=0;
                for(int i=1; i< a.length; i++){
                    if(a[i-1]==a[i]) sum++;
                }
                return sum;
            }
        }

        public static boolean shouldCompress(int n, int n_repeated){
            return n > 2 & n_repeated > n/2;
        }

        // O(n)
        public static double[] compress(double[] a,int n_repeated){
            int n_unique = a.length - n_repeated;
            double[] out = new double[n_unique*2];
            int k=0;
            out[k]=1;
            out[k+n_unique]=a[0];
            for(int i=1; i<a.length; i++){
                if(out[k+n_unique]==a[i]){
                    out[k]++;
                }else{
                    k++;
                    out[k]=1;
                    out[k+n_unique]=a[i];
                }
            }
            return out;
        }

        // r + n => O(n)
        public static double[] decompress(double[] a){
            int r = a.length/2; // number of pairs
            int k=0; // count output elements
            for(int i=0; i<r; i++) k+=a[i];
            double[] out = new double[k];
            int l=0; // index a
            int o=0; // index out
            while(o<k){
                // unroll a into out
                for(int j=0; j<a[l]; j++){
                    out[o]=a[l+r];
                    o++;
                }
                l++;
            }
            return out;
        }

        public void encodeDelta(double[] s2, double[] s1){
            super.encodeDelta(s2,s1);
            if(this.hasChanges()){
                int r  = countRepeated(this.delta);
                if(shouldCompress(this.delta.length,r)){
                    this.delta = compress(this.delta,r);
                    this.isCompressed = true;
                }
            }
        }
        public double[] decodeDelta(){
            if(this.isCompressed){
                this.delta = decompress(this.delta);
                this.isCompressed=false;
            }
            return super.decodeDelta();
        }
    }


    // testing the waters
    public static void main(String[] args){

        double[] s = {};
        double[] t = {0,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        System.out.println("original.");

        System.out.println(Arrays.toString(s));
        int n_repeated = CompressedVintageNode.countRepeated(s);
        if(CompressedVintageNode.shouldCompress(s.length,n_repeated)){
            System.out.println("compressed.");
            double[] c = CompressedVintageNode.compress(s,n_repeated);
            System.out.println(Arrays.toString(c));
            System.out.println("decompressed.");
            double[] d = CompressedVintageNode.decompress(c);
            System.out.println(Arrays.toString(d));
        }else{
            System.out.println("not compressed.");

        }

        CompressedVintageNode a = new CompressedVintageNode(1,s);
        CompressedVintageNode b = new CompressedVintageNode(2,t,a);

        System.out.println(Arrays.toString(b.decodeDelta()));

    }
}
