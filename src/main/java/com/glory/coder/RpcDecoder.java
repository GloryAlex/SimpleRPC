package com.glory.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> genericsClass;

    public RpcDecoder(Class<?> genericsClass) {
        this.genericsClass = genericsClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readableBytes();
        byte[] bytes = new byte[length];

        byteBuf.readBytes(bytes,0,length);
        Object object = Serializers.deserialize(bytes,genericsClass);
        list.add(object);
    }
}
