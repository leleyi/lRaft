package org.les.core.log.snapshot;

import org.les.core.node.NodeEndpoint;

import java.io.InputStream;
import java.util.Set;

public class EmptySnapshot implements Snapshot {
    public EmptySnapshot() {
    }

    @Override
    public int getLastIncludedIndex() {
        return 0;
    }

    @Override
    public int getLastIncludedTerm() {
        return 0;
    }

    @Override
    public Set<NodeEndpoint> getLastConfig() {
        return null;
    }

    @Override
    public long getDataSize() {
        return 0;
    }

    @Override
    public SnapshotChunk readData(int offset, int length) {
        return null;
    }

    @Override
    public InputStream getDataStream() {
        return null;
    }

    @Override
    public void close() {

    }
}
