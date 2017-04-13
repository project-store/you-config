package miao.you.meng.config.client;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 配置中心客户端,用于与zookeeper交互
 * Created by miaoyoumeng on 2017/4/7.
 */
public class ConfigClient {

    private static final int ZOOKEEPER_SESSION_TIME = 3000;

    private static final Logger logger = LoggerFactory.getLogger(ConfigClient.class);

    private ConfigClient() {}

    private static ZooKeeper zookeeper = null;
    private static CountDownLatch latch;
    private static String zookeeperCluster = null;

    public static ZooKeeper getZooKeeper() {
        return zookeeper;
    }



    private static void registerDSWatcher() {

    }

    public static void close() {
        if (zookeeper != null) {
            try {
                zookeeper.close();
                zookeeper = null;
            } catch (InterruptedException e) {
                // ignore exception
            }
        }
    }


}
