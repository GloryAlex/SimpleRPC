package com.glory;

import com.glory.server.NettyServer;
import com.glory.test.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan
@PropertySource("classpath:/server.properties")
public class AppConfig {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        NettyServer server = context.getBean(NettyServer.class);
        server.bind();
    }

    /**
     * 服务器能够提供的服务是有限的，不能随意调用
     * @return 服务器所能提供的服务
     */
    @Bean("services")
    Map<String, Object> getServices() {
        Map<String, Object> services = new HashMap<>();
        services.put(TestPlus.class.getName(), new TestPlusImp());
        services.put(TestMinus.class.getName(),new TestMinusImp());
        services.put(TestHello.class.getName(),new TestHelloImp());
        return services;
    }
}
