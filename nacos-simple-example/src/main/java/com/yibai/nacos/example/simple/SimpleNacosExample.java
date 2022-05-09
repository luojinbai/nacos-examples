package com.yibai.nacos.example.simple;

import ch.qos.logback.core.joran.spi.JoranException;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author yb
 * @date 2022/5/9 16:37
 */
public class SimpleNacosExample {

    private final static String SERVICE_ADDRESS = "127.0.0.1";
    //    private final static String SERVICE_ADDRESS = "8.135.109.39";
    private final static String DATA_ID = "example";
    private final static String GROUP = "DEFAULT_GROUP";
    private final static String NAMESPACE = "public";

    private static Properties properties = new Properties();

    static {
        try {
            // 手动加载日志配置文件
            LogBackConfigLoader.load("logback-spring.xml");
//            org.slf4j.Logger logger = LoggerFactory.getLogger("com.yibai");
//            logger.debug("Hello");

            // 公共配置
            properties.put(PropertyKeyConst.SERVER_ADDR, SERVICE_ADDRESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NacosException {
        // 获取配置
//        publishConfig();
//        getConfig();
        addListener();
    }

    private static void publishConfig() throws NacosException {
        try {
            // 初始化配置服务，控制台通过示例代码自动获取下面参数
            ConfigService configService = NacosFactory.createConfigService(properties);
            boolean isPublishOk = configService.publishConfig(DATA_ID, GROUP, "name=yibai");
            System.out.println(isPublishOk);
        } catch (NacosException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取配置
     *
     * @throws NacosException 异常
     */
    private static String getConfig() throws NacosException {
        ConfigService configService = NacosFactory.createConfigService(properties);
        String content = configService.getConfig(DATA_ID, GROUP, 5000);
//        System.out.println(content);
        return content;
    }

    private static void addListener() throws NacosException {
        ConfigService configService = NacosFactory.createConfigService(properties);

        // 修改前配置
        System.out.println("修改前配置: " + getConfig());

        configService.addListener(DATA_ID, GROUP, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                System.out.println("收到配置变更的通知: " + configInfo);
            }

            @Override
            public Executor getExecutor() {
                System.out.println("配置监听器的 getExecutor 方法被调用");
                return null;
            }
        });

        // 测试让主线程不退出，因为订阅配置是守护线程，主线程退出守护线程就会退出。 正式代码中无需下面代码
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
