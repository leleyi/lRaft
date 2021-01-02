package org.les.core.log;

import com.google.common.eventbus.EventBus;
import org.les.core.log.entry.EntryMeta;
import org.les.core.log.entry.NoOpEntry;
import org.les.core.log.snapshot.Snapshot;
import org.les.core.log.snapshot.SnapshotBuilder;
import org.les.core.log.state.StateMachine;
import org.les.core.node.NodeEndpoint;
import org.les.core.node.NodeId;
import org.les.core.rpc.message.InstallSnapshotRpc;

import java.io.File;
import java.util.Set;

public class FileLog extends AbstractLog {

    public FileLog(File dataDir, EventBus eventBus) {
        super(eventBus);
    }

    @Override
    public NoOpEntry appendEntry(int newTerm) {
        return null;
    }

    @Override
    protected SnapshotBuilder newSnapshotBuilder(InstallSnapshotRpc rpc) {
        return null;
    }

    @Override
    protected void replaceSnapshot(Snapshot newSnapshot) {

    }

    @Override
    public EntryMeta getLastEntryMeta() {
        return null;
    }

    @Override
    public int getNextIndex() {
        return 0;
    }

    @Override
    public void setStateMachine(StateMachine stateMachine) {

    }

    @Override
    public void generateSnapshot(int lastIncludedIndex, Set<NodeEndpoint> groupConfig) {

    }

    @Override
    public void close() {

    }

    @Override
    public void appendEntryForRemoveNode(int term, Set<NodeEndpoint> nodeEndpoints, NodeId nodeId) {

    }

}
