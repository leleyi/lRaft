package org.les.core.log.sequence;

import org.les.core.log.entry.Entry;
import org.les.core.log.entry.EntryMeta;

import java.util.ArrayList;
import java.util.List;

public class MemoryEntrySequence extends AbstractEntrySequence {

    private final List<Entry> entries = new ArrayList<>();

    public MemoryEntrySequence() {
        this(1);
    }

    public MemoryEntrySequence(int logIndexOffset) {
        super(logIndexOffset);
    }

    @Override
    protected Entry doGetEntry(int index) {
        return entries.get(index - logIndexOffset);
    }

    @Override
    public int getNextLogIndex() {
        return 0;
    }

    @Override
    public List<Entry> subView(int fromIndex) {
        return null;
    }

    @Override
    public List<Entry> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public GroupConfigEntryList buildGroupConfigEntryList() {
        return null;
    }

    @Override
    public EntryMeta getEntryMeta(int index) {
        return null;
    }

    @Override
    public Entry getEntry(int index) {
        return null;
    }

    @Override
    public void append(Entry entry) {

    }

    @Override
    public void append(List<Entry> entries) {

    }

    @Override
    public void commit(int index) {

    }

    @Override
    public int getCommitIndex() {
        return 0;
    }

    @Override
    public void removeAfter(int index) {

    }

    @Override
    public void close() {

    }
}
