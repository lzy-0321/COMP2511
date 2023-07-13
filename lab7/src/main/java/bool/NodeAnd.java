package bool;

public class NodeAnd implements BooleanNode {
    // a node has left and right, the value is after opeartion
    private BooleanNode left;
    private BooleanNode right;

    public NodeAnd(BooleanNode left, BooleanNode right) {
        this.left = left;
        this.right = right;
    }

    public boolean evaluate() {
        return left.evaluate() && right.evaluate();
    }

    public String prettyPrint() {
        return "(AND" + left.prettyPrint() + right.prettyPrint() + ")";
    }
}
