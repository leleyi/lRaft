package org.les.core.rpc.nio;

import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelHandlerContext;
import org.les.core.node.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FromRemoteHandler extends AbstractHandler {

    private static final Logger logger = LoggerFactory.getLogger(FromRemoteHandler.class);
    private final InboundChannelGroup channelGroup;

    FromRemoteHandler(EventBus eventBus, InboundChannelGroup channelGroup) {
        super(eventBus);
        this.channelGroup = channelGroup;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 如果消息 是一个NodeId.
        if (msg instanceof NodeId) {
            remoteId = (NodeId) msg;
            NioChannel nioChannel = new NioChannel(ctx.channel());
            channel = nioChannel;
            channelGroup.add(remoteId, nioChannel);
            return;
        }

        logger.debug("receive {} from {}", msg, remoteId);
        // read from remote
        System.out.println("from remote message: " + msg);
        super.channelRead(ctx, msg);
    }

}
