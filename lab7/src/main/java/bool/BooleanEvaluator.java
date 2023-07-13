package bool;

import org.json.JSONObject;

public class BooleanEvaluator {
    public static boolean evaluate(BooleanNode expression) {
        // Return the expression evaluated
        return expression.evaluate();
    }

    public static String prettyPrint(BooleanNode expression) {
        // Pretty print the expression
        return expression.prettyPrint();
    }

    public static void main(String[] args) {
        // Create a node factory
        NodeFactory nodeFactory = new NodeFactory();

        // Add nodes to the node factory
        // {
        // "node": "and",
        // "subnode1": {
        // "node": "or",
        // "subnode1": {
        // "node": "value",
        // "value": true
        // },
        // "subnode2": {
        // "node": "value",
        // "value": false
        // }
        // },
        // "subnode2": {
        // "node": "value",
        // "value": true
        // }
        // }
        nodeFactory.addNodeFromJson(new JSONObject("{" + "\"node\": \"and\"," + "\"subnode1\": {" + "\"node\": \"or\","
                + "\"subnode1\": {" + "\"node\": \"value\"," + "\"value\": true" + "}," + "\"subnode2\": {"
                + "\"node\": \"value\"," + "\"value\": false" + "}" + "}," + "\"subnode2\": {" + "\"node\": \"value\","
                + "\"value\": true" + "}" + "}"));

        // prettyPrint the expression
        // (AND (OR true false) true)
        assert (nodeFactory.printPretty() == "(AND (OR true false) true)");

        // evaluate the expression
        // true
        assert (nodeFactory.evaluate());
    }
}
