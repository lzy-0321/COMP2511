package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Invalidation {

    // TODO uncomment the code in this method once you have completed the other
    // parts of the graph exercise
    // they are required for this code to work
    private static void graphInvalidation() {
        Graph<Integer> graph = new Graph<>();

        graph.addNode(5);
        graph.addNode(4);
        graph.addNode(7);

        // degenerate graph...a line
        graph.addConnection(5, 4);
        graph.addConnection(4, 7);

        Set<Integer> nodesToRemove = new HashSet<>();

        for (Integer val1 : graph) {
            System.out.println(val1);
            for (Integer val2 : graph) {
                if (val1 > val2)
                    nodesToRemove.add(val2);
            }
        }

        for (Integer node : nodesToRemove) {
            graph.removeNode(node);
        }
    }

    private static void listInvalidation() {
        List<Integer> list = new ArrayList<>();

        list.add(5);
        list.add(4);
        list.add(7);

        List<Integer> toRemove = new ArrayList<>();

        for (Integer val1 : list) {
            System.out.println(val1);
            for (Integer val2 : list) {
                if (val1 > val2)
                    toRemove.add(val2);
            }
        }

        list.removeAll(toRemove);
    }

    public static void main(String[] args) {
        graphInvalidation();

        // the list invalidation is a bit similar to the graph one above. Uncomment this
        // and comment out the graph one if you think your graph iterators aren't
        // working or if you need a hint
        System.out.println("--------------------");

        listInvalidation();
    }
}
