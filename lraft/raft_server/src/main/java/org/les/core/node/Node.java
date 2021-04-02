package org.les.core.node;


import org.les.core.log.state.StateMachine;
import org.les.core.node.role.RoleNameAndLeaderId;

import javax.annotation.Nonnull;

/**
 * Node.
 */
public interface Node {

    /**
     * Register state machine to node.
     * <p>State machine should be registered before node start, or it may not take effect.</p>
     *
     * @param stateMachine state machine
     */
    void registerStateMachine(@Nonnull StateMachine stateMachine);

    /**
     * Get current role name and leader id.
     * <p>
     * Available results:
     * </p>
     * <ul>
     * <li>FOLLOWER, current leader id</li>
     * <li>CANDIDATE, <code>null</code></li>
     * <li>LEADER, self id</li>
     * </ul>
     *
     * @return role name and leader id
     */
    @Nonnull
    RoleNameAndLeaderId getRoleNameAndLeaderId();

    /**
     * Add node role listener.
     *
     * @param listener listener
     */
    void addNodeRoleListener(@Nonnull NodeRoleListener listener);

    /**
     * Start node.
     */
    void start();

    /**
     * Append log.
     *
     * @param commandBytes command bytes
     * @throws NotLeaderException if not leader
     */
    void appendLog(@Nonnull byte[] commandBytes);

    /**
     * Stop node.
     *
     * @throws InterruptedException if interrupted
     */
    void stop() throws InterruptedException;

}
