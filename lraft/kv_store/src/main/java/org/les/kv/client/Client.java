package org.les.kv.client;

import org.les.core.node.service.ServerRouter;
import org.les.kv.message.GetCommand;
import org.les.kv.message.SetCommand;

public class Client {

    private final ServerRouter serverRouter;

    public Client(ServerRouter serverRouter) {
        this.serverRouter = serverRouter;
    }


    public void set(String key, byte[] value) {
        serverRouter.send(new SetCommand(key, value));
    }

    public byte[] get(String key) {
        return (byte[]) serverRouter.send(new GetCommand(key));
    }

    public ServerRouter getServerRouter() {
        return serverRouter;
    }


    public void addNote(String nodeId, String host, int port) {
    }

    public void removeNode(String nodeId) {
    }
}

