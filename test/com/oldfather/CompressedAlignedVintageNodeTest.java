package com.oldfather;

import com.oldfather.TSDiff.CompressedAlignedVintageNode;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


/**
 * Created by theoldfather on 2/5/17.
 */
public class CompressedAlignedVintageNodeTest extends AlignedVintageNodeTest{

    public double[] t0 = {};
    public double[] t1 = range(1);
    public double[] t2 = range(2);
    public double[] t3 = range(3);

    CompressedAlignedVintageNode util = new CompressedAlignedVintageNode();

    public CompressedAlignedVintageNode a = new CompressedAlignedVintageNode(t0.hashCode(),t0);
    public CompressedAlignedVintageNode b = new CompressedAlignedVintageNode(t1.hashCode(),0,t1,a);
    public CompressedAlignedVintageNode c = new CompressedAlignedVintageNode(t2.hashCode(),0,t2,b);
    public CompressedAlignedVintageNode d = new CompressedAlignedVintageNode(t3.hashCode(),0,t3,c);

    public double[] t4 = range(1);
    public double[] t5 = range(2);

    public CompressedAlignedVintageNode e = new CompressedAlignedVintageNode(t4.hashCode(),t4);
    public CompressedAlignedVintageNode f = new CompressedAlignedVintageNode(t5.hashCode(),0,t5,e);

    public double[] t6 = {0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,3,3,3,3,3};
    public CompressedAlignedVintageNode g = new CompressedAlignedVintageNode(t6.hashCode(),t6);

    public double[] t0_1 = {};
    public double[] t1_1 = {0,1};
    public double[] t2_1 = {-1,0,1};
    public double[] t3_1 = {1,2,3,3,3};
    public double[] t4_1 = {4,5,6,6,6,6,6,6,6};
    public double[] t5_1 = {4,5,6,6,6,6,6,6,6};
    public double[] t6_1 = {0,1,2,3};
    public double[] t7_1 = {-1,0,1,2,3,4,5,6,6,6,6,6,6,6,7};

    public CompressedAlignedVintageNode a_1 = new CompressedAlignedVintageNode(t0_1.hashCode(),t0_1);
    public CompressedAlignedVintageNode b_1 = new CompressedAlignedVintageNode(t1_1.hashCode(),0,t1_1,a_1);
    public CompressedAlignedVintageNode c_1 = new CompressedAlignedVintageNode(t2_1.hashCode(),-1,t2_1,b_1);
    public CompressedAlignedVintageNode d_1 = new CompressedAlignedVintageNode(t3_1.hashCode(),1,t3_1,c_1);
    public CompressedAlignedVintageNode e_1 = new CompressedAlignedVintageNode(t4_1.hashCode(),4,t4_1,d_1);
    public CompressedAlignedVintageNode f_1 = new CompressedAlignedVintageNode(t5_1.hashCode(),4,t5_1,e_1);
    public CompressedAlignedVintageNode g_1 = new CompressedAlignedVintageNode(t6_1.hashCode(),0,t6_1,f_1);
    public CompressedAlignedVintageNode h_1 = new CompressedAlignedVintageNode(t7_1.hashCode(),0,t7_1,g_1);


    @Test
    public void checkClass() throws Exception {
        DebugTools.printActiveClassMethodName();

        System.out.println(c.getClass().getName());
        assertThat(c.getClass().getName(),equalTo("com.oldfather.TSDiff$CompressedAlignedVintageNode"));
    }

    @Test
    public void hasChanges() throws Exception {
        DebugTools.printActiveClassMethodName();

        assertThat("an empty delta has no changes",a.hasChanges(),equalTo(false));

        assertThat("an non-empty delta has changes",b.hasChanges(),equalTo(true));

        assertThat("a repeated series should not have changes",f_1.hasChanges(),equalTo(false));


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

        assertThat("the root should be the first non-empty node",f_1.getRootNode(),equalTo(b_1));

    }

    @Test
    public void getParent() throws Exception {
        DebugTools.printActiveClassMethodName();
        assertThat("the root should be the first non-empty node",f_1.cleanup().getParent(),equalTo(d_1));
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

        System.out.println(Arrays.toString(g_1.decodeDelta()));
        assertThat(g_1.hasChanges(),equalTo(true));
        assertThat(g_1.decodeDelta(),equalTo(t6_1));

        System.out.println(Arrays.toString(h_1.delta));
        System.out.println(Arrays.toString(h_1.decodeDelta()));
        assertThat(h_1.hasChanges(),equalTo(true));
        assertThat(h_1.decodeDelta(),equalTo(t7_1));


    }

}