package com.oldfather;

import com.oldfather.TSDiff.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by theoldfather on 2/7/17.
 */
public class AlignedVintageNodeTest {

    public double[] t0 = {};
    public double[] t1 = range(1);
    public double[] t2 = range(2);
    public double[] t3 = range(3);

    public AlignedVintageNode a = new AlignedVintageNode(t0.hashCode(),t0);
    public AlignedVintageNode b = new AlignedVintageNode(t1.hashCode(),0,t1,a);
    public AlignedVintageNode c = new AlignedVintageNode(t2.hashCode(),0,t2,b);
    public AlignedVintageNode d = new AlignedVintageNode(t3.hashCode(),0,t3,c);

    public double[] t0_1 = {};
    public double[] t1_1 = {0,1};
    public double[] t2_1 = {-1,0,1};
    public double[] t3_1 = {1,2,3,3,3};
    public double[] t4_1 = {3,4,5,6,6,6,6,6,6,6};
    public double[] t5_1 = {3,4,5,6,6,6,6,6,6,6};
    public double[] t6_1 = {0,1,2,3,4,5};

    public AlignedVintageNode a_1 = new AlignedVintageNode(t0_1.hashCode(),t0_1);
    public AlignedVintageNode b_1 = new AlignedVintageNode(t1_1.hashCode(),0,t1_1,a_1);
    public AlignedVintageNode c_1 = new AlignedVintageNode(t2_1.hashCode(),-1,t2_1,b_1);
    public AlignedVintageNode d_1 = new AlignedVintageNode(t3_1.hashCode(),1,t3_1,c_1);
    public AlignedVintageNode e_1 = new AlignedVintageNode(t4_1.hashCode(),3,t4_1,d_1);
    public AlignedVintageNode f_1 = new AlignedVintageNode(t5_1.hashCode(),3,t5_1,e_1);
    public AlignedVintageNode g_1 = new AlignedVintageNode(t6_1.hashCode(),0,t6_1,f_1);

    public double[] randSeries(int n){
        double[] s = new double[n];
        Random rand = new Random();
        for(int i=0; i<n; i++){
            s[i]=rand.nextDouble();
        }
        return s;
    }

    public double[] range(int start,int end){
        double[] s = new double[end-start];
        for(int i=0; i<s.length; i++){
            s[i]=i+start;
        }
        return s;
    }

    public double[] range(int end){
        return range(0,end);
    }

    @Test
    public void checkClass() throws Exception {
        DebugTools.printActiveClassMethodName();

        assertThat("class of node should be AlignedVintageNode",c.getClass().getName(),equalTo("com.oldfather.TSDiff$AlignedVintageNode"));
    }

    @Test
    public void hasChanges() throws Exception {
        DebugTools.printActiveClassMethodName();

        assertThat("an empty delta has no changes",a.hasChanges(),equalTo(false));

        assertThat("an non-empty delta has changes",b.hasChanges(),equalTo(true));
    }

    @Test
    public void getRootNode() throws Exception {
        DebugTools.printActiveClassMethodName();

        // assert first node is the root when there is only 1 vintage
        assertThat("",a.getRootNode(),equalTo(a));

        // assert second node is root if first node empty
        assertThat("empty nodes at root of chain should get dropped.",b.getRootNode(),equalTo(b));

        // assert second node is root if first node empty
        assertThat("second non-empty node should not be root",c.getRootNode(),equalTo(b));

    }

    @Test
    public void encodeDelta() throws Exception {
        DebugTools.printActiveClassMethodName();

        System.out.println(Arrays.toString(a.delta));
        assertThat(a.delta,equalTo(null));

        System.out.println(Arrays.toString(b.delta));
        assertThat(b.delta,equalTo(new double[]{0}));

        System.out.println(Arrays.toString(c.delta));
        assertThat(c.delta,equalTo(new double[]{1}));

    }

    @Test
    public void decodeDelta() throws Exception {
        DebugTools.printActiveClassMethodName();

        System.out.println(Arrays.toString(a.decodeDelta()));
        assertThat(a.decodeDelta(),equalTo(null));

        System.out.println(Arrays.toString(b.decodeDelta()));
        assertThat(b.decodeDelta(),equalTo(t1));

        System.out.println(Arrays.toString(c.decodeDelta()));
        assertThat(c.decodeDelta(),equalTo(t2));


        System.out.println(Arrays.toString(a_1.decodeDelta()));
        assertThat(a_1.decodeDelta(),equalTo(null));

        System.out.println(Arrays.toString(b_1.decodeDelta()));
        assertThat(b_1.decodeDelta(),equalTo(t1_1));

        System.out.println(Arrays.toString(c_1.decodeDelta()));
        assertThat(c_1.decodeDelta(),equalTo(t2_1));

        System.out.println(Arrays.toString(d_1.decodeDelta()));
        assertThat(d_1.decodeDelta(),equalTo(t3_1));

        System.out.println(Arrays.toString(e_1.decodeDelta()));
        assertThat(e_1.decodeDelta(),equalTo(t4_1));

        System.out.println(Arrays.toString(f_1.decodeDelta()));
        assertThat(f_1.decodeDelta(),equalTo(t5_1));
        assertThat(f_1.hasChanges(),equalTo(false));

        System.out.println(Arrays.toString(g_1.delta));
        System.out.println(Arrays.toString(g_1.decodeDelta()));
        assertThat(g_1.hasChanges(),equalTo(true));
        assertThat(g_1.decodeDelta(),equalTo(t6_1));

    }

}