package org.les.kv.server;

import org.les.core.node.Node;
import org.les.kv.message.CommandRequest;
import org.les.kv.message.GetCommand;
import org.les.kv.message.SetCommand;

public class Service {

    private final Node node;

    public Service(Node node) {
        this.node = node;
    }

    public void set(CommandRequest<SetCommand> setCommandCommandRequest) {

    }

    public void get(CommandRequest<GetCommand> getCommandCommandRequest) {

    }
}
