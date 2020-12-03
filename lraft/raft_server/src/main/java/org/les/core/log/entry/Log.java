package org.les.core.log.entry;

public interface Log {

    NoOpEntry appendEntry(int newTerm);

    EntryMeta getLastEntryMeta();

    int getNextIndex();
}
