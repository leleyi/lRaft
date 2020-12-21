package org.les.core.log;

import com.google.common.eventbus.EventBus;
import org.les.core.log.entry.EntryMeta;
import org.les.core.log.entry.NoOpEntry;
import org.les.core.log.snapshot.Snapshot;
import org.les.core.log.snapshot.SnapshotBuilder;
import org.les.core.rpc.message.InstallSnapshotRpc;

import java.io.File;

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

}
