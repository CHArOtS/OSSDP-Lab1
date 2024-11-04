/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

/**
 * Tests for static methods of Graph.
 * 
 * To facilitate testing multiple implementations of Graph, instance methods are
 * tested in GraphInstanceTest.
 */
public class GraphStaticTest {
    
    // Testing strategy
    //   empty()
    //     no inputs, only output is empty graph
    //     observe with vertices()
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testEmptyVerticesEmpty() {
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.empty().vertices());
    }
    
    // test other vertex label types in Problem 3.2
    /*
      考虑1个维度：是String/Boolean/Integer/Float/Character....类型
     */
    @Test
    public void testOtherLabelTypes(){
        // 同学那里获取的想法...直接测试空图就行了
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.<String>empty().vertices());
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.<Boolean>empty().vertices());
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.<Character>empty().vertices());
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.<Float>empty().vertices());
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.<Integer>empty().vertices());
        assertEquals("expected empty() graph to have no vertices",
                Collections.emptySet(), Graph.<Collections>empty().vertices());
    }
    
}
