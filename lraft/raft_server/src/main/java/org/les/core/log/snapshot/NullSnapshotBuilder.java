package org.les.core.log.snapshot;

import org.les.core.rpc.message.InstallSnapshotRpc;

public class NullSnapshotBuilder implements SnapshotBuilder {

    @Override
    public void append(InstallSnapshotRpc rpc) {
    }

    @Override
    public Snapshot build() {
        return null;
    }

    @Override
    public void close() {

    }
}
