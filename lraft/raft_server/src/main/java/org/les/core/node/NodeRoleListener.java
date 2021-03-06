package org.les.core.node;


import org.les.core.node.role.RoleState;

import javax.annotation.Nonnull;

/**
 * Node role listener.
 */
public interface NodeRoleListener {

    /**
     * Called when node role changes. e.g FOLLOWER to CANDIDATE.
     *
     * @param roleState role state
     */
    void nodeRoleChanged(@Nonnull RoleState roleState);

}
