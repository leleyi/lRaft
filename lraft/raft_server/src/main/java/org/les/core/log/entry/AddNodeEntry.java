package org.les.core.log.entry;

import org.les.core.node.NodeEndpoint;

import java.util.Set;

public class AddNodeEntry extends GroupConfigEntry {


    private final NodeEndpoint newNodeEndpoint;

    public AddNodeEntry(int index, int term, Set<NodeEndpoint> nodeEndpoints, NodeEndpoint newNodeEndpoint) {
        super(KIND_ADD_NODE, index, term, nodeEndpoints);
        this.newNodeEndpoint = newNodeEndpoint;
    }


    @Override
    public Set<NodeEndpoint> getResultNodeEndpoints() {
        return null;
    }
}
