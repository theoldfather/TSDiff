package com.oldfather;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by theoldfather on 2/7/17.
 */
public class VintageNodeTest {

    public double[] t0 = {};
    public double[] t1 = range(1);
    public double[] t2 = range(2);
    public double[] t3 = range(3);

    public TSDiff.VintageNode a = new TSDiff.VintageNode(t0.hashCode(),t0);
    public TSDiff.VintageNode b = new TSDiff.VintageNode(t1.hashCode(),t1,a);
    public TSDiff.VintageNode c = new TSDiff.VintageNode(t2.hashCode(),t2,b);
    public TSDiff.VintageNode d = new TSDiff.VintageNode(t3.hashCode(),t3,c);

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

        assertThat("class of node should be VintageNode",c.getClass().getName(),equalTo("com.oldfather.TSDiff$VintageNode"));
    }

    @Test
    public void hasChanges() throws Exception {
        DebugTools.printActiveClassMethodName();

        assertThat("an empty delta has no changes",a.hasChanges(),equalTo(false));

        assertThat("an non-empty delta has no changes",b.hasChanges(),equalTo(true));
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

    }

}