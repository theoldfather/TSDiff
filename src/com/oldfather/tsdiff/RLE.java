package com.oldfather.tsdiff;


/**
 * Provides Run-Length Encoding
 */
public class RLE{

    /**
     * Tolerance for double-comparison
     */
    public double TOL = 1e-12;

    /**
     * Default constructor that accepts the given tolerance of 1e-12
     */
    public RLE(){}

    /**
     * Constructor that sets the tolerance for comparing equality of doubles
     * @param tolerance
     */
    public RLE(double tolerance){
        this.TOL = tolerance;
    }


    private boolean isEqual(double a, double b){
        return Math.abs(a-b) < this.TOL;
    }

    /**
     * Counts the number of repeated elements in a <code>double[]</code>.
     *
     * @param a a <code>double[]</code>
     * @return The number of duplicate elements (excluding the first instance).
     * @algo.complexity O(n)
     */
    // O(n)
    public int countRuns(double[] a) {
        if (a.length == 0) {
            return 0;
        } else {
            int sum = 1; // i=0 is the first run
            for (int i = 1; i < a.length; i++) {
                if (!isEqual(a[i],a[i - 1])) sum++;
            }
            return sum;
        }
    }

    /**
     * Decision rule for RLE compression
     * @param n Length of series
     * @param n_runs Number of runs in the series
     * @return  True if compression would lead to space reduction.
     *
     * @algo.complexity O(1)
     */
    public boolean shouldCompress(int n, int n_runs) {
        return n > 2 & (2*n_runs < n);
    }

    /**
     * Compress an array
     *
     * @param a array to be compressed
     * @param n_runs number of runs
     * @return a compressed array
     *
     * @algo.complexity O(n)
     */
    // O(n)
    public double[] compress(double[] a, int n_runs) {
        double[] out = new double[n_runs * 2];
        int k = 0;
        out[k] = 1;
        out[k + n_runs] = a[0];
        for (int i = 1; i < a.length; i++) {
            if (isEqual(a[i-1],a[i])) {
                out[k]++;
            } else {
                k++;
                out[k] = 1;
                out[k + n_runs] = a[i];
            }
        }
        return out;
    }


    /**
     * Decompressed RLE-type compressed <code>double[]</code>
     * @param a a compressed <code>double[]</code>
     * @return a decompressed <code>double[]</code>
     *
     * @algo.complexity O(n)
     */
    // r + n => O(n)
    public double[] decompress(double[] a) {
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
}
