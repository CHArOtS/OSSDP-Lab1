/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P1.poet;

import java.io.File;
import java.io.IOException;

/**
 * Example program using GraphPoet.
 * 
 * <p>PS2 instructions: you are free to change this example class.
 */
public class Main {
    
    /**
     * Generate example poetry.
     * 
     * @param args unused
     * @throws IOException if a poet corpus file cannot be found or read
     */
    public static void main(String[] args) throws IOException {
        final GraphPoet GentleGoodNight = new GraphPoet(new File("src/P1/poet/Do Not Go Gentle into That Good Night.txt"));
        final String input = "Go into a bay, rage with sight.";
        System.out.println(input + "\n>>>\n" + GentleGoodNight.poem(input));
    }
    
}
