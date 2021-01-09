package org.les.kv.client;


import org.les.core.node.NodeId;
import org.les.core.node.service.ServerRouter;
import org.les.core.rpc.Address;

import java.util.Map;


public class CommandContext {

    private final Map<NodeId, Address> serverMap;
    private Client client;

    volatile boolean running = false;

    public CommandContext(Map<NodeId, Address> serverMap) {
        this.serverMap = serverMap;
        client = new Client(buildServerRouter(serverMap));
    }

    private ServerRouter buildServerRouter(Map<NodeId, Address> serverMap) {
        ServerRouter router = new ServerRouter();
        for (NodeId nodeId :
                serverMap.keySet()) {
            Address address = serverMap.get(nodeId);
            router.add(nodeId, new SocketChannel(address.getHost(), address.getPort()));
        }
        return router;
    }

    public void setRunning(boolean running) {
        running = running;
    }

    public boolean isRunning() {
        return running;
    }

    public void printSeverList() {

    }

    public Client getClient() {
        return client;
    }
}
