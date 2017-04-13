package com.oldfather.tsdiff;

import com.oldfather.TSDiff.RLE;

import java.util.Arrays;

/**
 * VintageNode that maintains alignment
 */
public class AlignedVintageNode  {

    public final static double TOL = 1e-2;

    public AlignedVintageNode parent = null;
    public long s_hash;
    public int align;
    public int offset;
    public double[] delta=null;

    /**
     * Empty Constructor
     */
    public AlignedVintageNode(){

    }

    /**
     * Constructor that clones another <code>AlignedVintageNode</code>
     * @param node
     */
    public AlignedVintageNode(AlignedVintageNode node) {
        this.fromNode(node);
    }


    public AlignedVintageNode(long s_hash, double[] s) {
        this.encodeDelta(s);
        this.s_hash = s_hash;
    }

    public AlignedVintageNode(long s_hash, int align, double[] s, AlignedVintageNode parent) {
        if (parent.hasChanges()) {
            this.parent = parent;
            this.collapseParent();
            this.encodeDelta(align, s, parent.align, parent.decodeDelta());
        }else if(!parent.hasChanges() & !parent.isRootNode()) {
            this.parent = parent.parent;
            this.collapseParent();
            this.encodeDelta(align, s, parent.parent.align, parent.parent.decodeDelta());
        }else{
            this.encodeDelta(s);
        }
        this.s_hash = s_hash;
        this.align = align;
    }

    public AlignedVintageNode(long s_hash, int align, int offset, double[] delta) {
        this.s_hash = s_hash;
        this.align = align;
        this.offset = offset;
        this.delta = delta;
    }

    public AlignedVintageNode(long s_hash, int align, int offset, double[] delta, AlignedVintageNode parent) {
        this(s_hash, align, offset, delta);
        this.parent = parent;
        this.collapseParent();
    }


    /**
     * Maps <code>i</code>, an index in the space of <code>a</code>, to an index in the space of <code>b</code>.
     *
     * @param i an index in the space of <code>a</code>
     * @param a0 the alignment of a
     * @param b0 the alignment of b
     * @return an index in the space of <code>b</code>
     */
    public static int mapAtoB(int i, int a0, int b0){
        return(i - (a0-b0));
    }

    public void fromNode(AlignedVintageNode node){
        this.s_hash = node.s_hash;
        this.align = node.align;
        this.offset = node.offset;
        this.delta = node.delta;
        this.parent = node.parent;
    }

    /**
     * Check for equality with <code>node</code> contemporaneously.
     *
     * @param node     Node for comparison
     * @return  True if nodes match on all contemporaneous characteristics.
     */
    public boolean equalTo(AlignedVintageNode node){
        return this.s_hash==node.s_hash
                & this.align==node.align
                & this.offset==node.offset
                & Arrays.equals(this.delta,node.delta);

    }


    public void collapseParent(){
        if(!this.isRootNode()){
            if(!this.parent.isRootNode()){
                if(this.s_hash == this.parent.s_hash){
                    this.parent = this.parent.parent;
                }
            }
        }
    }

    /**
     * Does this node contain information not found in the previous vintage?
     * @return <code>True</code> if this node has new information
     */
    public boolean hasChanges() {
        if (this.delta != null) {
            return true;
        }
        return false;
    }

    /**
     * Cleans-up vintages that do not contain changes
     * @return the nearest leaf node with changes
     */
    public AlignedVintageNode cleanup() {
        if (this.hasChanges()) {
            return this;
        } else {
            if(this.hasParent()){
                return this.getParent().cleanup();
            }else{
                return this.getParent();
            }
        }
    }

    /**
     * Gets the root node of this list
     * @return the root node
     */
    public AlignedVintageNode getRootNode() {
        if (this.isRootNode()) {
            return this;
        } else {
            return this.getParent().getRootNode();
        }
    }

    /**
     * Gets the parent node of the current node
     * @return the parent node
     */
    public AlignedVintageNode getParent() {
        return this.parent;
    }

    /**
     * Encodes the trivial case where this node is the root.
     * @param s the root vintage series
     */
    public void encodeDelta(double[] s) {
        if (s != null) {
            if (s.length > 0) {
                this.delta = s;
                this.offset = 0;
                this.align = 0;
            }
        }
    }

    /**
     * Encodes a delta for <i>s2</i> given <i>s1</i>. Assumes that the union of <i>s1</i> and <i>s2</i> contains no missing elements.
     *
     * @algo.complexity  O(n)
     *
     * @param a2 Alignment of the new vintage, <code>s2</code>.
     * @param s2 New vintage series for which we would like to generate a delta.
     * @param a1 Alignment of the previous vintage, <code>s1</code>.
     * @param s1 Previous vintage series to be differenced against.
     */
    // O(n)
    public void encodeDelta(int a2, double[] s2, int a1, double[] s1) {

        double[] delta = null;
        int offset = 0;
        int j;
        double d;
        boolean found_offset = false;

        int n1 = s1.length;
        int n2 = s2.length;

        // iterate length of longest possible delta
        for (int i = 0; i < n2; i++) {
            j = mapAtoB(i,a2,a1);
            d = s2[i] - ((0 <= j & j < n1 ) ? s1[j] : 0);
            if (Math.abs(d)>TOL) {
                if (!found_offset) {
                    found_offset = true;
                    offset = i;
                    delta = new double[n2-offset];
                }
                delta[i - offset] = d;
            }
        }
        // check case where delta should be empty and still saved
        // ie, when the start date shifts forward, but all values are identical to previous vintage
        int last_i = n2-1;
        j = mapAtoB(last_i,a2,a1);
        if(a2>a1 & !found_offset & (0 <= j & j < n1) ){
            delta = new double[0];
            offset = last_i;
            found_offset = true;
        }

        if (found_offset) {
            this.delta = delta;
            this.offset = offset;
        }
    }

    /**
     * Decodes a vintage series the vintage node instance.
     *
     * @algo.complexity k*mean(n), where k is the number of vintages and mean(n) is the average vintage length.
     *
     * @return A decoded vintage series
     */
    // k*n => O(k), where k is the number of prior vintages
    public double[] decodeDelta() {

        if (this.isRootNode()) {
            return this.delta;
        } else {

            double[] s1 = this.parent.decodeDelta();

            if (!this.hasChanges()) {
                return s1;
            } else {
                int a1 = this.parent.align;
                int a2 = this.align;
                int n1 = s1.length;
                int n2 = this.delta.length + this.offset;
                int j;
                double[] s2 = new double[n2];
                for (int i = 0; i < n2; i++) {
                    j = mapAtoB(i,a2,a1);
                    s2[i] = (this.offset <= i) ? this.delta[i-this.offset] : 0;
                    s2[i] += (0 <= j & j < n1) ? s1[j] : 0;
                }
                return s2;
            }
        }
    }


    /**
     * Gives the index of the current vintage
     * @return The index of the current vintage, eg. the root node returns 1, the next 2, and so on
     */
    public int getVintageNumber() {
        return 1 + (this.isRootNode() ? 0 : this.parent.getVintageNumber());
    }

    /**
     * Checks if this node is the root
     * @return True if this instance has no parent         *
     */
    public boolean isRootNode() {
        return (this.parent == null);
    }

    /**
     * Checks if this node has a parent
     * @return True if this instance is not a root node.
     */
    public boolean hasParent() {
        return !(this.isRootNode());
    }

    /**
     * Converts the delta double[] to Double[]
     * @return delta as Double[]
     */
    public Double[] deltaToDoubleArray() {
        Double[] delta = new Double[this.delta.length];
        for (int i = 0; i < this.delta.length; i++) {
            delta[i] = this.delta[i];
        }
        return delta;
    }

    public AlignedVintageNode getVintage(long queryHash){
        if(queryHash >= s_hash){
            return this;
        }else{
            if(this.hasParent()){
                return this.parent.getVintage(queryHash);
            }else{
                System.out.println("none");
                return null;
            }
        }
    }

    public double[] getVintageSeries(long queryHash){
        if(queryHash >= s_hash){
            return decodeDelta();
        }else{
            if(this.hasParent()){
                return parent.getVintageSeries(queryHash);
            }else{
                return null;
            }
        }
    }


}
