package org.les.core.rpc.message;


import org.les.core.node.NodeId;
import org.les.core.rpc.Channel;

import javax.annotation.Nullable;

public class InstallSnapshotRpcMessage extends AbstractRpcMessage<InstallSnapshotRpc> {

    public InstallSnapshotRpcMessage(InstallSnapshotRpc rpc, NodeId sourceNodeId, @Nullable Channel channel) {
        super(rpc, sourceNodeId, channel);
    }

}
