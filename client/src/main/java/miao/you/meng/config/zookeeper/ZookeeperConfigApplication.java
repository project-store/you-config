package miao.you.meng.config.zookeeper;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

/**
 * 使用配置的应用名称
 * Created by miaoyoumeng on 2017/4/7.
 */
public class ZookeeperConfigApplication implements Configuration {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfigApplication.class);

    private ZookeeperConfigProfile configProfile;

    private static final Map<String, Object> configs;

    private ScheduledExecutorService scheduler;

    static {
        configs = Maps.newHashMap();
    }

    public ZookeeperConfigApplication( ZookeeperConfigProfile configProfile) {
        this.configProfile = configProfile;
        initApplicationConfigs();
    }

    /**
     * 节点名字
     */
    private String application;

    private CuratorFramework client;

    private ConfigNodeEventListener listener = new ConfigNodeEventListener(this);


    private void initApplicationConfigs() {
        client = CuratorFrameworkFactory.newClient(configProfile.getConnectStr(), configProfile.getRetryPolicy());
        client.start();
        client.getCuratorListenable().addListener(listener);

        logger.debug("Loading properties for application name: {} ", application);
        loadNode();

        // Consistency check
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setDaemon(true);
                return thread;
            }
        };
        if (configProfile.isConsistencyCheck()) {
            scheduler = Executors.newScheduledThreadPool(1, threadFactory);
            scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    logger.trace("Do consistency check for application: {}", application);
                    loadNode();
                }
            }, 60000L, configProfile.getConsistencyCheckRate(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 加载配置配置节点
     */
    void loadNode() {
        final String nodePath = ZKPaths.makePath(configProfile.getVersionedRootNode(), application);

        GetChildrenBuilder childrenBuilder = client.getChildren();

        try {
            List<String> children = childrenBuilder.watched().forPath(nodePath);
            if (children != null) {
                for (String child : children) {
                    Pair<String, String> keyValue = loadKey(ZKPaths.makePath(nodePath, child));
                    if (keyValue != null) {
                        configs.put(keyValue.getKey(), keyValue.getValue());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("error : {}", e);
            throw Throwables.propagate(e);
        }
    }

    private Pair<String, String> loadKey(final String nodePath) throws Exception {
        String nodeName = ZKPaths.getNodeFromPath(nodePath);
        GetDataBuilder data = client.getData();
        String value = new String(data.watched().forPath(nodePath), Charsets.UTF_8);
        return new ImmutablePair<String, String>(nodeName, value);
    }

    @PreDestroy
    public void close() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
        if (client != null) {
            client.getCuratorListenable().removeListener(listener);
            client.close();
        }

    }

    @Override
    public Configuration subset(String prefix) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(String key) {
        return this.configs.containsKey(key);
    }

    @Override
    public void addProperty(String key, Object value) {
        this.setProperty(key, value);
    }

    @Override
    public void setProperty(String key, Object value) {
        configs.put(key, value);
    }

    @Override
    public void clearProperty(String key) {
        configs.remove(key);
    }

    @Override
    public void clear() {
        configs.clear();;
    }

    @Override
    public Object getProperty(String key) {
        return configs.get(key);
    }

    @Override
    public Iterator<String> getKeys(String prefix) {
        return null;
    }

    @Override
    public Iterator<String> getKeys() {
        return null;
    }

    @Override
    public Properties getProperties(String key) {
        return null;
    }

    @Override
    public boolean getBoolean(String key) {
        Object object = this.getProperty(key);
        if(object instanceof Boolean){
            return Boolean.valueOf(object.toString());
        }
        return false;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        if (this.containsKey(key)){
            return getBoolean(key);
        } else {
            return defaultValue;
        }
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        return null;
    }

    @Override
    public byte getByte(String key) {
        return 0;
    }

    @Override
    public byte getByte(String key, byte defaultValue) {
        return 0;
    }

    @Override
    public Byte getByte(String key, Byte defaultValue) {
        return null;
    }

    @Override
    public double getDouble(String key) {
        return 0;
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return 0;
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return null;
    }

    @Override
    public float getFloat(String key) {
        return 0;
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return 0;
    }

    @Override
    public Float getFloat(String key, Float defaultValue) {
        return null;
    }

    @Override
    public int getInt(String key) {
        return 0;
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return 0;
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        return null;
    }

    @Override
    public long getLong(String key) {
        return 0;
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return 0;
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return null;
    }

    @Override
    public short getShort(String key) {
        return 0;
    }

    @Override
    public short getShort(String key, short defaultValue) {
        return 0;
    }

    @Override
    public Short getShort(String key, Short defaultValue) {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String key) {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return null;
    }

    @Override
    public BigInteger getBigInteger(String key) {
        return null;
    }

    @Override
    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return null;
    }

    @Override
    public String getString(String key) {
        return null;
    }

    @Override
    public String getString(String key, String defaultValue) {
        return null;
    }

    @Override
    public String[] getStringArray(String key) {
        return new String[0];
    }

    @Override
    public List<Object> getList(String key) {
        return null;
    }

    @Override
    public List<Object> getList(String key, List<Object> defaultValue) {
        return null;
    }
}
