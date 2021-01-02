package org.les.core.log;

import org.les.core.log.entry.*;
import org.les.core.log.snapshot.InstallSnapshotState;
import org.les.core.log.state.StateMachine;
import org.les.core.node.NodeEndpoint;
import org.les.core.node.NodeId;
import org.les.core.rpc.message.AppendEntriesRpc;
import org.les.core.rpc.message.InstallSnapshotRpc;

import java.util.List;
import java.util.Set;

public interface Log {
    int ALL_ENTRIES = -1;

    /**
     * heart beat
     *
     * @param newTerm
     * @return
     */
    NoOpEntry appendEntry(int newTerm);

    /**
     * command entry
     *
     * @param newTerm
     * @param command
     * @return
     */
    GeneralEntry appendEntry(int newTerm, byte[] command);

    /**
     * add Node command
     *
     * @param term
     * @param nodeEndpoints
     * @param newNodeEndpoint
     * @return
     */
    AddNodeEntry appendEntryForAddNode(int term, Set<NodeEndpoint> nodeEndpoints, NodeEndpoint newNodeEndpoint);

    EntryMeta getLastEntryMeta();

    int getNextIndex();

    boolean isNewerThan(int lastLogIndex, int lastLogTerm);

    AppendEntriesRpc createAppendEntriesRpc(int term, NodeId selfId, int nextIndex, int maxEntries);

    InstallSnapshotRpc createInstallSnapshotRpc(int term, NodeId selfId, int i, int snapshotDataLength);

    void advanceCommitIndex(int i, int term);

    boolean appendEntriesFromLeader(int prevLogIndex, int prevLogTerm, List<Entry> entries);

    InstallSnapshotState installSnapshot(InstallSnapshotRpc rpc);


    void setStateMachine(StateMachine stateMachine);

    /**
     * Generate snapshot.
     *
     * @param lastIncludedIndex last included index
     * @param groupConfig       group config
     */
    void generateSnapshot(int lastIncludedIndex, Set<NodeEndpoint> groupConfig);

    void close();

    void appendEntryForRemoveNode(int term, Set<NodeEndpoint> nodeEndpoints, NodeId nodeId);
}
