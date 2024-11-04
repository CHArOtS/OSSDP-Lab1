/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.graph;

import java.util.*;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph<L> implements Graph<L> {
    
    private final List<Vertex<L>> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   AF(vertices) = 图中的各个顶点及他们之间相互的父节点子节点关系
    // Representation invariant:
    //   vertices中每个顶点的key唯一确定
    // Safety from rep exposure:
    //   "private final" + 返回Map、Set类型数据时的防御式拷贝
    
    // constructor
    public ConcreteVerticesGraph(){
    }
    // checkRep
    public void checkRep(){
        Set<Vertex<L>> verticesSet = new HashSet<>(vertices);
        assert verticesSet.size() == vertices.size();
    }
    
    @Override public boolean add(L vertex) {
        for(Vertex<L> temp: vertices){
            if(temp.getKey().equals(vertex)) return false;
        }
        Vertex<L> newVertex = new Vertex<>(vertex);
        vertices.add(newVertex);
        checkRep();
        return true;
        // throw new RuntimeException("not implemented");
    }
    
    @Override public int set(L source, L target, int weight) {
        int pst_weight = 0;
        Vertex<L> temp_source = null, temp_target = null;
        if(weight==0){
            for(Vertex<L> vertex: vertices){
                if(vertex.getKey().equals(source)) temp_source = vertex;
                if(vertex.getKey().equals(target)) temp_target = vertex;
            }
            if(temp_target==null||temp_source==null){
                checkRep();
                return pst_weight;
            }
            boolean flag1 = temp_source.getTargets().containsKey(temp_target.getKey());
            boolean flag2 = temp_target.getSources().containsKey(temp_source.getKey());
            if(flag1&&flag2) {
                pst_weight = temp_source.setTarget(target, weight);
                assert pst_weight == temp_target.setSource(source, weight);
            }
        }
        else{
            add(source);
            add(target);
            for(Vertex<L> vertex: vertices){
                if(vertex.getKey().equals(source)) temp_source = vertex;
                if(vertex.getKey().equals(target)) temp_target = vertex;
            }
            if (temp_source != null) {
                pst_weight = temp_source.setTarget(target,weight);
                assert temp_target != null;
                int temp_pst_weight = temp_target.setSource(source, weight);
                assert temp_pst_weight == pst_weight;
                // 进行了一点点修改
                // 按照Vertex的设计 这里temp_source和temp_target是必然同时非空的
                // 同时得到的source与target中存入的边权应当一样
                // 在这里我们做assert判定 确保Vertex结构运作正常
            }
        }
        checkRep();
        return pst_weight;
        // throw new RuntimeException("not implemented");
    }
    
    @Override public boolean remove(L vertex) {
        Vertex<L> temp_vertex = null;
        for(Vertex<L> v: vertices){
            if(v.getKey().equals(vertex)){
                temp_vertex = v;
                break;
            }
        }
        if(temp_vertex!=null){
            vertices.remove(temp_vertex);
            for(Vertex<L> v: vertices){
                if(v.getSources().containsKey(vertex)) v.setSource(vertex,0);
                if(v.getTargets().containsKey(vertex)) v.setTarget(vertex,0);
            }
            checkRep();
            return true;
        }
        else{
            checkRep();
            return false;
        }
        // throw new RuntimeException("not implemented");
    }
    
    @Override public Set<L> vertices() {
        Set<L> verticesSet = new HashSet<>();
        for(Vertex<L> vertex: vertices){
            verticesSet.add(vertex.getKey());
        }
        checkRep();
        return verticesSet;
        // throw new RuntimeException("not implemented");
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Vertex<L> temp_vertex = null;
        for(Vertex<L> v: vertices){
            if(v.getKey().equals(target)){
                temp_vertex = v;
                break;
            }
        }
        checkRep();
        if(temp_vertex==null){
            return new HashMap<>();
        }
        else{
            return new HashMap<>(temp_vertex.getSources());
        }
        // throw new RuntimeException("not implemented");
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Vertex<L> temp_vertex = null;
        for(Vertex<L> v: vertices){
            if(v.getKey().equals(source)){
                temp_vertex = v;
                break;
            }
        }
        checkRep();
        if(temp_vertex==null){
            return new HashMap<>();
        }
        else{
            return new HashMap<>(temp_vertex.getTargets());
        }
        // throw new RuntimeException("not implemented");
    }
    
    // toString()
    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n------------------------------------------");
        for(Vertex<L> vertex: vertices){
            stringBuilder.append(vertex.toString());
        }
        return stringBuilder.toString();
    }
    
}

/**
 * An implementation of Vertex in the ConcreteVerticesGraph.
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex<L> {

    // fields
    // 当前顶点的唯一关键字（也即图要存储的数据）
    private final L key;
    // 当前顶点的全部父亲节点及对应边的权值
    private final Map<L, Integer> sources;
    // 当前顶点的全部子结点及对应边的权值
    private final Map<L, Integer> targets;
    // Abstraction function:
    //   AF(key) = 结点的关键字（名字）
    //   AF(sources) = 此节点全部二元组<当前结点的某父节点，从该父节点指向当前结点有向边的权重>的集合
    //   AF(targets) = 此节点全部二元组<当前结点的某子节点，从当前结点指向该子节点有向边的权重>的集合
    // Representation invariant:
    //   所有边权值均＞0
    //   任意Vertex的key关键字唯一确定(在ConcreteVerticesGraph类中保证）
    //   允许自达边存在 但两点之间同一方向的边最多只许存在一条(数据结构保证)
    //
    // Safety from rep exposure:
    //   对返回的Map类型数据sources与targets采用防御性拷贝
    //   Vertex类内属性均采用private final修饰
    
    // constructor
    public Vertex(L key){
        this.key = key;
        this.sources = new HashMap<>();
        this.targets = new HashMap<>();
    }

    // checkRep
    public void checkRep(){
        Set<L> cur_sources = sources.keySet();
        Set<L> cur_targets = targets.keySet();
        if(!cur_sources.isEmpty()){
            for(L source: cur_sources)
                assert sources.get(source) > 0;
        }
        if(!cur_targets.isEmpty()){
            for(L target: cur_targets)
                assert targets.get(target) > 0;
        }
    }
    // methods

    /**
     * 获取当前顶点的唯一关键字(也即图内定点对应的数据)
     * @return key
     */
    public L getKey() {
        return key;
    }

    /**
     * 获取当前顶点的全部父亲节点信息及对应有向边的边权
     * @return 一个Map类型数据 包括当前顶点的全部父亲节点信息及对应有向边的边权
     * 已进行防御式拷贝
     */
    public Map<L, Integer> getSources() {
        Map<L, Integer> map = new HashMap<>();
        List<L> keyList = new ArrayList<>(sources.keySet());
        for(L key:keyList){
            map.put(key,sources.get(key));
        }
        return map;
    }

    /**
     * 设定当前顶点父亲节点的相关情况
     * @param source 父亲节点的关键字
     * @param weight 整型值 要设置的边权
     *               若为0，在当前顶点有父亲节点source时删去对应的有向边
     *               否则不做任何处理
     *               若不为0，在当前顶点有父亲节点source时修改对应有向边边权为weight
     *               否则在图中添加有向边(source, 当前顶点) 边权为weight
     * @return 当且仅当当前顶点原来有父亲节点source时，返回方法执行前对应有向边的权值
     *         其他情况均返回0
     */
    public int setSource(L source, int weight) {
        // 在ConcreteVerticesGraph的set方法内 weight不为0时
        // 调用此方法前前会调用ConcreteVerticesGraph中的add方法来确保source存在
        // 也就是说 执行此方法时 不会出现source不在图中的情况
        int pst_weight = 0;
        boolean flag = sources.containsKey(source);
        if(flag) pst_weight = sources.get(source);

        if(flag && weight!=0) sources.replace(source,weight);
        else if (flag) sources.remove(source); // && weight==0
        else if (weight!=0) sources.put(source,weight); // && !flag
        checkRep();
        return pst_weight;
    }

    /**
     * 获取当前顶点的全部子节点信息及对应有向边的边权
     * @return 一个Map类型数据 包括当前顶点的全部子节点信息及对应有向边的边权
     * 已进行防御式拷贝
     */
    public Map<L, Integer> getTargets() {
        Map<L, Integer> map = new HashMap<>();
        List<L> keyList = new ArrayList<>(targets.keySet());
        for(L key:keyList){
            map.put(key,targets.get(key));
        }
        return map;
    }

    /**
     * 设定当前顶点父亲节点的相关情况
     * @param target 子节点的关键字
     * @param weight 整型值 要设置的边权
     *               若为0，在当前顶点有子节点target时删去对应的有向边
     *               否则不做任何处理
     *               若不为0，在当前顶点有子节点target时修改对应有向边边权为weight
     *               否则在图中添加有向边(当前顶点, target) 边权为weight
     * @return 当且仅当当前顶点原来有子节点target时，返回方法执行前对应有向边的权值
     *         其他情况均返回0
     */
    public int setTarget(L target, int weight){
        // 在ConcreteVerticesGraph的set方法内 weight不为0时
        // 调用此方法前前会调用ConcreteVerticesGraph中的add方法来确保target存在
        // 也就是说 执行此方法时 不会出现target不在图中的情况
        int pst_weight = 0;
        boolean flag = targets.containsKey(target);
        if(flag) pst_weight = targets.get(target);

        if(flag && weight!=0) targets.replace(target,weight);
        else if (flag) targets.remove(target); //  && weight==0
        else if (weight!=0) targets.put(target,weight); // && !flag
        checkRep();
        return pst_weight;
    }
    @Override
    public String toString(){
        String str;
        str = "";
        str += "\n顶点：" + this.key.toString();
        str += "\n父结点：" + this.sources;
        str += "\n子结点：" + this.targets;
        str += "\n------------------------------------------";
        return str;
    }
}
