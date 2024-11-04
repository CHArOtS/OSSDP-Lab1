/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import P1.graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   AF(graph) = 由语料库中全部单词按规则生成的“单词亲和图”
    // Representation invariant:
    //   graph中边权全部为正
    //   完成语料库读入后graph非空
    //   不存在内容单词重复的顶点 （由Graph类保证）
    // Safety from rep exposure:
    //   'private + final'
    //   返回值均为Immutable的数据类型
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        FileReader fileReader = new FileReader(corpus);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        String[] line_words;
        List<String> word_list = new ArrayList<>();
        while((line = bufferedReader.readLine()) != null){
            line = line.toLowerCase();
            line_words = line.trim().split(" ");
            for(String word: line_words){
                word_list.add(word.replaceAll("[^a-z]*",""));
            }
        }
        bufferedReader.close();

        int n = word_list.size();
        String source, target;
        for(int i = 0; i < n - 1; i++){
            source = word_list.get(i);
            target = word_list.get(i+1);
            graph.add(source);
            graph.add(target);
            if(graph.targets(source).containsKey(target))
                graph.set(source,target,graph.targets(source).get(target)+1);
            else graph.set(source,target,1);
        }
        checkRep();
        // throw new RuntimeException("not implemented");
    }
    
    // checkRep
    public void checkRep(){
        assert !graph.vertices().isEmpty(); // 数量上非空
        assert !graph.equals(Graph.empty()); // 性质上非空
        // 确保各边权位正值
        for(String vertex: graph.vertices()){
            Map<String, Integer> temp_map = new HashMap<>(graph.targets(vertex));
            for(String temp_vertex: temp_map.keySet()){
                assert temp_map.get(temp_vertex) > 0;
            }
        }
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        String[] input_split = input.trim().split(" ");
        String head,tail,low_head,low_tail;
        List<String> input_list = new ArrayList<>(Arrays.asList(input_split));
        StringBuilder stringBuilder = new StringBuilder();
        int n = input_list.size();

        for(int i = 0; i < n-1; i++){
            head = input_list.get(i);
            tail = input_list.get(i+1);
            low_head = head.replaceAll("[^A-Za-z]*","").toLowerCase();
            low_tail = tail.replaceAll("[^A-Za-z]*","").toLowerCase();
            stringBuilder.append(head).append(" ");

            if(graph.vertices().contains(low_head)){
                Map<String, Integer> w1_to_b = graph.targets(low_head);
                Map<String, Integer> w1_to_w2 = new HashMap<>();
                for(String b_possible: w1_to_b.keySet()){
                    if(graph.targets(b_possible).containsKey(low_tail)){
                        int weight = graph.targets(low_head).get(b_possible);
                        weight = weight + graph.sources(low_tail).get(b_possible);
                        w1_to_w2.put(b_possible, weight);
                    }
                }
                if(!w1_to_w2.isEmpty()){
                    String b = null;
                    int max_weight = 0;
                    // 优先选择边权大且靠前出现的
                    for(String temp_b: w1_to_w2.keySet()){
                        if(w1_to_w2.get(temp_b)>max_weight){
                            b = temp_b;
                            max_weight = w1_to_w2.get(temp_b);
                        }
                    }
                    stringBuilder.append(b).append(" ");
                }
            }
        }
        stringBuilder.append(input_list.get(n-1));
        return stringBuilder.toString();
        // throw new RuntimeException("not implemented");
    }
    
    // toString()
    @Override
    public String toString(){
        return graph.toString();
    }
}
