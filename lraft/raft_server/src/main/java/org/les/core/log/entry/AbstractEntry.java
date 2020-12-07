package org.les.core.log.entry;

public class AbstractEntry implements Entry {

    private final int kind;
    protected final int index;
    protected final int term;

    public AbstractEntry(int kind, int index, int term) {
        this.kind = kind;
        this.index = index;
        this.term = term;
    }

    @Override
    public int getKind() {
        return kind;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getTerm() {
        return term;
    }

    @Override
    public byte[] getCommandBytes() {
        return null;
    }

    @Override
    public EntryMeta getMeta() {
        return null;
    }
}
