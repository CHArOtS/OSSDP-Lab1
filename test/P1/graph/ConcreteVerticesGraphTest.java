/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    //   考虑顶点被边连接和顶点不被边连接的情况 在同一个方法中实现两个等价类
    
    // tests for ConcreteVerticesGraph.toString()
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

        String str;
        str = "\n------------------------------------------";
        str += "\n顶点：" + "Rizline";
        str += "\n父结点：" + "{}";
        str += "\n子结点：" + "{}";
        str += "\n------------------------------------------";
        str += "\n顶点：" + "Maimai";
        str += "\n父结点：" + "{Arcaea=2, Ongeki=10}";
        str += "\n子结点：" + "{Arcaea=2, Ongeki=10}";
        str += "\n------------------------------------------";
        str += "\n顶点：" + "Arcaea";
        str += "\n父结点：" + "{Ongeki=2, Maimai=2}";
        str += "\n子结点：" + "{Ongeki=2, Maimai=2}";
        str += "\n------------------------------------------";
        str += "\n顶点：" + "Ongeki";
        str += "\n父结点：" + "{Arcaea=2, Maimai=10}";
        str += "\n子结点：" + "{Arcaea=2, Maimai=10}";
        str += "\n------------------------------------------";
        assertEquals(str,graph.toString());
    }
    
    /*
     * Testing Vertex...
     */
    
    // Testing strategy for Vertex
    /*
        对Vertex类中其他方法进行测试
        -getKey() 测试中直接验证对比key即可
        -setSource()
         考虑两个维度：有/没有source指向当前顶点的边 weight为0/非0正值
         ConcreteVerticesGraph已确保source在图中 checkRep确保weight不小于0
         整理得到4个等价类 设计testSetSource() 1~4
        -setTarget()
         考虑两个维度：有/没有当前顶点指向target的边 weight为0/非0正值
         ConcreteVerticesGraph已确保source在图中 checkRep确保weight不小于0
         整理得到4个等价类 设计testSetTarget() 1~4
        -getSources()
         考虑一个维度：当前结点有/无父结点 共2个等价类
         在同一方法中测试
        -getTargets()
         考虑一个维度：当前结点有/无子结点 共2个等价类
         在同一方法中测试
     */
    
    // tests for operations of Vertex

    // -getKey() 测试中直接验证对比key即可
    @Test
    public void testGetKey(){
        String aa = "Maimai";
        String bb = "Arcaea";
        String cc = "Ongeki";
        // String dd = "Rizline";

        Vertex<String> a = new Vertex<>("Maimai");
        Vertex<String> b = new Vertex<>("Arcaea");
        Vertex<String> c = new Vertex<>("Ongeki");
        Vertex<String> d = new Vertex<>("Rizline");

        a.setSource(bb,2);
        a.setTarget(bb,2);
        b.setSource(aa,2);
        b.setTarget(aa,2);
        b.setSource(cc,2);
        b.setTarget(cc,2);
        c.setSource(bb,2);
        c.setTarget(bb,2);
        a.setSource(cc,10);
        a.setTarget(cc,10);
        c.setSource(aa,10);
        c.setTarget(aa,10);

        assertEquals("Maimai",a.getKey());
        assertEquals("Arcaea",b.getKey());
        assertEquals("Ongeki",c.getKey());
        assertEquals("Rizline",d.getKey());
    }
    /*
        -setSource()
         考虑两个维度：有/没有source指向当前顶点的边 weight为0/非0正值
         ConcreteVerticesGraph已确保source在图中 checkRep确保weight不小于0
         整理得到4个等价类 设计testSetSource() 1~4
     */
    // 有从source指向当前顶点的边 weight为非0正值
    @Test
    public void testSetSource1(){
        String aa = "Maimai";
        String bb = "Arcaea";
        String cc = "Ongeki";

        Vertex<String> a = new Vertex<>("Maimai");
        Vertex<String> b = new Vertex<>("Arcaea");
        Vertex<String> c = new Vertex<>("Ongeki");

        Map<String, Integer> sourceA = new HashMap<>();
        Map<String, Integer> sourceB = new HashMap<>();
        Map<String, Integer> sourceC = new HashMap<>();

        a.setSource(bb,1);
        sourceA.put(bb,1);
        b.setTarget(aa,1);
        b.setSource(aa,1);
        sourceB.put(aa,1);
        a.setTarget(bb,1);
        c.setSource(aa,9);
        sourceC.put(aa,9);
        a.setTarget(cc,9);

        a.setSource(bb,2);
        sourceA.replace(bb,2);
        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());
        b.setTarget(aa,2);

        b.setSource(aa,2);
        sourceB.replace(aa,2);
        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());
        a.setTarget(bb,2);

        c.setSource(aa,10);
        sourceC.replace(aa,10);
        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());
        a.setTarget(cc,10);

    }
    // 有从source指向当前顶点的边 weight为0
    @Test
    public void testSetSource2() {
        String aa = "Maimai";
        String bb = "Arcaea";
        String cc = "Ongeki";

        Vertex<String> a = new Vertex<>("Maimai");
        Vertex<String> b = new Vertex<>("Arcaea");
        Vertex<String> c = new Vertex<>("Ongeki");

        Map<String, Integer> sourceA = new HashMap<>();
        Map<String, Integer> sourceB = new HashMap<>();
        Map<String, Integer> sourceC = new HashMap<>();

        a.setSource(bb,2);
        sourceA.put(bb,2);
        b.setTarget(aa,2);
        b.setSource(aa,2);
        sourceB.put(aa,2);
        a.setTarget(bb,2);
        c.setSource(aa,10);
        sourceC.put(aa,10);
        a.setTarget(cc,10);

        a.setSource(bb,0);
        sourceA.remove(bb);
        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());
        b.setTarget(aa,0);

        b.setSource(aa,0);
        sourceB.remove(aa);
        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());
        a.setTarget(bb,0);

    }
    // 无从source指向当前顶点的边 weight为非0正值
    @Test
    public void testSetSource3() {
        String aa = "Maimai";
        String bb = "Arcaea";
        String cc = "Ongeki";

        Vertex<String> a = new Vertex<>("Maimai");
        Vertex<String> b = new Vertex<>("Arcaea");
        Vertex<String> c = new Vertex<>("Ongeki");

        Map<String, Integer> sourceA = new HashMap<>();
        Map<String, Integer> sourceB = new HashMap<>();
        Map<String, Integer> sourceC = new HashMap<>();

        c.setSource(aa,10);
        sourceC.put(aa,10);
        a.setTarget(cc,10);

        a.setSource(bb,2);
        sourceA.put(bb,2);
        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());
        b.setTarget(aa,2);

        b.setSource(aa,2);
        sourceB.put(aa,2);
        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());
        a.setTarget(bb,2);
    }
    // 无从source指向当前顶点的边 weight为0
    @Test
    public void testSetSource4() {
        String aa = "Maimai";
        String bb = "Arcaea";
        String cc = "Ongeki";

        Vertex<String> a = new Vertex<>("Maimai");
        Vertex<String> b = new Vertex<>("Arcaea");
        Vertex<String> c = new Vertex<>("Ongeki");

        Map<String, Integer> sourceA = new HashMap<>();
        Map<String, Integer> sourceB = new HashMap<>();
        Map<String, Integer> sourceC = new HashMap<>();

        c.setSource(aa,10);
        sourceC.put(aa,10);
        a.setTarget(cc,10);

        a.setSource(bb,0);
        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());
        b.setTarget(aa,0);

        b.setSource(aa,0);
        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());
        a.setTarget(bb,0);
    }
    /*
        -setTarget()
         考虑两个维度：有/没有当前顶点指向target的边 weight为0/非0正值
         ConcreteVerticesGraph已确保source在图中 checkRep确保weight不小于0
         整理得到4个等价类 设计testSetTarget() 1~4
     */
    // 有从当前顶点指向target的边 weight为非0正值
    @Test
    public void testSetTarget1(){
        String aa = "Maimai";
        String bb = "Arcaea";
        String cc = "Ongeki";

        Vertex<String> a = new Vertex<>("Maimai");
        Vertex<String> b = new Vertex<>("Arcaea");
        Vertex<String> c = new Vertex<>("Ongeki");

        Map<String, Integer> targetA = new HashMap<>();
        Map<String, Integer> targetB = new HashMap<>();
        Map<String, Integer> targetC = new HashMap<>();

        a.setSource(bb,1);
        b.setTarget(aa,1);
        targetB.put(aa,1);
        b.setSource(aa,1);
        a.setTarget(bb,1);
        targetA.put(bb,1);
        c.setSource(aa,9);
        a.setTarget(cc,9);
        targetA.put(cc,9);

        a.setSource(bb,2);
        b.setTarget(aa,2);
        targetB.replace(aa,2);
        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());

        b.setSource(aa,2);
        a.setTarget(bb,2);
        targetA.replace(bb,2);
        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());

        c.setSource(aa,10);
        a.setTarget(cc,10);
        targetA.replace(cc,10);
        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());
    }
    // 有从当前顶点指向target的边 weight为0
    @Test
    public void testSetTarget2() {
        String aa = "Maimai";
        String bb = "Arcaea";
        String cc = "Ongeki";

        Vertex<String> a = new Vertex<>("Maimai");
        Vertex<String> b = new Vertex<>("Arcaea");
        Vertex<String> c = new Vertex<>("Ongeki");

        Map<String, Integer> targetA = new HashMap<>();
        Map<String, Integer> targetB = new HashMap<>();
        Map<String, Integer> targetC = new HashMap<>();

        a.setSource(bb,2);
        b.setTarget(aa,2);
        targetB.put(aa,2);
        b.setSource(aa,2);
        a.setTarget(bb,2);
        targetA.put(bb,2);
        c.setSource(aa,10);
        a.setTarget(cc,10);
        targetA.put(cc,10);

        a.setSource(bb,0);
        b.setTarget(aa,0);
        targetB.remove(aa);
        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());

        b.setSource(aa,0);
        a.setTarget(bb,0);
        targetA.remove(bb);
        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());
    }
    // 无从当前顶点指向target的边 weight为非0正值
    @Test
    public void testSetTarget3() {
        String aa = "Maimai";
        String bb = "Arcaea";
        String cc = "Ongeki";

        Vertex<String> a = new Vertex<>("Maimai");
        Vertex<String> b = new Vertex<>("Arcaea");
        Vertex<String> c = new Vertex<>("Ongeki");

        Map<String, Integer> targetA = new HashMap<>();
        Map<String, Integer> targetB = new HashMap<>();
        Map<String, Integer> targetC = new HashMap<>();

        c.setSource(aa,10);
        a.setTarget(cc,10);
        targetA.put(cc,10);

        a.setSource(bb,2);
        b.setTarget(aa,2);
        targetB.put(aa,2);
        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());

        b.setSource(aa,2);
        a.setTarget(bb,2);
        targetA.put(bb,2);
        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());

    }
    // 无从当前顶点指向target的边 weight为0
    @Test
    public void testSetTarget4() {
        String aa = "Maimai";
        String bb = "Arcaea";
        String cc = "Ongeki";

        Vertex<String> a = new Vertex<>("Maimai");
        Vertex<String> b = new Vertex<>("Arcaea");
        Vertex<String> c = new Vertex<>("Ongeki");

        Map<String, Integer> targetA = new HashMap<>();
        Map<String, Integer> targetB = new HashMap<>();
        Map<String, Integer> targetC = new HashMap<>();

        c.setSource(aa,10);
        a.setTarget(cc,10);
        targetA.put(cc,10);

        a.setSource(bb,0);
        b.setTarget(aa,0);
        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());

        b.setSource(aa,0);
        a.setTarget(bb,0);
        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());
    }
    /*
        -getSources()
         考虑一个维度：当前结点有/无父结点 共2个等价类
         在同一方法中测试
     */
    @Test
    public void testGetSources(){
        String aa = "Maimai";
        String bb = "Arcaea";
        String cc = "Ongeki";

        Vertex<String> a = new Vertex<>("Maimai");
        Vertex<String> b = new Vertex<>("Arcaea");
        Vertex<String> c = new Vertex<>("Ongeki");

        Map<String, Integer> sourceA = new HashMap<>();
        Map<String, Integer> sourceB = new HashMap<>();
        Map<String, Integer> sourceC = new HashMap<>();

        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());

        a.setSource(bb,2);
        sourceA.put(bb,2);
        b.setTarget(aa,2);
        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());

        b.setSource(aa,2);
        sourceB.put(aa,2);
        a.setTarget(bb,2);
        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());

        c.setSource(aa,10);
        sourceC.put(aa,10);
        a.setTarget(cc,10);
        assertEquals(sourceA,a.getSources());
        assertEquals(sourceB,b.getSources());
        assertEquals(sourceC,c.getSources());
    }
    /*
        -getTargets()
         考虑一个维度：当前结点有/无子结点 共2个等价类
         在同一方法中测试
     */
    @Test
    public void testGetTargets(){
        String aa = "Maimai";
        String bb = "Arcaea";
        String cc = "Ongeki";

        Vertex<String> a = new Vertex<>("Maimai");
        Vertex<String> b = new Vertex<>("Arcaea");
        Vertex<String> c = new Vertex<>("Ongeki");

        Map<String, Integer> targetA = new HashMap<>();
        Map<String, Integer> targetB = new HashMap<>();
        Map<String, Integer> targetC = new HashMap<>();

        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());

        a.setSource(bb,2);
        b.setTarget(aa,2);
        targetB.put(aa,2);
        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());

        b.setSource(aa,2);
        a.setTarget(bb,2);
        targetA.put(bb,2);
        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());

        c.setSource(aa,10);
        a.setTarget(cc,10);
        targetA.put(cc,10);
        assertEquals(targetA,a.getTargets());
        assertEquals(targetB,b.getTargets());
        assertEquals(targetC,c.getTargets());
    }
}