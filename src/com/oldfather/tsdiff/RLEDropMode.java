package com.oldfather.tsdiff;

/**
 * Provides Run-Length Encoding where the given mode value is dropped and represented as a negative count.
 */
public class RLEDropMode {

    /**
     * Tolerance for double-comparison
     */
    public double TOL = 1e-12;
    public double mode = 0;

    public RLEDropMode(){}

    public RLEDropMode(double mode, double tolerance){
        this.mode = mode;
        this.TOL = tolerance;
    }

    /**
     * Counts the number of runs a <code>double[]</code>.
     *
     * @param a a <code>double[]</code>
     * @return an int[] containing the number of runs {n_mode, n_other}
     * @algo.complexity O(n)
     */
    // O(n)
    public int[] countRuns(double[] a) {
        if (a.length==0) {
            return new int[]{0,0};
        } else {
            int n_mode = 0;
            int n_other = 0;

            // i=0
            if(!equalsMode(a[0])){
                n_other++;
            }else{
                n_mode++;
            }

            // i>0
            for (int i = 1; i < a.length; i++) {
                // identify beginning of runs
                if(!isEqual(a[i],a[i-1])){
                    if(!equalsMode(a[i])){
                        n_other++;
                    }else{
                        n_mode++;
                    }
                }
            }
            return new int[]{n_mode,n_other};
        }
    }

    /**
     * Decision rule for RLE compression
     * @param n Length of series
     * @param n_runs Number of runs in the series {n_mode, n_other}
     * @return  True if compression would lead to space reduction.
     *
     * @algo.complexity O(1)
     */
    public static boolean shouldCompress(int n, int[] n_runs) {
        int n_mode = n_runs[0];
        int n_other = n_runs[1];
        return n > 2 & (n_mode + 2*n_other < n);
    }

    private boolean isEqual(double a, double b){
        return Math.abs(a-b) < this.TOL;
    }

    public boolean equalsMode(double v){
        return isEqual(v,mode);
    }

    /**
     * Compress an array
     *
     * @param a array to be compressed
     * @param n_runs number of runs {n_mode, n_other}
     * @return a compressed array
     *
     * @algo.complexity O(n)
     */
    // O(n)
    public double[] compress(double[] a, int[] n_runs) {
        int n_mode = n_runs[0];
        int n_other = n_runs[1];
        double[] out = new double[2*n_other + n_mode];
        int jn = 0;                 // start index for counts
        int jv = n_other + n_mode;  // start index for values
        boolean is_mode;

        // i=0
        if(equalsMode(a[0])){
            out[jn]--;
        }else{
            out[jn]++;
            out[jv]=a[0];
        }
        // i>0
        for(int i=1; i<a.length; i++){
            is_mode = equalsMode(a[i]);
            if(!isEqual(a[i],a[i-1])){
                jn++;
                if(!is_mode){
                    jv++;
                    out[jv] = a[i];
                }
            }
            out[jn] += (is_mode ? -1 : 1);
        }
        return out;
    }

    /**
     * Decompressed RLEDropMode-type compressed <code>double[]</code>
     * @param a a compressed <code>double[]</code>
     * @return a decompressed <code>double[]</code>
     *
     * @algo.complexity O(n)
     */
    // r + n => O(n)
    public double[] decompress(double[] a) {
        int r_other = 0;
        int n_mode = 0;
        int n_other = 0;
        int i = 0;
        while(i + r_other < a.length){
            if(a[i]>0){
                r_other++;
                n_other += a[i];
            }else{
                n_mode += -a[i];
            }
            i++;
        }
        int r_mode = i - r_other;

        int k = n_mode + n_other; // count output elements
        double[] out = new double[k];
        int l = 0; // index a
        int o = 0; // index out
        while (o < k) {
            // unroll 'a' into out
            for (int j = 0; j < Math.abs(a[l]); j++) {
                out[o] = (a[l]<0) ? this.mode : a[l + r_mode + r_other];
                o++;
            }
            if((a[l]<0)) r_mode--;
            l++;
        }
        return out;
    }
}
