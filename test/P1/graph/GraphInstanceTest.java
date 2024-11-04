/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    /* Testing strategy

       针对Graph接口的全部非静态方法进行测试:
        -testAdd()：测试Graph接口内add方法
         考虑两种等价类：添加的点未加入图/已加入图
         在同一方法中完成测试

        -testSet()：测试Graph接口内set方法
         **方法应当在外部确保weight输入不为负值**
         考虑5个维度：顶点source在图中/不在图中 顶点target在图中/不在图中
                   source与target相同/不同
                   输入值weight为非0正值/0
                   当前图中从source顶点指向target顶点有边/无边
         要使得source与target相同 必须有source target都在图中/都不在图中
         而source target都不在图中时 必定没有在图中的从source顶点指向target顶点的边
         故source与target相同情况下有2*2+2=6个等价类
         考虑source与target不相同的情况，也即一般情况
         --source target不满足都在图中时 必定没有在图中的从source顶点指向target顶点的边
           此时有3*2*1=6个等价类
         --source target都在图中时 考虑当前图中从source顶点指向target顶点有边/无边
           此时有2*2*1*1=4个等价类
         整理得到总计有6+6+4=16个等价类
         设计testSet()方法1~16

        -testRemove()：测试Graph接口内remove方法
         考虑两个维度：要移除的顶点在图中/不在图中 要移除的顶点在图中有边/无边
         除去矛盾情况，整理得到总计有3个等价类
         设计testRemove()方法1~3

        -testVertices()：测试Graph接口内的vertices方法
         考虑两种等价类：图中有顶点/无顶点
         对有顶点的情况进一步展开测试：测试图中有小规模顶点/较大规模顶点的情况(以20个为界限)
         总计3个等价类
         在同一方法中完成测试

        -testSources()：测试Graph接口内的sources方法
         由于无向图可以看做特殊有向图 故对有向图的一般情况测试即可
         考虑顶点target有/无入边和顶点target在图中/不在图中
         整理得到3个等价类，在同一方法中完成测试

        -testTargets()：测试Graph接口内的targets方法
         由于无向图可以看做特殊有向图 故对有向图的一般情况测试即可
         考虑顶点source有/无出边和顶点source在图中/不在图中
         整理得到3个等价类，在同一方法中完成测试
    */
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }

    /*
        -testAdd()：测试Graph接口内add方法
         考虑两种等价类：添加的点未加入图/已加入图
         在同一方法中完成测试
     */
    @Test
    public void testAdd(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        // 添加的点未加入图——a b c三个顶点
        assertFalse(graph.vertices().contains(a));
        assertFalse(graph.vertices().contains(b));
        assertFalse(graph.vertices().contains(c));

        graph.add(a);
        assertTrue(graph.vertices().contains(a));
        assertFalse(graph.vertices().contains(b));
        assertFalse(graph.vertices().contains(c));

        graph.add(b);
        assertTrue(graph.vertices().contains(a));
        assertTrue(graph.vertices().contains(b));
        assertFalse(graph.vertices().contains(c));

        // 添加的点已加入图——重复添加顶点a测试
        assertFalse(graph.add(a));
        assertTrue(graph.vertices().contains(a));
        assertTrue(graph.vertices().contains(b));
        assertFalse(graph.vertices().contains(c));

        graph.add(c);
        assertTrue(graph.vertices().contains(a));
        assertTrue(graph.vertices().contains(b));
        assertTrue(graph.vertices().contains(c));
    }


    /*
        -testSet()：测试Graph接口内set方法
         **方法应当在外部确保weight输入不为负值**
         考虑5个维度：顶点source在图中/不在图中 顶点target在图中/不在图中
                   source与target相同/不同
                   输入值weight为非0正值/0
                   当前图中从source顶点指向target顶点有边/无边
         要使得source与target相同 必须有source target都在图中/都不在图中
         而source target都不在图中时 必定没有在图中的从source顶点指向target顶点的边
         故source与target相同情况下有2*2+2=6个等价类
         考虑source与target不相同的情况，也即一般情况
         --source target不满足都在图中时 必定没有在图中的从source顶点指向target顶点的边
           此时有3*2*1=6个等价类
         --source target都在图中时 考虑当前图中从source顶点指向target顶点有边/无边
           此时有2*2*1*1=4个等价类

         整理得到总计有6+6+4=16个等价类
         设计testSet()方法1~16
     */
    // source在图中 target在图中 输入weight为非0正值 当前图中从source顶点指向target顶点有边
    // source与target不相同
    @Test
    public void testSet1(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(a);
        graph.add(b);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        graph.set(a,b,4);
        assertEquals(4,graph.set(a,b,10));

        actualVertices.add(a);
        actualVertices.add(b);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        actualSourcesB.put(a,10);
        actualTargetsA.put(b,10);
        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source在图中 target在图中 输入weight为0 当前图中从source顶点指向target顶点有边
    // source与target不相同
    @Test
    public void testSet2(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(a);
        graph.add(b);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        graph.set(a,b,5);
        assertEquals(5,graph.set(a,b,0));

        actualVertices.add(a);
        actualVertices.add(b);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source在图中 target在图中 输入weight为非0正值 当前图中从source顶点指向target顶点无边
    // source与target不相同
    @Test
    public void testSet3(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(a);
        graph.add(b);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        assertEquals(0,graph.set(a,b,5));

        actualVertices.add(a);
        actualVertices.add(b);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        actualSourcesB.put(a,5);
        actualTargetsA.put(b,5);
        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source在图中 target在图中 输入weight为0 当前图中从source顶点指向target顶点无边
    // source与target不相同
    @Test
    public void testSet4(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(a);
        graph.add(b);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        assertEquals(0,graph.set(a,b,0));

        actualVertices.add(a);
        actualVertices.add(b);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source不在图中 target在图中 输入weight为非0正值 图中从source顶点指向target顶点必然无边
    // source与target不相同
    @Test
    public void testSet5(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(b);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        assertEquals(0,graph.set(a,b,5));

        actualVertices.add(a);
        actualVertices.add(b);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        actualSourcesB.put(a,5);
        actualTargetsA.put(b,5);
        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source不在图中 target在图中 输入weight为0 图中从source顶点指向target顶点必然无边
    // source与target不相同
    @Test
    public void testSet6(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(b);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        assertEquals(0,graph.set(a,b,0));

        actualVertices.add(b);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source在图中 target不在图中 输入weight为非0正值 图中从source顶点指向target顶点必然无边
    // source与target不相同
    @Test
    public void testSet7(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(a);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        assertEquals(0,graph.set(a,b,5));

        actualVertices.add(a);
        actualVertices.add(b);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        actualSourcesB.put(a,5);
        actualTargetsA.put(b,5);
        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source在图中 target不在图中 输入weight为0 图中从source顶点指向target顶点必然无边
    // source与target不相同
    @Test
    public void testSet8(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(a);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        assertEquals(0,graph.set(a,b,0));

        actualVertices.add(a);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source不在图中 target不在图中 输入weight为非0正值 图中从source顶点指向target顶点必然无边
    // source与target不相同
    @Test
    public void testSet9(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        assertEquals(0,graph.set(a,b,5));

        actualVertices.add(a);
        actualVertices.add(b);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        actualTargetsA.put(b,5);
        actualSourcesB.put(a,5);
        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source不在图中 target不在图中 输入weight为0 图中从source顶点指向target顶点必然无边
    // source与target不相同
    @Test
    public void testSet10(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        assertEquals(0,graph.set(a,b,0));

        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source在图中 target在图中 输入weight为非0正值 图中从source顶点指向target顶点有边
    // source与target相同
    @Test
    public void testSet11(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(a);
        graph.add(b);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        graph.set(a,a,6);
        assertEquals(6,graph.set(a,a,10));

        actualVertices.add(a);
        actualVertices.add(b);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        actualSourcesA.put(a,10);
        actualTargetsA.put(a,10);
        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source在图中 target在图中 输入weight为0 图中从source顶点指向target顶点有边
    // source与target相同
    @Test
    public void testSet12(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(a);
        graph.add(b);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        graph.set(a,a,6);
        assertEquals(6,graph.set(a,a,0));

        actualVertices.add(a);
        actualVertices.add(b);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source在图中 target在图中 输入weight为非0正值 图中从source顶点指向target顶点无边
    // source与target相同
    @Test
    public void testSet13(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(a);
        graph.add(b);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        assertEquals(0,graph.set(a,a,10));

        actualVertices.add(a);
        actualVertices.add(b);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        actualSourcesA.put(a,10);
        actualTargetsA.put(a,10);
        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source在图中 target在图中 输入weight为0 图中从source顶点指向target顶点无边
    // source与target相同
    @Test
    public void testSet14(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(a);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        assertEquals(0,graph.set(a,a,0));

        actualVertices.add(a);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source不在图中 target不在图中 输入weight为非0正值 在图中从source顶点指向target顶点必定无边
    // source与target相同
    @Test
    public void testSet15(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(b);
        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        assertEquals(0,graph.set(a,a,10));

        actualVertices.add(a);
        actualVertices.add(b);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        actualSourcesA.put(a,10);
        actualTargetsA.put(a,10);
        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // source不在图中 target不在图中 输入weight为0 在图中从source顶点指向target顶点必定无边
    // source与target相同
    @Test
    public void testSet16(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        graph.add(c);

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        assertEquals(0,graph.set(a,a,0));

        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    /*
        -testRemove()：测试Graph接口内remove方法
         考虑两个维度：要移除的顶点在图中/不在图中 要移除的顶点在图中有边/无边
         除去矛盾情况，整理得到总计有3个等价类
         设计testRemove()方法1~3
     */
    // 要移除的顶点在图中 要移除的顶点在图中有边
    @Test
    public void testRemove1(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.set(c,a,5);
        graph.set(a,c,4);
        graph.set(a,b,11);
        assertTrue(graph.remove(c));

        actualVertices.add(a);
        actualVertices.add(b);
        assertEquals(actualVertices,graph.vertices());

        actualTargetsA.put(b,11);
        actualSourcesB.put(a,11);
        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // 要移除的顶点在图中 要移除的顶点在图中无边
    @Test
    public void testRemove2(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.set(c,a,5);
        graph.set(a,c,4);
        graph.set(a,a,11);
        assertTrue(graph.remove(b));

        actualVertices.add(a);
        actualVertices.add(c);
        assertEquals(actualVertices,graph.vertices());

        actualTargetsA.put(c,4);
        actualTargetsA.put(a,11);
        actualTargetsC.put(a,5);
        actualSourcesA.put(a,11);
        actualSourcesA.put(c,5);
        actualSourcesC.put(a,4);
        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    // 要移除的顶点不在图中 要移除的顶点在图中必然无边
    @Test
    public void testRemove3(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";

        Set<String> actualVertices = new HashSet<>();
        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();

        graph.set(a,b,11);
        assertFalse(graph.remove(c));

        actualVertices.add(a);
        actualVertices.add(b);
        assertEquals(actualVertices,graph.vertices());

        actualTargetsA.put(b,11);
        actualSourcesB.put(a,11);
        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualTargetsC,graph.targets(c));
    }
    /*
        -testVertices()：测试Graph接口内的vertices方法
         考虑两种等价类：图中有顶点/无顶点
         对有顶点的情况进一步展开测试：测试图中有小规模顶点/较大规模顶点的情况(以15个为界限)
         总计3个等价类
         在同一方法中完成测试
     */
    @Test
    public void testVertices(){
        Graph<String> graph = emptyInstance();
        Set<String> actualVertices = new HashSet<>();
        String p0 = "Alpha";
        String p1 = "Beta";
        String p2 = "Charlies";
        String p3 = "Delta";
        String p4 = "Echo";
        String p5 = "Foxtrot";
        String p6 = "Golf";
        String p7 = "Hotel";
        String p8 = "India";
        String p9 = "Juliet";
        String p10 = "Kilo";
        String p11 = "Lima";
        String p12 = "Mike";
        String p13 = "November";
        String p14 = "Oscar";
        String p15 = "Papa";
        String p16 = "Quebec";
        String p17 = "Romeo";
        String p18 = "Sierra";
        String p19 = "Tango";
        String p20 = "Uniform";

        // 图中无顶点
        assertEquals(graph.vertices(),actualVertices);

        // 图中有顶点 小规模
        graph.add(p0);
        actualVertices.add(p0);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p1);
        actualVertices.add(p1);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p2);
        actualVertices.add(p2);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p4);
        actualVertices.add(p4);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p5);
        actualVertices.add(p5);
        assertEquals(graph.vertices(),actualVertices);

        assertTrue(graph.vertices().contains(p0));
        assertTrue(graph.vertices().contains(p1));
        assertTrue(graph.vertices().contains(p2));
        assertTrue(graph.vertices().contains(p4));
        assertTrue(graph.vertices().contains(p5));
        assertFalse(graph.vertices().contains(p3));

        //图中有顶点 较大规模 扩充当前图的点规模
        graph.add(p6);
        actualVertices.add(p6);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p7);
        actualVertices.add(p7);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p8);
        actualVertices.add(p8);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p9);
        actualVertices.add(p9);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p10);
        actualVertices.add(p10);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p11);
        actualVertices.add(p11);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p13);
        actualVertices.add(p13);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p14);
        actualVertices.add(p14);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p17);
        actualVertices.add(p17);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p18);
        actualVertices.add(p18);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p19);
        actualVertices.add(p19);
        assertEquals(graph.vertices(),actualVertices);

        graph.add(p20);
        actualVertices.add(p20);
        assertEquals(graph.vertices(),actualVertices);

        assertTrue(graph.vertices().contains(p0));
        assertTrue(graph.vertices().contains(p1));
        assertTrue(graph.vertices().contains(p2));
        assertFalse(graph.vertices().contains(p3));
        assertTrue(graph.vertices().contains(p4));
        assertTrue(graph.vertices().contains(p5));
        assertTrue(graph.vertices().contains(p6));
        assertTrue(graph.vertices().contains(p7));
        assertTrue(graph.vertices().contains(p8));
        assertTrue(graph.vertices().contains(p9));
        assertTrue(graph.vertices().contains(p10));
        assertTrue(graph.vertices().contains(p11));
        assertFalse(graph.vertices().contains(p12));
        assertTrue(graph.vertices().contains(p13));
        assertTrue(graph.vertices().contains(p14));
        assertFalse(graph.vertices().contains(p15));
        assertFalse(graph.vertices().contains(p16));
        assertTrue(graph.vertices().contains(p17));
        assertTrue(graph.vertices().contains(p18));
        assertTrue(graph.vertices().contains(p19));
        assertTrue(graph.vertices().contains(p20));
    }
    /*
        -testSources()：测试Graph接口内的sources方法
         由于无向图可以看做特殊有向图 故对有向图的一般情况测试即可
         考虑顶点target有/无入边和顶点target在图中/不在图中
         整理得到3个等价类，在同一方法中完成测试
     */
    @Test
    public void testSources(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";
        String d = "Delta";
        String e = "Echo";

        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.add(d);

        Map<String, Integer> actualSourcesA = new HashMap<>();
        Map<String, Integer> actualSourcesB = new HashMap<>();
        Map<String, Integer> actualSourcesC = new HashMap<>();
        Map<String, Integer> actualSourcesD = new HashMap<>();
        Map<String, Integer> actualSourcesE = new HashMap<>();

        graph.set(a,b,4);
        graph.set(b,a,4);
        graph.set(a,c,7);
        graph.set(d,b,11);

        actualSourcesC.put(a,7);
        actualSourcesB.put(d,11);
        actualSourcesA.put(b,4);
        actualSourcesB.put(a,4);
        assertEquals(actualSourcesA,graph.sources(a));
        assertEquals(actualSourcesB,graph.sources(b));
        assertEquals(actualSourcesC,graph.sources(c));
        assertEquals(actualSourcesD,graph.sources(d));
        assertEquals(actualSourcesE,graph.sources(e));
    }
    /*
        -testTargets()：测试Graph接口内的targets方法
         由于无向图可以看做特殊有向图 故对有向图的一般情况测试即可
         考虑顶点source有/无出边和顶点source在图中/不在图中
         整理得到3个等价类，在同一方法中完成测试
     */
    // 图为有向图 顶点target无入边无出边
    @Test
    public void testTargets(){
        Graph<String> graph = emptyInstance();
        String a = "Alpha";
        String b = "Beta";
        String c = "Charlies";
        String d = "Delta";
        String e = "Echo";

        graph.add(a);
        graph.add(b);
        graph.add(c);
        graph.add(d);

        Map<String, Integer> actualTargetsA = new HashMap<>();
        Map<String, Integer> actualTargetsB = new HashMap<>();
        Map<String, Integer> actualTargetsC = new HashMap<>();
        Map<String, Integer> actualTargetsD = new HashMap<>();
        Map<String, Integer> actualTargetsE = new HashMap<>();

        graph.set(a,b,4);
        graph.set(b,a,4);
        graph.set(a,c,7);
        graph.set(d,b,11);

        actualTargetsA.put(b,4);
        actualTargetsB.put(a,4);
        actualTargetsA.put(c,7);
        actualTargetsD.put(b,11);

        assertEquals(actualTargetsA,graph.targets(a));
        assertEquals(actualTargetsB,graph.targets(b));
        assertEquals(actualTargetsC,graph.targets(c));
        assertEquals(actualTargetsD,graph.targets(d));
        assertEquals(actualTargetsE,graph.targets(e));
    }
}