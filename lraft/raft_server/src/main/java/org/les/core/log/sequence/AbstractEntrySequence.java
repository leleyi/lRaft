package org.les.core.log.sequence;

import org.les.core.log.entry.Entry;

abstract class AbstractEntrySequence implements EntrySequence {
    int logIndexOffset;
    int nextLogIndex;

    public AbstractEntrySequence(int logIndexOffset) {
        this.logIndexOffset = logIndexOffset;
        this.nextLogIndex = logIndexOffset;
    }

    @Override
    public boolean isEmpty() {
        return logIndexOffset == nextLogIndex;
    }

    @Override
    public int getFirstLogIndex() {
        // todo
        return doGetFirstLogIndex();
    }

    private int doGetFirstLogIndex() {
        return logIndexOffset;
    }

    @Override
    public int getLastLogIndex() {
        // todo
        return nextLogIndex - 1;
    }

    @Override
    public boolean isEntryPresent(int index) {
        return !isEmpty() && index >= doGetFirstLogIndex() && index <= doGetLastLogIndex();
    }

    @Override
    public Entry getLastEntry() {
        return isEmpty() ? null : doGetEntry(doGetLastLogIndex());
    }

    protected abstract Entry doGetEntry(int doGetLastLogIndex);

    private int doGetLastLogIndex() {
        return nextLogIndex - 1;
    }

}
