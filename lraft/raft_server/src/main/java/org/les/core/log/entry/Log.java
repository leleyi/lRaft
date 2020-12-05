package org.les.core.log.entry;

import org.les.core.node.NodeId;
import org.les.core.rpc.message.AppendEntriesRpc;
import org.les.core.rpc.message.InstallSnapshotRpc;

import java.util.List;

public interface Log {
    int ALL_ENTRIES = -1;

    NoOpEntry appendEntry(int newTerm);

    EntryMeta getLastEntryMeta();

    int getNextIndex();

    boolean isNewerThan(int lastLogIndex, int lastLogTerm);

    AppendEntriesRpc createAppendEntriesRpc(int term, NodeId selfId, int nextIndex, int maxEntries);

    InstallSnapshotRpc createInstallSnapshotRpc(int term, NodeId selfId, int i, int snapshotDataLength);

    void advanceCommitIndex(int i, int term);

    boolean appendEntriesFromLeader(int prevLogIndex, int prevLogTerm, List<Entry> entries);
}
