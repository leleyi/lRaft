package org.les.kv.server;

import org.les.core.node.Node;
import org.les.kv.message.CommandRequest;
import org.les.kv.message.GetCommand;
import org.les.kv.message.SetCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Service {
    private static final Logger logger = LoggerFactory.getLogger(Service.class);

    private final ConcurrentMap<String, CommandRequest<?>> pendingCommands = new ConcurrentHashMap<>();
    private Map<String, byte[]> map = new HashMap<>();

    private final Node node;

    public Service(Node node) {
        this.node = node;
    }

    public void set(CommandRequest<SetCommand> setCommandCommandRequest) {
    }

    public void get(CommandRequest<GetCommand> getCommandCommandRequest) {

    }
}
