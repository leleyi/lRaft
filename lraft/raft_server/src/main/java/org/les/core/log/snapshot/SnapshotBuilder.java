package org.les.core.log.snapshot;

import org.les.core.rpc.message.InstallSnapshotRpc;

public interface SnapshotBuilder<T extends Snapshot> {

    void append(InstallSnapshotRpc rpc);

    T build();

    void close();

}

