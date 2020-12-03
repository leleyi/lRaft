package org.les.core.log.entry;


import com.google.common.eventbus.EventBus;

public class MemoryLog implements Log {
    private EventBus eventBus;

    public MemoryLog(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public NoOpEntry appendEntry(int newTerm) {
        return null;
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
