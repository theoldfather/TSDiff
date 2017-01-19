import java.util.Arrays;

/**
 * Created by theoldfather on 1/7/17.
 */
public class TSDiff {

    public static class VintageNode{

        VintageNode parent=null;
        int s_hash;
        int offset=0;
        double[] delta;

        public VintageNode(double[] s){
            this.delta = s;
            this.s_hash = Arrays.hashCode(s);
        }
        public VintageNode(double[] s, VintageNode parent){
            this.parent=parent;
            this.s_hash = Arrays.hashCode(s);
            encodeDelta(s,parent.decodeDelta());
        }
        public VintageNode(int s_hash,int offset, double[] delta){
            this.s_hash=s_hash;
            this.offset=offset;
            this.delta=delta;
        }
        public VintageNode(int s_hash,int offset, double[] delta, VintageNode parent){
            this.s_hash=s_hash;
            this.offset=offset;
            this.delta=delta;
            this.parent=parent;
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

        public static double[] decodeDelta(VintageNode v){

            if(v.parent==null){
                return v.delta;
            }else{
                double[] s1 = decodeDelta(v.parent);
                if(v.offset==-1){
                    return s1;
                }else{
                    int n = v.delta.length + v.offset;
                    double[] s2 = new double[n];
                    for(int i=0; i<n; i++){
                        if(i<v.offset){
                            s2[i]=s1[i];
                        }else if(i>=v.offset & i<s1.length){
                            s2[i]=s1[i]+v.delta[i-v.offset];
                        }else{
                            s2[i]=v.delta[i-v.offset];
                        }
                    }
                    return s2;
                }
            }
        }

        public double[] decodeDelta(){
            return VintageNode.decodeDelta(this);
        }
    }

    public static void main(String[] args){
        System.out.println("hello");

        double[] a = {0,1,2,3,4};
        double[] b = {0,1,2,3,4,5};
        double[] c = {0,1,2,3,4,5,6,7,10,11,12};
        System.out.println(Arrays.toString(a));
        System.out.println(Arrays.toString(b));
        System.out.println(Arrays.toString(c));


        System.out.println(Arrays.hashCode(a));
        System.out.println(Arrays.hashCode(b));
        System.out.println(Arrays.hashCode(c));

        VintageNode A = new VintageNode(a);
        VintageNode B = new VintageNode(b,A);
        VintageNode C = new VintageNode(c,B);

        System.out.println(Arrays.toString(A.delta));
        System.out.println(Arrays.toString(B.delta));
        System.out.printf("%s: %s\n",Arrays.toString(C.delta),C.offset);

        System.out.println(Arrays.toString(C.decodeDelta()));
    }
}
