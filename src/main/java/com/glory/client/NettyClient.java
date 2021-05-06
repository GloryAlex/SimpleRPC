package com.glory.client;

import com.glory.coder.RpcDecoder;
import com.glory.coder.RpcEncoder;
import com.glory.coder.RpcRequest;
import com.glory.coder.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

public class NettyClient {
    private final String address;
    private final int port;
    private final Logger LOGGER;
    private final CountDownLatch latch;

    public NettyClient(String address, int port) {
        this.address = address;
        this.port = port;
        LOGGER = LoggerFactory.getLogger(this.getClass());
        latch = new CountDownLatch(1);
    }

    public RpcResponse send(RpcRequest request){
        EventLoopGroup group = new NioEventLoopGroup();
        ClientHandler handler = new ClientHandler(request, latch);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1024)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                                    .addLast(new RpcDecoder(RpcResponse.class))
                                    .addLast(new LengthFieldPrepender(2))
                                    .addLast(new RpcEncoder(RpcRequest.class))
                                    .addLast(handler);
                        }
                    });
            ChannelFuture future = bootstrap.connect(address, port).sync();
            latch.await();
            if(handler.getResponse()!=null)future.channel().close();
        }catch (InterruptedException e){
            LOGGER.error("Failed to connect {}:{}",address,port);
            e.printStackTrace();
        }
        finally {
            group.shutdownGracefully();
        }

        return handler.getResponse();
    }
}
