/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<>();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //   考虑顶点被边连接和顶点不被边连接的情况 在同一个方法中实现两个等价类
    
    // tests for ConcreteEdgesGraph.toString()
    @Test
    public void testToString(){
        Graph<String> graph = emptyInstance();
        String a = "Maimai";
        String b = "Arcaea";
        String c = "Ongeki";
        String d = "Rizline";

        // 在图中而没有边的点
        graph.add(d);

        graph.set(a,b,2);
        graph.set(b,a,2);
        graph.set(b,c,2);
        graph.set(c,b,2);
        graph.set(a,c,10);
        graph.set(c,a,10);

        String str = "顶点：" + "[Rizline, Arcaea, Ongeki, Maimai]" + "\n" +
                "<Maimai, Arcaea>  weight: 2\n" +
                "<Arcaea, Maimai>  weight: 2\n" +
                "<Arcaea, Ongeki>  weight: 2\n" +
                "<Ongeki, Arcaea>  weight: 2\n" +
                "<Maimai, Ongeki>  weight: 10\n" +
                "<Ongeki, Maimai>  weight: 10\n";
        assertEquals(graph.toString(),str);
    }
    
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    //  直接测试Edge类中的三个方法：获取起点 终点与此边权重
    
    // tests for operations of Edge
    // 测试"获取起点"方法返回值正确
    @Test
    public void testGetSource(){
        Edge<String> edge = new Edge<>("start", "end", 6);
        assertEquals("start",edge.getSource());
    }
    // 测试"获取终点"方法返回值正确
    @Test
    public void testGetTarget(){
        Edge<String> edge = new Edge<>("start","end",6);
        assertEquals("end",edge.getTarget());
    }
    // 测试"获取权重"方法返回值正确
    @Test
    public void testGetWeight(){
        Edge<String> edge = new Edge<>("start","end",6);
        assertEquals(6,edge.getWeight());
    }
    
}
