package miao.you.meng.config;

import com.google.common.base.Preconditions;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Created by miaoyoumeng on 2017/4/12.
 */
public class ZookeeperConfigProperties {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfigProperties.class);

    private static final String DEFAULT_ZOOKEEPER_CLUSTER_ADDRESS = "127.0.0.1:2181";

    private static CuratorFramework client;
    static {
        String zookeeperConfigAddress = DEFAULT_ZOOKEEPER_CLUSTER_ADDRESS;
        try {
            InputStream in = ZookeeperConfigProperties.class.getClassLoader().getResourceAsStream("zookeeper-config.properties");
            PropertiesConfiguration config = new PropertiesConfiguration("zookeeper-config.properties");
            zookeeperConfigAddress = config.getString("zookeeper.cluster", DEFAULT_ZOOKEEPER_CLUSTER_ADDRESS);
            String application = config.getString("application");

            logger.warn("zookeeper address: {}, application: {}", zookeeperConfigAddress, application);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        client = CuratorFrameworkFactory.newClient(zookeeperConfigAddress,  new ExponentialBackoffRetry(1000, 3));
        client.start();
    }

    protected static CuratorFramework getCuratorFrameworkClient(){
        return client;
    }



}
