package org.les.core.log;


import com.google.common.eventbus.EventBus;
import org.les.core.log.sequence.EntrySequence;
import org.les.core.log.sequence.MemoryEntrySequence;
import org.les.core.log.snapshot.EmptySnapshot;
import org.les.core.log.snapshot.Snapshot;
import org.les.core.log.snapshot.SnapshotBuilder;
import org.les.core.rpc.message.InstallSnapshotRpc;

public class MemoryLog extends AbstractLog {

    private EventBus eventBus;

    public MemoryLog() {
        this(new EventBus());
    }

    public MemoryLog(EventBus eventBus) {
        this(new EmptySnapshot(), new MemoryEntrySequence(), eventBus);
    }

    @Override
    protected SnapshotBuilder newSnapshotBuilder(InstallSnapshotRpc rpc) {
        return null;
    }

    @Override
    protected void replaceSnapshot(Snapshot newSnapshot) {

    }

    public MemoryLog(Snapshot snapshot, EntrySequence entrySequence, EventBus eventBus) {
        super(eventBus);
        this.snapshot = snapshot;
        this.entrySequence = entrySequence;
    }
}
