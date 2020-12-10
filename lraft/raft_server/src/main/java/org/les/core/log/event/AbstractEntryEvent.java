package org.les.core.log.event;

import org.les.core.log.entry.Entry;

public abstract class AbstractEntryEvent<T extends Entry> {

    protected final T entry;

    AbstractEntryEvent(T entry) {
        this.entry = entry;
    }

    public T getEntry() {
        return entry;
    }

}

