package rpc.nio;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelHandlerContext;
import node.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ToRemoteHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(ToRemoteHandler.class);
    private final NodeId selfNodeId;
    // 从 其他 node 读入请求之后.
    ToRemoteHandler(EventBus eventBus, NodeId remoteId, NodeId selfNodeId) {
        super(eventBus);
        this.remoteId = remoteId;
        this.selfNodeId = selfNodeId;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.write(selfNodeId);
        channel = new NioChannel(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("receive {} from {}", msg, remoteId);
        // 读到消息.
        super.channelRead(ctx, msg);
    }

}
