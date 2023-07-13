package bool;

public class NodeNot implements BooleanNode {
    private BooleanNode node;

    public NodeNot(BooleanNode node) {
        this.node = node;
    }

    public boolean evaluate() {
        return !node.evaluate();
    }

    public String prettyPrint() {
        return "(NOT" + node.prettyPrint() + ")";
    }
}
