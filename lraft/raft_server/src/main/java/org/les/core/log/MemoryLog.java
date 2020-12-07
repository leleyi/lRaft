package org.les.core.log;


import com.google.common.eventbus.EventBus;
import org.les.core.log.sequence.EntrySequence;
import org.les.core.log.sequence.MemoryEntrySequence;
import org.les.core.log.snapshot.EmptySnapshot;
import org.les.core.log.snapshot.Snapshot;

public class MemoryLog extends AbstractLog {

    private EventBus eventBus;

    public MemoryLog() {
        this(new EventBus());
    }

    public MemoryLog(EventBus eventBus) {
        this(new EmptySnapshot(), new MemoryEntrySequence(), eventBus);
    }

    public MemoryLog(Snapshot snapshot, EntrySequence entrySequence, EventBus eventBus) {
        super(eventBus);
        this.snapshot = snapshot;
        this.entrySequence = entrySequence;
    }
}