package graph;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class BreadthFirstGraphIterator<N extends Comparable<N>> implements Iterator<N> {
    private Queue<N> queue = new LinkedList<>();
    private Set<N> visited = new HashSet<>();
    private Graph<N> graph;

    public BreadthFirstGraphIterator(Graph<N> graph, N start) {
        this.graph = graph;
        this.queue.add(start);
        this.visited.add(start);
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public N next() {
        if (!hasNext()) {
            return null;
        }
        N current = queue.remove();
        List<N> neighbours = graph.getAdjacentNodes(current);
        for (N neighbour : neighbours) {
            if (!visited.contains(neighbour)) {
                queue.add(neighbour);
                visited.add(neighbour);
            }
        }
        return current;
    }
}
