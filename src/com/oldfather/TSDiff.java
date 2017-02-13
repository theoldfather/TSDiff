package com.oldfather;

/**
 * Created by theoldfather on 1/7/17.
 */
public class TSDiff {

    public static class VintageNode extends Object{

        public VintageNode parent=null;
        public long s_hash;
        public int offset=-1;
        public double[] delta;

        public VintageNode(){
            // nothing to do here
        }

        public VintageNode(long s_hash, double[] s){
            this.encodeDelta(s);
            this.s_hash = s_hash;
        }
        public VintageNode(long s_hash, double[] s, VintageNode parent){
            if(parent.hasChanges()){
                this.parent=parent;
                this.encodeDelta(s,parent.decodeDelta());
            }else{
                this.encodeDelta(s);
            }
            this.s_hash = s_hash;

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
            if(this.delta!=null){
                if(this.delta.length>0){
                    return true;
                }
            }
            return false;
        }

        public VintageNode cleanup(){
            if(this.hasChanges()){
                return this.parent;
            }else{
                return this;
            }
        }

        public VintageNode getRootNode(){
            if(this.isRootNode()){
                return this;
            }else{
                return this.getParent().getRootNode();
            }
        }

        public VintageNode getParent(){ return this.parent;  }

        public void encodeDelta(double[] s){
            if(s!=null){
                if(s.length>0){
                    this.delta=s;
                    this.offset=0;
                }
            }
        }
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
            // iterate of length of s2
            // split into revisions and updates
            for(int i=0; i<n; i++){
                // revisions
                if(i<s1.length){
                    // does s2 contain revisions to elements of s1?
                    if(s2[i]!=s1[i]){
                        // if so, mark the offset of the first change
                        if(offset==-1) {
                            offset = i;
                            delta = new double[n-i];
                        }
                        delta[i-offset]=s2[i]-s1[i];
                    }
                // does s2 contain updates to end of s1
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

        public CompressedVintageNode(){

        }

        public CompressedVintageNode(long s_hash, double[] s){
            this.encodeDelta(s);
            this.s_hash = s_hash;
        }
        public CompressedVintageNode(long s_hash, double[] s, CompressedVintageNode parent){

            if(parent.hasChanges()){
                this.parent=parent;
                this.encodeDelta(s,parent.decodeDelta());
            }else{
                this.encodeDelta(s);
            }
            this.s_hash = s_hash;

        }
        public CompressedVintageNode(long s_hash, int offset, double[] delta){
            this.s_hash=s_hash;
            this.offset=offset;
            this.delta=delta;
        }
        public CompressedVintageNode(long s_hash, int offset, double[] delta, boolean isCompressed, CompressedVintageNode parent){
            this.s_hash=s_hash;
            this.offset=offset;
            this.delta=delta;
            this.isCompressed=isCompressed;
            if(parent.hasChanges()) this.parent=parent;
        }




        //---- START NEW ------------
        // O(n)
        public static int countRepeated(double[] a){
            if(a.length<=1){
                return 0;
            }else{
                int sum=0;
                for(int i=1; i < a.length; i++){
                    if(a[i-1]==a[i]) sum++;
                }
                return sum;
            }
        }

        public static boolean shouldCompress(int n, int n_repeated){
            return n > 2 & n_repeated > n/2;
        }

        // O(n)
        public static double[] compress(double[] a, int n_repeated){
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
        //---- END NEW ------------

        //---- START OVERRIDES ------------
        @Override
        public CompressedVintageNode cleanup(){
            return (CompressedVintageNode) super.cleanup();
        }
        @Override
        public CompressedVintageNode getRootNode(){
            return (CompressedVintageNode) super.getRootNode();
        }
        @Override
        public CompressedVintageNode getParent(){ return (CompressedVintageNode) this.parent;  }

        @Override
        public void encodeDelta(double[] s){
            super.encodeDelta(s);
            if(this.hasChanges()){
                int r  = countRepeated(this.delta);
                if(shouldCompress(this.delta.length,r)){
                    this.delta = compress(this.delta,r);
                    this.isCompressed = true;
                }
            }
        }

        @Override
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

        @Override
        public double[] decodeDelta(){
            double[] delta = this.delta;

            if(this.isCompressed){
                delta = this.decompress(delta);
            }

            if(this.isRootNode()){
                return delta;
            }else{
                double[] s1 = this.parent.decodeDelta();
                if(this.offset==-1){
                    return s1;
                }else{
                    int n = delta.length + this.offset;
                    double[] s2 = new double[n];
                    for(int i=0; i<n; i++){
                        if(i<this.offset){
                            s2[i]=s1[i];
                        }else if(i>=this.offset & i<s1.length){
                            s2[i]=s1[i]+delta[i-this.offset];
                        }else{
                            s2[i]=delta[i-this.offset];
                        }
                    }
                    return s2;
                }
            }
        }

        //---- END OVERRIDES ------------


    }

}
