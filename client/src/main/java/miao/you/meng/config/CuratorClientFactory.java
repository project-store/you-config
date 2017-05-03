package miao.you.meng.config;

import com.google.common.collect.Lists;
import miao.you.meng.commons.lang.LangUtils;
import miao.you.meng.config.common.Constants;
import miao.you.meng.config.common.ZooKeeperConfig;
import miao.you.meng.config.exception.ConfigException;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static miao.you.meng.config.common.Constants.*;
import static miao.you.meng.config.common.ZooKeeperConfig.*;

/**
 * Created by miaoyoumeng on 2017/4/13.
 */
public class CuratorClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(CuratorClientFactory.class);

    private static String zookeeperCluster;

    private static String appName;

    private static String[] dependAppNames;

    private static RetryPolicy retryPolicy;

    private static final Properties properties = new Properties();

    private static final YouStandardConfig config = new YouStandardConfig();

    private static CuratorFramework client;


    private CuratorClientFactory() {}

    static {
        InputStream in = CuratorClientFactory.class.getClassLoader().getResourceAsStream(FILE_NAME);
        try {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("init zk config failed!", e);
        }

    }

    public static CuratorFramework getInstance(){
        if (client != null) {
            return client;
        }
        synchronized (CuratorClientFactory.class) {
            if (client != null) {
                return client;
            }
            //zk 集群地址
            zookeeperCluster = properties.getProperty(ZooKeeperConfig.ZOOKEEPER_CLUSTER);
            if (StringUtils.isBlank(zookeeperCluster)) {
                throw new ConfigException("zk 'cluster' config is null!");
            }

            //应用名字
            appName = properties.getProperty(APPLICATION_NAME);
            if (StringUtils.isBlank(appName)) {
                throw new ConfigException("zk 'application.name' config is null!");
            }

            //依赖的节点s
            String dependAppNameStr = properties.getProperty(PARENT_APPLICATION_NAMES);
            if (StringUtils.isNotBlank(dependAppNameStr)) {
                dependAppNames = dependAppNameStr.split(",");
            }

            //重试策略
            String retryDelayKey = properties.getProperty(RETRY_DELAY);
            int retryDelay = LangUtils.parseInt(retryDelayKey, DEFAULT_RETRY_DELAY);

            //重试次数
            String retryTimesKey = properties.getProperty(RETRY_TIMES);
            int retryTimes = LangUtils.parseInt(retryTimesKey, DEFAULT_RETRY_TIMES);


            logger.info("cluster [{}], appName [{}], dependAppName [{}], retryDelay [{}], retryTimes [{}] ",
                    zookeeperCluster, appName, dependAppNameStr, retryDelay, retryTimes);

            retryPolicy = new ExponentialBackoffRetry(retryDelay, retryTimes);

            logger.warn("zookeeper address: {}, application: {}", zookeeperCluster, ZooKeeperConfig.APPLICATION_NAME);
            client = CuratorFrameworkFactory.newClient(zookeeperCluster, new ExponentialBackoffRetry(1000, 3));
            client.start();
            addListener();
        }
        return client;
    }

    private static void addListener(){
        final String listenerPath = Constants.APP_CONFIG_ROOT + Constants.BACK_SLANT + appName;
        PathChildrenCache childrenCache = new PathChildrenCache(client, listenerPath, true);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            childrenCache.getListenable().addListener(
                new PathChildrenCacheListener(){
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                        logger.debug("-----开始进行事件分析:-----");
                        ChildData data = event.getData();
                        if (data != null) {
                            String value = new String(data.getData());
                            String key = StringUtils.difference(listenerPath + Constants.BACK_SLANT, data.getPath());
                            switch (event.getType()) {
                                case CHILD_ADDED:
                                    logger.debug("CHILD_ADDED: path:[{}], key:[{}], value:[{}]", data.getPath(), key, value);
                                    config.setProperty(key, value);
                                    break;
                                case CHILD_REMOVED:
                                    logger.debug("CHILD_REMOVED: path:[{}], key:[{}], value:[{}]", data.getPath(), key, value);
                                    config.removeProperty(key);
                                    break;
                                case CHILD_UPDATED:
                                    logger.debug("CHILD_UPDATED: path:[{}], key:[{}], value:[{}]", data.getPath(), key, value);
                                    config.setProperty(key, value);
                                    break;
                                default:
                                    break;
                            }

                        }
                    }
                }, pool
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static boolean isExist(String path) throws Exception {
        Stat stat = CuratorClientFactory.getInstance().checkExists().forPath(path);
        if(stat != null) {
            return true;
        }
        return false;
    }

    /**
     * 从zookeeper中加载配置
     *
     * @param application
     */
    private static void loadZookeeperAppConfig(String application){
        try{
            String appPath = Constants.BACK_SLANT + Constants.APP_CONFIG_ROOT + Constants.BACK_SLANT + application;
            if (!isExist(appPath)) {
                return;
            }

            GetChildrenBuilder childrenBuilder = CuratorClientFactory.getInstance().getChildren();
            List<String> keys = childrenBuilder.forPath(appPath);

            if (keys == null) {
                keys = Lists.newArrayList();
            }

            for (String key : keys) {
                byte[] data = CuratorClientFactory.getInstance().getData().forPath(appPath + Constants.BACK_SLANT + key);
                String value = new String(data);
                config.setProperty(key, value);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
