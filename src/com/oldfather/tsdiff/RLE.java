package com.oldfather.tsdiff;

/**
 * Provides Run-Length Encoding
 */
public class RLE{

    /**
     * Tolerance for double-comparison
     */
    public final static double TOL = 1e-12;


    /**
     * Counts the number of repeated elements in a <code>double[]</code>.
     *
     * @param a a <code>double[]</code>
     * @return The number of duplicate elements (excluding the first instance).
     * @algo.complexity O(n)
     */
    // O(n)
    public static int measureSavings(double[] a) {
        if (a.length <= 1) {
            return 0;
        } else {
            int sum = 0;
            for (int i = 1; i < a.length; i++) {
                if (Math.abs(a[i] - a[i - 1])<TOL) sum++;
            }
            return sum;
        }
    }

    /**
     * Counts the number of repeated elements in a <code>int[]</code>.
     *
     * @param a a <code>int[]</code>
     * @return The number of duplicate elements (excluding the first instance).
     * @algo.complexity O(n)
     */
    // O(n)
    public static int countRepeated(int[] a) {
        if (a.length <= 1) {
            return 0;
        } else {
            int sum = 0;
            for (int i = 1; i < a.length; i++) {
                if ( a[i]==a[i - 1] ) sum++;
            }
            return sum;
        }
    }

    /**
     * Decision rule for RLE compression
     * @param n Length of series
     * @param n_repeated Number of repeated elements in the series
     * @return  True if compression would lead to space reduction.
     *
     * @algo.complexity O(1)
     */
    public static boolean shouldCompress(int n, int n_repeated) {
        return n > 2 & n_repeated > n / 2;
    }

    /**
     * Compress an array
     *
     * @param a array to be compressed
     * @param n_repeated number of repeated elements
     * @return a compressed array
     *
     * @algo.complexity O(n)
     */
    // O(n)
    public static double[] compress(double[] a, int n_repeated) {
        int n_unique = a.length - n_repeated;
        double[] out = new double[n_unique * 2];
        int k = 0;
        out[k] = 1;
        out[k + n_unique] = a[0];
        for (int i = 1; i < a.length; i++) {
            if (Math.abs(out[k + n_unique]-a[i])<TOL) {
                out[k]++;
            } else {
                k++;
                out[k] = 1;
                out[k + n_unique] = a[i];
            }
        }
        return out;
    }

    /**
     * Compress an array
     *
     * @param a array to be compressed
     * @param n_repeated number of repeated elements
     * @return a compressed array
     *
     * @algo.complexity O(n)
     */
    // O(n)
    public static int[] compress(int[] a, int n_repeated) {
        int n_unique = a.length - n_repeated;
        int[] out = new int[n_unique * 2];
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


    /**
     * Decompressed RLE-type compressed <code>double[]</code>
     * @param a a compressed <code>double[]</code>
     * @return a decompressed <code>double[]</code>
     *
     * @algo.complexity O(n)
     */
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

    /**
     * Decompressed RLE-type compressed <code>int[]</code>
     * @param a a compressed <code>int[]</code>
     * @return a decompressed <code>int[]</code>
     *
     * @algo.complexity O(n)
     */
    // r + n => O(n)
    public static int[] decompress(int[] a) {
        int r = a.length / 2; // number of pairs
        int k = 0; // count output elements
        for (int i = 0; i < r; i++) k += a[i];
        int[] out = new int[k];
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
