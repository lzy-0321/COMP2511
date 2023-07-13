package bool;

public class BooleanLeaf implements BooleanNode {
    private final boolean value;

    public BooleanLeaf(boolean value) {
        this.value = value;
    }

    @Override
    public boolean evaluate() {
        return value;
    }

    @Override
    public String prettyPrint() {
        return String.valueOf(value);
    }
}
