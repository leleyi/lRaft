package org.les.core.log.entry;


import org.junit.Assert;
import org.junit.Test;
import org.les.core.node.NodeEndpoint;
import org.les.core.node.NodeId;

import java.util.Collections;
import java.util.Set;

public class AddNodeEntryTest {
    @Test
    public void testGetResultNodeEndpoints() {
        AddNodeEntry entry = new AddNodeEntry(1, 1, Collections.emptySet(),
                new NodeEndpoint("A", "localhost", 2333));
        Set<NodeEndpoint> nodeEndpoints = entry.getResultNodeEndpoints();
        Assert.assertEquals(1, nodeEndpoints.size());
        Assert.assertEquals(NodeId.of("A"), nodeEndpoints.iterator().next().getId());
    }

}
