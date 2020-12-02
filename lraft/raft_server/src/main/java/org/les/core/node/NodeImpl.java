package org.les.core.node;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.FutureCallback;
import org.les.core.node.role.AbstractNodeRole;
import org.les.core.node.role.FollowerNodeRole;
import org.les.core.node.role.RoleNameAndLeaderId;
import org.les.core.node.role.RoleState;
import org.les.core.node.store.NodeStore;
import org.les.core.node.task.GroupConfigChangeTaskHolder;
import org.les.core.node.task.GroupConfigChangeTaskReference;
import org.les.core.node.task.NewNodeCatchUpTaskGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.les.core.schedule.ElectionTimeout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Node implementation.
 *
 * @see NodeContext
 */
@ThreadSafe
public class NodeImpl implements Node {

    private static final Logger logger = LoggerFactory.getLogger(NodeImpl.class);

    // callback for async tasks.
    private static final FutureCallback<Object> LOGGING_FUTURE_CALLBACK = new FutureCallback<Object>() {
        @Override
        public void onSuccess(@Nullable Object result) {
        }

        @Override
        public void onFailure(@Nonnull Throwable t) {
            logger.warn("failure", t);
        }
    };

    private final NodeContext context;
    @GuardedBy("this")
    private boolean started;
    private volatile AbstractNodeRole role;
    private final List<NodeRoleListener> roleListeners = new CopyOnWriteArrayList<>();

    // NewNodeCatchUpTask and GroupConfigChangeTask related
//    private final NewNodeCatchUpTaskContext newNodeCatchUpTaskContext = new NewNodeCatchUpTaskContextImpl();
    private final NewNodeCatchUpTaskGroup newNodeCatchUpTaskGroup = new NewNodeCatchUpTaskGroup();
    //    private final GroupConfigChangeTaskContext groupConfigChangeTaskContext = new GroupConfigChangeTaskContextImpl();
    private volatile GroupConfigChangeTaskHolder groupConfigChangeTaskHolder = new GroupConfigChangeTaskHolder();

    /**
     * Create with context.
     *
     * @param context context
     */
    NodeImpl(NodeContext context) {
        this.context = context;
    }

    /**
     * Get context.
     *
     * @return context
     */
    NodeContext getContext() {
        return context;
    }

    @Override
    public synchronized void registerStateMachine(@Nonnull StateMachine stateMachine) {
        Preconditions.checkNotNull(stateMachine);
//        context.log().setStateMachine(stateMachine);
    }

    @Override
    @Nonnull
    public RoleNameAndLeaderId getRoleNameAndLeaderId() {
        return role.getNameAndLeaderId(context.selfId());
    }

    /**
     * Get role state.
     *
     * @return role state
     */
    @Nonnull
    RoleState getRoleState() {
        return role.getState();
    }

    @Override
    public void addNodeRoleListener(@Nonnull NodeRoleListener listener) {
        Preconditions.checkNotNull(listener);
        roleListeners.add(listener);
    }

    @Override
    public synchronized void start() {
        if (started) {
            return;
        }
        context.eventBus().register(this);
        context.connector().initialize();

        // load term, votedFor from store and become follower
        NodeStore store = context.store();
        changeToRole(new FollowerNodeRole(store.getTerm(), store.getVotedFor(), null, scheduleElectionTimeout()));
        started = true;
    }

    @Override
    public void appendLog(byte[] commandBytes) {

    }

    @Override
    public GroupConfigChangeTaskReference addNode(NodeEndpoint endpoint) {
        return null;
    }

    @Override
    public GroupConfigChangeTaskReference removeNode(NodeId id) {
        return null;
    }

    @Override
    public void stop() throws InterruptedException {

    }

    private ElectionTimeout scheduleElectionTimeout() {
        return null;
    }

    private void changeToRole(AbstractNodeRole role) {

    }


}
