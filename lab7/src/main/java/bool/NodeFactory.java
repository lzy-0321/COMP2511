package bool;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class NodeFactory {
    private List<BooleanNode> nodeList = new ArrayList<BooleanNode>();

    public void addNodeFromJson(JSONObject jsonObject) {
        BooleanNode node = createNodeFromJson(jsonObject);
        nodeList.add(node);
    }

    public String printPretty() {
        // Pretty print the expression for the first node
        return nodeList.get(0).prettyPrint();
    }

    public boolean evaluate() {
        // Return the expression evaluated for the first node
        return nodeList.get(0).evaluate();
    }

    private BooleanNode createNodeFromJson(JSONObject jsonObject) {
        String nodeType = jsonObject.getString("node");
        BooleanNode node;

        switch (nodeType) {
        case "and":
            node = new NodeAnd(createNodeFromJson(jsonObject.getJSONObject("subnode1")),
                    createNodeFromJson(jsonObject.getJSONObject("subnode2")));
            break;
        case "or":
            node = new NodeOr(createNodeFromJson(jsonObject.getJSONObject("subnode1")),
                    createNodeFromJson(jsonObject.getJSONObject("subnode2")));
            break;
        case "not":
            node = new NodeNot(createNodeFromJson(jsonObject.getJSONObject("subnode1")));
            break;
        case "value":
            node = new BooleanLeaf(jsonObject.getBoolean("value"));
            break;
        default:
            throw new IllegalArgumentException("Unknown node type: " + nodeType);
        }

        return node;
    }
}
