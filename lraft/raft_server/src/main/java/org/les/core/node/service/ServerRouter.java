package org.les.core.node.service;

import org.les.core.node.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ServerRouter {
    private static Logger logger = LoggerFactory.getLogger(ServerRouter.class);
    private final Map<NodeId, Channel> availableServers = new HashMap<>();
    private NodeId leaderId;

    public Object send(Object payLoad) {
        for (NodeId nodeId :
                getCandidateNodeIds()) {
            Object result = doSend(nodeId, payLoad);
            this.leaderId = nodeId;
            return result;
        }
        return null;
    }


    private Collection<NodeId> getCandidateNodeIds() {
        if (availableServers.isEmpty()) {
            throw new NoAvailableServerException("no available server");
        }
        if (leaderId != null) {
            List<NodeId> nodeIds = new ArrayList<>();
            nodeIds.add(leaderId);
            for (NodeId nodeId : availableServers.keySet()) {
                if (!nodeId.equals(leaderId)) {
                    nodeIds.add(nodeId);
                }
            }
            return nodeIds;
        }
        return availableServers.keySet();
    }

    private Object doSend(NodeId id, Object payLoad) {
        Channel channel = this.availableServers.get(id);
        if (channel == null) {
            throw new IllegalStateException("no such channel to server" + id);
        }
        return channel.send(payLoad);
    }

    public void add(NodeId id, Channel channel) {
        this.availableServers.put(id, channel);
    }
}
