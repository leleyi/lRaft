package org.les.kv.server;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.les.kv.MessageConstants;
import org.les.kv.Protos;
import org.les.kv.message.*;

import java.io.IOException;


public class Encoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        if (msg instanceof Protos.Success) {
            this.writeMessage(MessageConstants.MSG_TYPE_SUCCESS, Protos.Success.newBuilder().build(), out);
        } else if (msg instanceof Failure) {
            Failure failure = (Failure) msg;
            Protos.Failure protoFailure = Protos.Failure
                    .newBuilder()
                    .setErrorCode(failure.getErrorCode())
                    .setMessage(failure.getMessage())
                    .build();
            this.writeMessage(MessageConstants.MSG_TYPE_FAILURE, protoFailure, out);
        } else if (msg instanceof Redirect) {
            Redirect redirect = (Redirect) msg;
            Protos.Redirect protoRedirect = Protos.Redirect
                    .newBuilder()
                    .setLeaderId(redirect.getLeaderId())
                    .build();
            this.writeMessage(MessageConstants.MSG_TYPE_REDIRECT, protoRedirect, out);
        } else if (msg instanceof SetCommand) {
            SetCommand setCommand = (SetCommand) msg;
            Protos.SetCommand protoSetCommand = Protos.SetCommand
                    .newBuilder()
                    .setKey(setCommand.getKey())
                    .setValue(ByteString.copyFrom(setCommand.getValue()))
                    .build();
            this.writeMessage(MessageConstants.MSG_TYPE_SET_COMMAND, protoSetCommand, out);
        } else if (msg instanceof GetCommand) {
            GetCommand getCommand = (GetCommand) msg;
            Protos.GetCommand protoGetCommand = Protos.GetCommand
                    .newBuilder()
                    .setKey(getCommand.getKey())
                    .build();
            this.writeMessage(MessageConstants.MSG_TYPE_GET_COMMAND, protoGetCommand, out);
        } else if (msg instanceof Protos.GetCommandResponse) {
            GetCommandResponse response = (GetCommandResponse) msg;
            byte[] value = response.getValue();
            Protos.GetCommandResponse protoResponse = Protos.GetCommandResponse.newBuilder()
                    .setFound(response.isFound())
                    .setValue(value != null ? ByteString.copyFrom(value) : ByteString.EMPTY).build();
            this.writeMessage(MessageConstants.MSG_TYPE_GET_COMMAND_RESPONSE, protoResponse, out);
        }
    }

    private void writeMessage(int messageType, MessageLite message, ByteBuf out) throws IOException {
        // 4字节 + 4字节 + len(bytes)
        out.writeInt(messageType);
        byte[] bytes = message.toByteArray();
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
