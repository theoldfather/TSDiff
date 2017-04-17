package com.oldfather.tsdiff;

/**
 * Created by theoldfather on 4/8/17.
 */
public class CompressedAlignedVintageNode extends AlignedVintageNode {

    public boolean isCompressed = false;
    public RLE rle = new RLE(1e-12);

    public CompressedAlignedVintageNode(){

    }

    public CompressedAlignedVintageNode(CompressedAlignedVintageNode node) {
        this.fromNode(node);
    }

    public CompressedAlignedVintageNode(long s_hash,  double[] s) {
        this.encodeDelta(s);
        this.s_hash = s_hash;
    }

    public CompressedAlignedVintageNode(long s_hash, int align, double[] s, CompressedAlignedVintageNode parent) {
        if (parent.hasChanges()) {
            this.parent = parent;
            this.collapseParent();
            this.encodeDelta(align,s,parent.align,parent.decodeDelta());
        }else if(!parent.hasChanges() & !parent.isRootNode()) {
            this.parent = parent.parent;
            this.collapseParent();
            this.encodeDelta(align,s,parent.parent.align,parent.parent.decodeDelta());
        }else{
            this.encodeDelta(s);
        }
        this.s_hash = s_hash;
        this.align = align;
    }

    public CompressedAlignedVintageNode(long s_hash, int align, double[] s, CompressedAlignedVintageNode parent, double[] parent_s) {
        if (parent.hasChanges()) {
            this.parent = parent;
            this.collapseParent();
            this.encodeDelta(align,s,parent.align,parent_s);
        }else if(!parent.hasChanges() & !parent.isRootNode()) {
            this.parent = parent.parent;
            this.collapseParent();
            this.encodeDelta(align,s,parent.parent.align,parent.parent.decodeDelta());
        }else{
            this.encodeDelta(s);
        }
        this.s_hash = s_hash;
        this.align = align;
    }

    public CompressedAlignedVintageNode(long s_hash, int align, int offset, double[] delta) {
        this.s_hash = s_hash;
        this.align = align;
        this.offset = offset;
        this.delta = delta;
    }

    public CompressedAlignedVintageNode(long s_hash, int align, int offset, double[] delta, boolean isCompressed, CompressedAlignedVintageNode parent) {
        this(s_hash,align,offset,delta);
        this.isCompressed = isCompressed;
        this.parent = parent;
        this.collapseParent();
    }

    public void fromNode(CompressedAlignedVintageNode node){
        super.fromNode(node);
        this.isCompressed = node.isCompressed;
    }

    /**
     * Check for equality with <i>node</i> contemporaneously.
     *
     * @param node     Node for comparison
     * @return  True is nodes match on all contemporaneous characteristics.
     */
    public boolean equalTo(CompressedAlignedVintageNode node){
        return super.equalTo(node) & this.isCompressed==node.isCompressed;
    }


    @Override
    public CompressedAlignedVintageNode cleanup() {
        return (CompressedAlignedVintageNode) super.cleanup();
    }

    @Override
    public CompressedAlignedVintageNode getRootNode() {
        return (CompressedAlignedVintageNode) super.getRootNode();
    }

    @Override
    public CompressedAlignedVintageNode getParent() {
        return (CompressedAlignedVintageNode) this.parent;
    }

    @Override
    public CompressedAlignedVintageNode getVintage(long queryHash){
        return (CompressedAlignedVintageNode)super.getVintage(queryHash);
    }

    public void applyCompression(){
        if (this.hasChanges()) {
            int n_runs = rle.countRuns(this.delta);
            if (rle.shouldCompress(this.delta.length, n_runs)) {
                this.delta = rle.compress(this.delta, n_runs);
                this.isCompressed = true;
            }
        }
    }

    @Override
    public void encodeDelta(double[] s) {
        super.encodeDelta(s);
        this.applyCompression();
    }

    @Override
    public void encodeDelta(int a2, double[] s2, int a1, double[] s1) {
        super.encodeDelta(a2,s2,a1,s1);
        this.applyCompression();
    }

    @Override
    public double[] decodeDelta() {
        double[] delta = this.delta;

        if (this.isCompressed) {
            delta = rle.decompress(delta);
        }

        if (this.isRootNode()) {
            return delta;
        } else {
            double[] s1 = this.parent.decodeDelta();

            if (!this.hasChanges()) {
                return s1;
            } else {
                int a1 = this.parent.align;
                int a2 = this.align;
                int n1 = s1.length;
                int n2 = delta.length + this.offset;
                int j;
                double[] s2 = new double[n2];
                for (int i = 0; i < n2; i++) {
                    j = mapAtoB(i,a2,a1);
                    s2[i] = (this.offset <= i) ? delta[i-this.offset] : 0;
                    s2[i] += (0 <= j & j < n1) ? s1[j] : 0;
                }
                return s2;
            }
        }
    }

}
