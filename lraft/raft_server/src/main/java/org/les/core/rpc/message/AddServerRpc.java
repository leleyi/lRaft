package org.les.core.rpc.message;


import org.les.core.node.NodeEndpoint;

public class AddServerRpc {

    private final NodeEndpoint newServer;

    public AddServerRpc(NodeEndpoint newServer) {
        this.newServer = newServer;
    }

    public NodeEndpoint getNewServer() {
        return newServer;
    }

    @Override
    public String toString() {
        return "AddServerRpc{" +
                "newServer=" + newServer +
                '}';
    }

}
