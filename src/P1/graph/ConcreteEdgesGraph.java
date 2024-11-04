/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import java.util.*;

/**
 * An implementation of Graph.
 * You can add/remove vertices and edges, or check edges information in this graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    // vertices in the Graph
    private final Set<L> vertices = new HashSet<>();
    // edges in thr Graph
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //   AF(vertices) = Vertices in actual graph
    //   AF(edges) = Edges in actual graph
    //
    // Representation invariant:
    //   每个顶点的关键字（名称）唯一确定
    //   边允许自达 但从某一顶点指向一个顶点最多允许存在一条有向边
    //   边的权值为非0正值 所有权值为0的边视为不存在
    //
    // Safety from rep exposure:
    //   对返回的内部变量采用防御式拷贝，预防类内部泄露
    //   ConcreteEdgesGraph类内全部属性采用private final修饰
    
    // constructor
    public ConcreteEdgesGraph(){
    }
    
    // checkRep
    public void checkRep(){
        for(Edge<L> edge: edges){
            assert edge.getWeight()>0;
            assert vertices.contains(edge.getSource());
            assert vertices.contains(edge.getTarget());
        }
    }
    
    @Override public boolean add(L vertex) {
        if(vertices.contains(vertex)) return false;
        else{
            vertices.add(vertex);
            checkRep();
            return true;
        }
        // throw new RuntimeException("not implemented");
    }
    
    @Override public int set(L source, L target, int weight) {
        // 方法外部前置确保输入的weight不为负数
        int pst_weight = 0;
        Edge<L> temp_edge = null;
        // 由于Edge是Immutable的 我们必须从edges中找到原有的边删去再添加新的Edge对象
        // weight不为0 则先确保图内有source与target点
        // 直接执行add方法 若无指定点则直接添加
        if(weight!=0)
        {
            vertices.add(source);
            vertices.add(target);
        }
        // 遍历 找到图中从source到target的边 记录到temp_edge
        List<Edge<L>> edgeList = new ArrayList<>(edges);
        for(Edge<L> edge: edgeList){
            if(edge.getSource().equals(source)&&edge.getTarget().equals(target)){
                pst_weight = edge.getWeight();
                temp_edge = edge;
                break;
            }
        }
        // temp_edge仍为null说明不存在从source到target的边
        // 不为null则原本存在指定的边 移除
        if(temp_edge!=null)  edges.remove(temp_edge);
        // 若weight不为0 此时图中可以直接添加新建的边
        // 若weight为0 则此时已经完成删去原有边的过程
        if(weight!=0){
            Edge<L> new_edge = new Edge<>(source, target, weight);
            edges.add(new_edge);
        }
        checkRep();
        return pst_weight;
        // throw new RuntimeException("not implemented");
    }

    @Override public boolean remove(L vertex) {
        if(vertices.contains(vertex)){
            edges.removeIf(edge -> edge.getSource().equals(vertex) || edge.getTarget().equals(vertex));
            vertices.remove(vertex);
            checkRep();
            return true;
        }
        else return false;
        // throw new RuntimeException("not implemented");
    }
    
    @Override public Set<L> vertices() {
        return new HashSet<>(vertices);
        // throw new RuntimeException("not implemented");
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> sources = new HashMap<>();
        for(Edge<L> edge: edges){
            if(edge.getTarget().equals(target))
                sources.put(edge.getSource(),edge.getWeight());
        }
        checkRep();
        return sources;
        // throw new RuntimeException("not implemented");
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> targets = new HashMap<>();
        for(Edge<L> edge: edges){
            if(edge.getSource().equals(source))
                targets.put(edge.getTarget(),edge.getWeight());
        }
        checkRep();
        return targets;
        // throw new RuntimeException("not implemented");
    }
    
    // toString()
    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("顶点：").append(vertices).append("\n");
        for(Edge<L> edge: edges){
            stringBuilder.append(edge.toString());
        }
        return stringBuilder.toString();
    }
}

/**
 * An implementation of edges in graph. Support basic Edge attributes and methods.
 * Immutable.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge<L> {
    
    // fields
    // source of current Edge Object
    private final L source;
    // target of current Edge Object
    private final L target;
    // weight of current Edge Object
    private final int weight;
    
    // Abstraction function:
    //   AF(source) = 当前边出发的点
    //   AF(target) = 当前边指向的点
    //   AF(weight) = 当前有向边的权值
    // Representation invariant:
    //   weight > 0
    //   顶点 source & target 必定在实际图中
    //   source 可以等于 target
    // Safety from rep exposure:
    //   'private final' 修饰
    //   返回的数据结构均为immutable 不能通过外部修改影响内部
    
    // constructor
    public Edge(L source, L target, int weight){
        this.source=source;
        this.target=target;
        this.weight=weight;
    }
    // checkRep
    public void checkRep(){
        assert this.source != null;
        assert this.target != null;
        assert this.weight > 0;
    }
    
    // methods: Observer

    /**
     * return the source of current Edge
     * @return source
     */
    public L getSource(){
        checkRep();
        return source;
    }
    /**
     * return the target of current Edge
     * @return target
     */
    public L getTarget(){
        checkRep();
        return target;
    }
    /**
     * return the weight of current Edge
     * @return weight<int>
     */
    public int getWeight(){
        checkRep();
        return weight;
    }
    // toString(): Observer
    @Override
    public String toString(){
        checkRep();
        return "<"+source.toString()+", "+ target.toString()+">  weight: "+weight+"\n";
    }
}
