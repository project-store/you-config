package miao.you.meng.config.factory;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by miaoyoumeng on 2017/4/13.
 */
public class CuratorClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(CuratorClientFactory.class);

    private static final String DEFAULT_ZOOKEEPER_CLUSTER_ADDRESS = "127.0.0.1:2181";

    static String zookeeperConfigAddress ;

    private CuratorClientFactory() {}

    static {
        InputStream in = CuratorClientFactory.class.getClassLoader().getResourceAsStream("zookeeper-config.properties");
        Properties properties = new Properties();
        try {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("init zk config failed!", e);
        }
        if (zookeeperConfigAddress == null) {
            zookeeperConfigAddress = properties.getProperty("zookeeper.cluster", DEFAULT_ZOOKEEPER_CLUSTER_ADDRESS);
        }
        String application = properties.getProperty("application");

        logger.warn("zookeeper address: {}, application: {}", zookeeperConfigAddress, application);
    }

    public static CuratorFramework getInstance(){
        CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeperConfigAddress, new ExponentialBackoffRetry(1000, 3));
        client.start();
        return client;
    }
}
