package org.les.core.log.snapshot;

import org.les.core.log.LogException;



public class EntryInSnapshotException extends LogException {

    private final int index;

    public EntryInSnapshotException(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}
