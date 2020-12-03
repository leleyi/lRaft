package org.les.core.log.entry;

import com.google.common.eventbus.EventBus;
import org.les.core.log.sequence.EntrySequence;
import org.les.core.log.snapshot.Snapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractLog implements Log {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLog.class);
    protected final EventBus eventBus;
    protected Snapshot snapshot;
    protected EntrySequence entrySequence;

    AbstractLog(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public NoOpEntry appendEntry(int newTerm) {
        return null;
    }


    @Override
    public EntryMeta getLastEntryMeta() {
        if (entrySequence.isEmpty()) {
            return new EntryMeta(Entry.KIND_NO_OP, snapshot.getLastIncludedIndex(), snapshot.getLastIncludedTerm());
        }
        return entrySequence.getLastEntry().getMeta();
    }


    @Override
    public int getNextIndex() {
        return 0;
    }

    @Override
    public boolean isNewerThan(int lastLogIndex, int lastLogTerm) {
        EntryMeta lastEntryMeta = getLastEntryMeta();
        logger.debug("last entry ({}, {}), candidate ({}, {})", lastEntryMeta.getIndex(), lastEntryMeta.getTerm(), lastLogIndex, lastLogTerm);
        return lastEntryMeta.getTerm() > lastLogTerm || lastEntryMeta.getIndex() > lastLogIndex;
    }
}
