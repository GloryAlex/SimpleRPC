package com.glory.client;

import com.glory.coder.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.CountDownLatch;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    private final RpcRequest request;
    private RpcResponse response;
    private final CountDownLatch latch;

    public ClientHandler(RpcRequest request, CountDownLatch latch) {
        this.request = request;
        this.latch = latch;
    }

    public RpcResponse getResponse() {
        return response;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response  = (RpcResponse) msg;
        latch.countDown();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.getCause().printStackTrace();
        ctx.close();
    }
}
