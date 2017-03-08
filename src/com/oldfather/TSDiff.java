package com.oldfather;

import java.util.Arrays;

/**
 * Created by theoldfather on 1/7/17.
 */
public class TSDiff {


    public static class VintageNode  {

        public final static double TOL = 1e-12;

        public VintageNode parent = null;
        public long s_hash;
        public int offset;
        public double[] delta = null;

        public VintageNode() {
            // nothing to do here
        }

        public VintageNode(long s_hash, double[] s) {
            this.encodeDelta(s);
            this.s_hash = s_hash;
        }

        public VintageNode(long s_hash, double[] s, VintageNode parent) {
            if (parent.hasChanges()) {
                this.parent = parent;
                this.encodeDelta(s, parent.decodeDelta());
            } else {
                this.encodeDelta(s);
            }
            this.s_hash = s_hash;

        }

        public VintageNode(long s_hash, int offset, double[] delta) {
            this.s_hash = s_hash;
            this.offset = offset;
            this.delta = delta;
        }

        public VintageNode(long s_hash, int offset, double[] delta, VintageNode parent) {
            this.s_hash = s_hash;
            this.offset = offset;
            this.delta = delta;
            if (parent.hasChanges()) this.parent = parent;
        }

        public boolean hasChanges() {
            if (this.delta != null) {
                if (this.delta.length > 0) {
                    return true;
                }
            }
            return false;
        }

        public VintageNode cleanup() {
            if (this.hasChanges()) {
                return this.parent;
            } else {
                return this;
            }
        }

        public VintageNode getRootNode() {
            if (this.isRootNode()) {
                return this;
            } else {
                return this.getParent().getRootNode();
            }
        }

        public VintageNode getParent() {
            return this.parent;
        }

        public void encodeDelta(double[] s) {
            if (s != null) {
                if (s.length > 0) {
                    this.delta = s;
                    this.offset = 0;
                }
            }
        }

        /**
         * Encodes a delta for <i>s2</i> given <i>s1</i>. Asserts the length of <i>s2</i> is greater than or equal to that of <i>s1</i>,
         * otherwise we will get an error.
         * <p>
         * <b>Complixity:</b> O(n)
         *
         * @param s2 The new node for which we would like to generate a delta
         * @param s1 The previous node to be differenced against
         */
        // O(n)
        public void encodeDelta(double[] s2, double[] s1) {

            try {
                if (s2.length < s1.length)
                    throw new RuntimeException("The length of a new vintage should be greater than or equal that of the previous one.");
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            int n = s2.length;
            double[] delta = null;
            int offset = 0;
            boolean unchanged = true;
            double d;

            // iterate length of s2
            for (int i = 0; i < n; i++) {

                d = 0;
                d += s2[i];
                d -= (i < s1.length) ? s1[i] : 0;

                // does s2 contain revisions to elements of s1?
                if (Math.abs(d) > TOL) {

                    // if so, mark the offset of the first change
                    if (unchanged) {
                        unchanged = false;
                        offset = i;
                        delta = new double[n - i];
                    }
                    delta[i - offset] = d;
                }

            }
            if (!unchanged) {
                this.delta = delta;
                this.offset = offset;
            }
        }

        // k*n => O(k), where k is the number of prior vintages
        public double[] decodeDelta() {

            if (this.isRootNode()) {
                return this.delta;
            } else {
                double[] s1 = this.parent.decodeDelta();
                if (!this.hasChanges()) {
                    return s1;
                } else {
                    int n = this.delta.length + this.offset;
                    double[] s2 = new double[n];
                    for (int i = 0; i < n; i++) {
                        s2[i] += ( i < s1.length ) ? s1[i] : 0;
                        s2[i] += ( i >= this.offset ) ? this.delta[i - this.offset] : 0;
                    }
                    return s2;
                }
            }
        }

        public int getVintageNumber() {
            return 1 + (this.isRootNode() ? 0 : this.parent.getVintageNumber());
        }


        public boolean isRootNode() {
            return (this.parent == null);
        }

        public boolean hasParent() {
            return !(this.isRootNode());
        }

        public Double[] deltaToDoubleArray() {
            Double[] delta = new Double[this.delta.length];
            for (int i = 0; i < this.delta.length; i++) {
                delta[i] = this.delta[i];
            }
            return delta;
        }

    }

    public static class CompressedVintageNode extends VintageNode {

        public boolean isCompressed = false;

        public CompressedVintageNode() {

        }

        public CompressedVintageNode(long s_hash, double[] s) {
            this.encodeDelta(s);
            this.s_hash = s_hash;
        }

        public CompressedVintageNode(long s_hash, double[] s, CompressedVintageNode parent) {

            if (parent.hasChanges()) {
                this.parent = parent;
                this.encodeDelta(s, parent.decodeDelta());
            } else {
                this.encodeDelta(s);
            }
            this.s_hash = s_hash;

        }

        public CompressedVintageNode(long s_hash, int offset, double[] delta) {
            this.s_hash = s_hash;
            this.offset = offset;
            this.delta = delta;
        }

        public CompressedVintageNode(long s_hash, int offset, double[] delta, boolean isCompressed, CompressedVintageNode parent) {
            this.s_hash = s_hash;
            this.offset = offset;
            this.delta = delta;
            this.isCompressed = isCompressed;
            if (parent.hasChanges()) this.parent = parent;
        }


        //---- START NEW ------------
        // O(n)
        public static int countRepeated(double[] a) {
            if (a.length <= 1) {
                return 0;
            } else {
                int sum = 0;
                for (int i = 1; i < a.length; i++) {
                    if ( Math.abs(a[i - 1] - a[i]) < TOL ) sum++;
                }
                return sum;
            }
        }

        public static boolean shouldCompress(int n, int n_repeated) {
            return n > 2 & n_repeated > n / 2;
        }

        // O(n)
        public static double[] compress(double[] a, int n_repeated) {
            int n_unique = a.length - n_repeated;
            double[] out = new double[n_unique * 2];
            int k = 0;
            out[k] = 1;
            out[k + n_unique] = a[0];
            for (int i = 1; i < a.length; i++) {
                if (Math.abs(out[k + n_unique] - a[i]) < TOL) {
                    out[k]++;
                } else {
                    k++;
                    out[k] = 1;
                    out[k + n_unique] = a[i];
                }
            }
            return out;
        }

        // r + n => O(n)
        public static double[] decompress(double[] a) {
            int r = a.length / 2; // number of pairs
            int k = 0; // count output elements
            for (int i = 0; i < r; i++) k += a[i];
            double[] out = new double[k];
            int l = 0; // index a
            int o = 0; // index out
            while (o < k) {
                // unroll a into out
                for (int j = 0; j < a[l]; j++) {
                    out[o] = a[l + r];
                    o++;
                }
                l++;
            }
            return out;
        }
        //---- END NEW ------------

        //---- START OVERRIDES ------------
        @Override
        public CompressedVintageNode cleanup() {
            return (CompressedVintageNode) super.cleanup();
        }

        @Override
        public CompressedVintageNode getRootNode() {
            return (CompressedVintageNode) super.getRootNode();
        }

        @Override
        public CompressedVintageNode getParent() {
            return (CompressedVintageNode) this.parent;
        }

        public void applyCompression(){
            if (this.hasChanges()) {
                int r = countRepeated(this.delta);
                if (shouldCompress(this.delta.length, r)) {
                    this.delta = compress(this.delta, r);
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
        public void encodeDelta(double[] s2, double[] s1) {
            super.encodeDelta(s2, s1);
            this.applyCompression();
        }

        @Override
        public double[] decodeDelta() {
            double[] delta = this.delta;

            if (this.isCompressed) {
                delta = this.decompress(delta);
            }

            if (this.isRootNode()) {
                return delta;
            } else {
                double[] s1 = this.parent.decodeDelta();
                if (!this.hasChanges()) {
                    return s1;
                } else {
                    int n = delta.length + this.offset;
                    double[] s2 = new double[n];
                    for (int i = 0; i < n; i++) {
                        s2[i] += ( i < s1.length ) ? s1[i] : 0;
                        s2[i] += ( i >= this.offset ) ? this.delta[i - this.offset] : 0;
                    }
                    return s2;
                }
            }
        }

        //---- END OVERRIDES ------------


    }

    public static class AlignedVintageNode  {

        public final static double TOL = 1e-12;

        public AlignedVintageNode parent = null;
        public long s_hash;
        public int align;
        public int offset;
        public double[] delta=null;

        public AlignedVintageNode(){

        }

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
                this.encodeDelta(align, s, parent.align, parent.decodeDelta());
            }else if(!parent.hasChanges() & !parent.isRootNode()) {
                this.parent = parent.parent;
                this.encodeDelta(align, s, parent.parent.align, parent.parent.decodeDelta());
            }else{
                this.encodeDelta(s);
            }
            this.s_hash = s_hash;
            this.align = align;
            this.collapseParent();

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
         * Check for equality with <i>node</i> contemporaneously.
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

        public boolean hasChanges() {
            if (this.delta != null) {
                if (this.delta.length > 0) {
                    return true;
                }
            }
            return false;
        }

        public AlignedVintageNode cleanup() {
            if (!this.hasChanges()) {
                return this.parent;
            } else {
                return this;
            }
        }

        public AlignedVintageNode getRootNode() {
            if (this.isRootNode()) {
                return this;
            } else {
                return this.getParent().getRootNode();
            }
        }

        public AlignedVintageNode getParent() {
            return this.parent;
        }

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
         * <b>Complexity:</b> O(n)
         *
         * @param s2 The new node for which we would like to generate a delta
         * @param s1 The previous node to be differenced against
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
                if (d!=0) {
                    if (!found_offset) {
                        found_offset = true;
                        offset = i;
                        delta = new double[n2-offset];
                    }
                    delta[i - offset] = d;
                }
            }
            if (found_offset) {
                this.delta = delta;
                this.offset = offset;
            }
        }

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

        public int getVintageNumber() {
            return 1 + (this.isRootNode() ? 0 : this.parent.getVintageNumber());
        }


        public boolean isRootNode() {
            return (this.parent == null);
        }

        public boolean hasParent() {
            return !(this.isRootNode());
        }

        public Double[] deltaToDoubleArray() {
            Double[] delta = new Double[this.delta.length];
            for (int i = 0; i < this.delta.length; i++) {
                delta[i] = this.delta[i];
            }
            return delta;
        }

    }

    public static class CompressedAlignedVintageNode extends AlignedVintageNode {

        public boolean isCompressed = false;

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
                this.encodeDelta(align,s,parent.align,parent.decodeDelta());
            }else if(!parent.hasChanges() & !parent.isRootNode()) {
                this.parent = parent.parent;
                this.encodeDelta(align,s,parent.parent.align,parent.parent.decodeDelta());
            }else{
                this.encodeDelta(s);
            }
            this.s_hash = s_hash;
            this.align = align;
            this.collapseParent();
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

        //---- START NEW ------------
        // O(n)
        public static int countRepeated(double[] a) {
            if (a.length <= 1) {
                return 0;
            } else {
                int sum = 0;
                for (int i = 1; i < a.length; i++) {
                    if ( a[i - 1]==a[i] ) sum++;
                }
                return sum;
            }
        }

        public static boolean shouldCompress(int n, int n_repeated) {
            return n > 2 & n_repeated > n / 2;
        }

        // O(n)
        public static double[] compress(double[] a, int n_repeated) {
            int n_unique = a.length - n_repeated;
            double[] out = new double[n_unique * 2];
            int k = 0;
            out[k] = 1;
            out[k + n_unique] = a[0];
            for (int i = 1; i < a.length; i++) {
                if (out[k + n_unique]==a[i]) {
                    out[k]++;
                } else {
                    k++;
                    out[k] = 1;
                    out[k + n_unique] = a[i];
                }
            }
            return out;
        }

        // r + n => O(n)
        public static double[] decompress(double[] a) {
            int r = a.length / 2; // number of pairs
            int k = 0; // count output elements
            for (int i = 0; i < r; i++) k += a[i];
            double[] out = new double[k];
            int l = 0; // index a
            int o = 0; // index out
            while (o < k) {
                // unroll 'a' into out
                for (int j = 0; j < a[l]; j++) {
                    out[o] = a[l + r];
                    o++;
                }
                l++;
            }
            return out;
        }
        //---- END NEW ------------

        //---- START OVERRIDES ------------


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

        public void applyCompression(){
            if (this.hasChanges()) {
                int r = countRepeated(this.delta);
                if (shouldCompress(this.delta.length, r)) {
                    this.delta = compress(this.delta, r);
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
                delta = this.decompress(delta);
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

        //---- END OVERRIDES ------------


    }

}
