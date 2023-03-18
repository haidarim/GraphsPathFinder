import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.ArrayList;
/**
 * A graph that encodes word ladders.
 *
 * The class does not store the full graph in memory, just a dictionary of words.
 * The edges are then computed on demand.
 */
public class WordLadder implements DirectedGraph<String> {

    private final Set<String> dictionary;
    private final Set<Character> alphabet;

    /**
     * Creates a new empty graph.
     */
    public WordLadder() {
        dictionary = new HashSet<>();
        alphabet = new HashSet<>();
    }

    /**
     * Adds the {@code word} to the dictionary if it only contains letters.
     * The word is converted to lowercase.
     * @param word  the word
     */
    public void addWord(String word) {
        if (word.matches("\\p{L}+")) {
            word = word.toLowerCase();
            dictionary.add(word);
            for (char c : word.toCharArray())
                alphabet.add(c);
        }
    }

    /**
     * Creates a new word ladder graph from the given dictionary file.
     * The file should contain one word per line, except lines starting with "#".
     * @param file  path to a text file
     */
    public WordLadder(String file) throws IOException {
        this();
        Files.lines(Paths.get(file))
            .filter(line -> !line.startsWith("#"))
            .map(String::trim)
            .forEach(this::addWord);
    }

    @Override
    public Set<String> nodes() {
        return Collections.unmodifiableSet(dictionary);
    }

    /**
     * @param  w  a graph node (a word)
     * @return a list of the graph edges that originate from {@code w}
     */
    @Override
    public List<DirectedEdge<String>> outgoingEdges(String w) {
        /*****************
         * TODO: Task 2  *
         * Replace this. *
         *****************/
        //return new LinkedList<>();
        
        int i = 0;
        LinkedList<DirectedEdge<String>> edges = new LinkedList();

        while(i<w.length()){
            //efter första, alla samma  
            if(i==0){
                for(Character c : alphabet){
                    String target = c + w.substring(i+1);
                    
                    if(dictionary.contains(target) && !(target.equals(w))){
                        edges.add(new DirectedEdge(w, target));
                    }
                }
            }
            // 0 till i |och| i+1 till slutet samma 
            else{
                for(Character c : alphabet){
                    String target = w.substring(0,i) + c + w.substring(i+1);
                    
                    if(dictionary.contains(target) && !(target.equals(w))){
                        edges.add(new DirectedEdge(w, target));
                    }
                }
            }
            i++;
        }
        return edges;
    }

    /**
     * @param  w  one node/word
     * @param  u  another node/word
     * @return the guessed best cost for getting from {@code w} to {@code u}
     * (the number of differing character positions)
     */
    @Override
    public double guessCost(String w, String u) {
        /*****************
         * TODO: Task 4  *
         * Replace this. *
         *****************/
        int cost = 0;
        if(w.length() == u.length()){
            for(int i = 0; i<u.length(); i++){
                if(w.charAt(i) != u.charAt(i)){
                    cost++;
                }
            }
        }
        return cost;
    }

    @Override
    public String parseNode(String w) {
        return w;
    }

    /**
     * @return a string representation of the graph
     */
    @Override
    public String toString() {
        StringWriter buffer = new StringWriter();
        PrintWriter w = new PrintWriter(buffer);
        w.println("Word ladder graph with " + numNodes() + " words");
        w.println("Alphabet: " + alphabet.stream().map(Object::toString).collect(Collectors.joining()));
        w.println();

        w.println("Random example words with ladder steps:");
        DirectedEdge.printOutgoingEdges(w, this, null);
        return buffer.toString();
    }

}