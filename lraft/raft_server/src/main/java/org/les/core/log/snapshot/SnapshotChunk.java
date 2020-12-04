package org.les.core.log.snapshot;

public class SnapshotChunk {
    public byte[] toByteArray() {
        return new byte[1];
    }

    public boolean isLastChunk() {
        return false;
    }
}
