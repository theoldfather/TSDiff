package com.oldfather;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

import java.util.Arrays;
import com.oldfather.TSDiff.*;


/**
 * Created by theoldfather on 2/5/17.
 */
public class CompressedVintageNodeTest extends VintageNodeTest{

    public double[] t0 = {};
    public double[] t1 = range(1);
    public double[] t2 = range(2);
    public double[] t3 = range(3);

    CompressedVintageNode util = new CompressedVintageNode();

    public CompressedVintageNode a = new CompressedVintageNode(t0.hashCode(),t0);
    public CompressedVintageNode b = new CompressedVintageNode(t1.hashCode(),t1,a);
    public CompressedVintageNode c = new CompressedVintageNode(t2.hashCode(),t2,b);
    public CompressedVintageNode d = new CompressedVintageNode(t3.hashCode(),t3,c);

    public double[] t4 = range(1);
    public double[] t5 = range(2);

    public CompressedVintageNode e = new CompressedVintageNode(t4.hashCode(),t4);
    public CompressedVintageNode f = new CompressedVintageNode(t5.hashCode(),t5,e);

    public double[] t6 = {0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,3,3,3,3,3};
    public CompressedVintageNode g = new CompressedVintageNode(t6.hashCode(),t6);


    @Test
    public void checkClass() throws Exception {
        DebugTools.printActiveClassMethodName();

        System.out.println(c.getClass().getName());
        assertThat(c.getClass().getName(),equalTo("com.oldfather.TSDiff$CompressedVintageNode"));
    }

    @Test
    public void compress() throws Exception {
        DebugTools.printActiveClassMethodName();

        assertThat(util.compress(t1, util.countRepeated(t1)),equalTo(new double[]{1,0}));
        assertThat(util.compress(t2, util.countRepeated(t2)),equalTo(new double[]{1,1,0,1}));
        assertThat(util.compress(t6, util.countRepeated(t6)),equalTo(new double[]{7,8,5,0,1,3}));

    }

    @Test
    public void decompress() throws Exception {
        DebugTools.printActiveClassMethodName();

        double[] ct1 = util.compress(t1, util.countRepeated(t1));
        double[] ct2 = util.compress(t2, util.countRepeated(t2));
        double[] ct6 = util.compress(t6, util.countRepeated(t6));

        assertThat(util.decompress(ct1),equalTo(t1));

        assertThat(util.decompress(ct2),equalTo(t2));

        assertThat(util.decompress(ct6),equalTo(t6));

    }

    @Test
    public void getRootNode() throws Exception {
        DebugTools.printActiveClassMethodName();

        // assert first node is the root when there is only 1 vintage
        assertThat("",a.getRootNode(),equalTo(a));

        // assert second node is root if first node empty
        assertThat("empty nodes at root of chain should get dropped.",b.getRootNode(),equalTo(b));

        System.out.println(c.getParent()==null);

        System.out.println(Arrays.toString(c.decodeDelta()));

        // assert second node is root if first node empty
        assertThat("second non-empty node should not be root",c.getRootNode(),equalTo(b));


    }

    @Test
    public void encodeDelta() throws Exception {
        DebugTools.printActiveClassMethodName();

        System.out.println(Arrays.toString(a.delta));
        assertThat(a.delta,equalTo(null));
        assertThat(a.isCompressed,equalTo(false));

        System.out.println(Arrays.toString(b.delta));
        assertThat(b.delta,equalTo(new double[]{0}));
        assertThat(b.isCompressed,equalTo(false));

        System.out.println(Arrays.toString(c.delta));
        assertThat(c.delta,equalTo(new double[]{1}));
        assertThat(c.isCompressed,equalTo(false));

        System.out.println(Arrays.toString(e.delta));
        assertThat(e.delta,equalTo(new double[]{0}));
        assertThat(e.isCompressed,equalTo(false));

        System.out.println(Arrays.toString(f.delta));
        assertThat(f.delta,equalTo(new double[]{1}));
        assertThat(f.isCompressed,equalTo(false));

        System.out.println(Arrays.toString(g.delta));
        assertThat(g.delta,equalTo(new double[]{7,8,5,0,1,3}));
        assertThat(g.isCompressed,equalTo(true));

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

        System.out.println(Arrays.toString(e.decodeDelta()));
        assertThat(e.decodeDelta(),equalTo(t4));

        System.out.println(Arrays.toString(f.decodeDelta()));
        assertThat(f.decodeDelta(),equalTo(t5));

    }

}