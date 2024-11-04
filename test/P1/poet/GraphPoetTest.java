/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Testing strategy
    /*
        testPoem():
        保留原本类内spec中提供的两个测试用例（转为Hello and goodbye.txt和mugar-omni-thater.txt)
        确保代码能够正常运行 并在可通往多个单词顶点的情况下优先选择权值较高的节点
        之后任意选取部分歌词和诗歌 代入用户的身份编写测试用例

        testToString():
        选择任一txt文件作为文件流读入 直接测试
     */
    // tests

    @Test
    public void testPoem1() throws IOException {

        String input = "Hello! goodbye!";
        String supposeOutput = "Hello! hello goodbye!";
        File file = new File("src/P1/poet/Hello and goodbye.txt");
        GraphPoet graphPoet = new GraphPoet(file);
        assertEquals(supposeOutput,graphPoet.poem(input));
    }
    @Test
    public void testPoem2() throws IOException {

        String input = "Go into a bay, rage with sight";
        String supposeOutput = "Go gentle into a green bay, rage rage with blinding sight";
        File file = new File("src/P1/poet/Do Not Go Gentle into That Good Night.txt");
        GraphPoet graphPoet = new GraphPoet(file);
        assertEquals(supposeOutput,graphPoet.poem(input));
    }
    @Test
    public void testPoem3() throws IOException {

        String input = "Wind whines through tips. Why you my remedy?";
        String supposeOutput = "Wind whines through broken tips. Why you still my remedy?";
        File file = new File("src/P1/poet/Magnolia.txt");
        GraphPoet graphPoet = new GraphPoet(file);
        assertEquals(supposeOutput,graphPoet.poem(input));
    }
    @Test
    public void testPoem4() throws IOException {

        String input = "I love you deeply immensely, with my heart.";
        String supposeOutput = "I love you always deeply genuinely immensely, steadily with all my heart.";
        File file = new File("src/P1/poet/Marigold.txt");
        GraphPoet graphPoet = new GraphPoet(file);
        assertEquals(supposeOutput,graphPoet.poem(input));
    }
    @Test
    public void testPoem5() throws IOException {

        String input = "Test the system.";
        String supposeOutput = "Test of the system.";
        File file = new File("src/P1/poet/mugar-omni-theater.txt");
        GraphPoet graphPoet = new GraphPoet(file);
        assertEquals(supposeOutput,graphPoet.poem(input));
    }
    @Test
    public void testPoem6() throws IOException {

        String input = "I have been a dreamer so long.";
        String supposeOutput = "I have been a dreamer so long.";
        File file = new File("src/P1/poet/The road not taken.txt");
        GraphPoet graphPoet = new GraphPoet(file);
        assertEquals(supposeOutput,graphPoet.poem(input));
    }
    @Test
    public void testPoem7() throws IOException {

        String input = "To see how a rose come up, to feel the heart of yourself";
        String supposeOutput = "To see how a yellow rose come up, to feel the central heart of yourself";
        File file = new File("src/P1/poet/What can I hold you with.txt");
        GraphPoet graphPoet = new GraphPoet(file);
        assertEquals(supposeOutput,graphPoet.poem(input));
    }
    @Test
    public void testToString() throws IOException{
        File file = new File("src/P1/poet/mugar-omni-theater.txt");
        GraphPoet graphPoet = new GraphPoet(file);
        String AccOutput = "\n------------------------------------------\n"
        + "顶点：this\n"
        + "父结点：{}\n"
        + "子结点：{is=1}\n"
        + "------------------------------------------\n"
        + "顶点：is\n"
        + "父结点：{this=1}\n"
        + "子结点：{a=1}\n"
        + "------------------------------------------\n"
        + "顶点：a\n"
        + "父结点：{is=1}\n"
        + "子结点：{test=1}\n"
        + "------------------------------------------\n"
        + "顶点：test\n"
        + "父结点：{a=1}\n"
        + "子结点：{of=1}\n"
        + "------------------------------------------\n"
        + "顶点：of\n"
        + "父结点：{test=1}\n"
        + "子结点：{the=1}\n"
        + "------------------------------------------\n"
        + "顶点：the\n"
        + "父结点：{of=1}\n"
        + "子结点：{mugar=1}\n"
        + "------------------------------------------\n"
        + "顶点：mugar\n"
        + "父结点：{the=1}\n"
        + "子结点：{omni=1}\n"
        + "------------------------------------------\n"
        + "顶点：omni\n"
        + "父结点：{mugar=1}\n"
        + "子结点：{theater=1}\n"
        + "------------------------------------------\n"
        + "顶点：theater\n"
        + "父结点：{omni=1}\n"
        + "子结点：{sound=1}\n"
        + "------------------------------------------\n"
        + "顶点：sound\n"
        + "父结点：{theater=1}\n"
        + "子结点：{system=1}\n"
        + "------------------------------------------\n"
        + "顶点：system\n"
        + "父结点：{sound=1}\n"
        + "子结点：{}\n"
        + "------------------------------------------";
        assertEquals(AccOutput, graphPoet.toString());
    }
    
}
