package com.glory.server;

import com.glory.coder.RpcRequest;
import com.glory.coder.RpcResponse;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
@Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);
    @Resource(name = "services")
    private final Map<String, Object> handlerMap;

    public ServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse response = new RpcResponse();
        RpcRequest request = (RpcRequest) msg;
        response.setId(request.getId());

        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (Throwable throwable) {
            LOGGER.error("invoke error,", throwable);
            response.setError(throwable);
        }

        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("server caught exception", cause);
        ctx.close();
    }

    private Object handle(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        Object serviceBean = handlerMap.get(className);
        Class<?> serviceBeanClass = serviceBean.getClass();
        FastClass fastClass = FastClass.create(serviceBeanClass);
        FastMethod method = fastClass.getMethod(methodName, parameterTypes);
        return method.invoke(serviceBean, parameters);
    }
}
