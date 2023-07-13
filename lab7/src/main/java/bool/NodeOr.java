package bool;

public class NodeOr implements BooleanNode {
    // a node has left and right, the value is after opeartion
    private BooleanNode left;
    private BooleanNode right;

    public NodeOr(BooleanNode left, BooleanNode right) {
        this.left = left;
        this.right = right;
    }

    public boolean evaluate() {
        return left.evaluate() || right.evaluate();
    }

    public String prettyPrint() {
        return "(OR" + left.prettyPrint() + right.prettyPrint() + ")";
    }
}
