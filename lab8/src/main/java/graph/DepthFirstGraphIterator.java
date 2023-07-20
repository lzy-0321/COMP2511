package graph;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;

public class DepthFirstGraphIterator<N extends Comparable<N>> implements Iterator<N> {
    private Stack<N> stack = new Stack<>();
    private Set<N> visited = new HashSet<>();
    private Graph<N> graph;

    public DepthFirstGraphIterator(Graph<N> graph, N start) {
        this.graph = graph;
        this.stack.push(start);
        this.visited.add(start);
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public N next() {
        if (!hasNext()) {
            return null;
        }
        N current = stack.pop();
        List<N> neighbours = graph.getAdjacentNodes(current);
        for (N neighbour : neighbours) {
            if (!visited.contains(neighbour)) {
                stack.push(neighbour);
                visited.add(neighbour);
            }
        }
        return current;
    }
}
