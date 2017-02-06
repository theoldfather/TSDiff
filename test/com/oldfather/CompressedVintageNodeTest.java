package com.oldfather;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;


/**
 * Created by theoldfather on 2/5/17.
 */
public class CompressedVintageNodeTest {
    @Test
    public void getRootNode() throws Exception {
        double[] t0 = {};
        double[] t1 = {0,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        double[] t2 = {0,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        double[] t3 = {0,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        TSDiff.CompressedVintageNode a = new TSDiff.CompressedVintageNode(0,t0);
        TSDiff.CompressedVintageNode b = new TSDiff.CompressedVintageNode(1,t1,a);

        // assert first node is the root when there is only 1 vintage
        assertThat("",a.getRootNode(),equalTo(a));

        // assert second node is root if first node empty
        assertThat("empty nodes at root of chain should get dropped.",b.getRootNode(),equalTo(b));




    }

}