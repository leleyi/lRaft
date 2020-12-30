import org.les.core.node.Node;
import org.les.core.node.NodeBuilder;
import org.les.core.node.NodeEndpoint;
import org.les.core.node.NodeId;

import java.util.HashSet;
import java.util.Set;

public class raftServerC {
    public static void main(String[] args) {

        Set<NodeEndpoint> endpoints = new HashSet<>();
        NodeEndpoint endpoint1 = new NodeEndpoint("A", "127.0.0.1", 2222);
        NodeEndpoint endpoint2 = new NodeEndpoint("B", "127.0.0.1", 2223);
        NodeEndpoint endpoint3 = new NodeEndpoint("C", "127.0.0.1", 2224);

        endpoints.add(endpoint1);
        endpoints.add(endpoint2);
        endpoints.add(endpoint3);
        ///////////////////////////////////

        Node node = new NodeBuilder(endpoints, new NodeId("C")) // node builder 的时候 new a eventBus
                .build();

        node.start();
    }
}
