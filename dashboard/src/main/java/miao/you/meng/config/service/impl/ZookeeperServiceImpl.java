package miao.you.meng.config.service.impl;

import miao.you.meng.config.constants.Constants;
import miao.you.meng.config.constants.ResponseCode;
import miao.you.meng.config.enumeration.MySQLRole;
import miao.you.meng.config.service.IZookeeperService;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by miaoyoumeng on 2017/5/6.
 */
public class ZookeeperServiceImpl implements IZookeeperService {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceImpl.class);

    @Autowired
    private CuratorFramework client;

    public int createAppConfigNode(final String appName, final String configKey, final String configValue) {
        logger.info("appName [{}], configKey [{}], configValue {}", appName, configKey, configValue);
        Stat stat = null;
        final String updateNodePath = Constants.APP_CONFIG_ROOT + Constants.BACK_SLANT + appName + Constants.BACK_SLANT + configKey;
        try{
            if ((stat = client.checkExists().forPath(updateNodePath)) == null){
                logger.info("create zookeeper node path [{}]", updateNodePath);
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(updateNodePath,
                        configValue.getBytes(CharEncoding.UTF_8));
            } else {
                logger.info("update zookeeper node path [{}]", updateNodePath);
                client.setData().forPath(updateNodePath, configValue.getBytes(CharEncoding.UTF_8));
            }
        } catch (Exception e){
            logger.error("zookeeper operator fail, error {}", e);
            return ResponseCode.FAIL;
        }
        return ResponseCode.SUCCESS;
    }


    @Override
    public String getAppConfigNode(final String appName, final String configKey) {
        String nodePath = Constants.APP_CONFIG_ROOT + Constants.BACK_SLANT + appName + Constants.BACK_SLANT + configKey;
        logger.info("appName [{}], configKey [{}]", appName, configKey);
        return  this.readNode(nodePath);
    }

    @Override
    public int createNode(String zkPath, String value) {
        logger.info("zkPath [{}], value : {}", zkPath, value);
        Stat stat = null;
        try{
            if ((stat = client.checkExists().forPath(zkPath)) == null){
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zkPath, value.getBytes(CharEncoding.UTF_8));
            }
        } catch (Exception e){
            e.printStackTrace();
            return ResponseCode.FAIL;
        }
        return ResponseCode.SUCCESS;
    }

    @Override
    public int deleteNode(String nodePath) {
        Stat stat = null;
        try{
            if ((stat = client.checkExists().forPath(nodePath)) != null){
                client.delete().deletingChildrenIfNeeded().forPath(nodePath);
            } else {
                return 1;
            }
        } catch (Exception e){
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    /**
     * 从zookeepre中读取数据值
     * @param nodePath
     * @return
     */
    private String readNode(String nodePath) {
        Stat stat = null;
        try{
            if ((stat = client.checkExists().forPath(nodePath)) != null){
                byte[] bytes = client.getData().forPath(nodePath);
                String value = new String(bytes);
                logger.info("nodePath [{} = {} ]", nodePath, value);
                return value;
            } else {
                logger.warn("value is not exists, nodePath  [{}]", nodePath);
                return null;
            }
        } catch (Exception e){
            logger.error("read value faile nodePath [{}], Exception: {}", nodePath, e);
            return null;
        }
    }

    public int refreshMasterConfig(String appName, String key, String value){
        return refreshDataSourceConfig(appName, MySQLRole.MASTER.toString(), key, value );
    }
    public int refreshSlaveConfig(String appName, int index, String key, String value){
        return refreshDataSourceConfig(appName, MySQLRole.SLAVE.toString() + index, key, value );
    }

    private int refreshDataSourceConfig(String appName, String type, String key, String value) {
        final String zookeeperPath = Constants.DATASOURCE_CONFIG_ROOT +
                Constants.BACK_SLANT + appName +
                Constants.BACK_SLANT + type +
                Constants.BACK_SLANT + Constants.DATASOURCE_CONFIG_NODE_NAME +
                Constants.BACK_SLANT + key;
        Stat stat = null;
        try{
            if ((stat = client.checkExists().forPath(zookeeperPath)) != null){
                client.setData().forPath(zookeeperPath, value.getBytes(CharEncoding.UTF_8));
            } else {
                createNode(zookeeperPath, value);
            }
        } catch (Exception e){
            e.printStackTrace();
            return ResponseCode.FAIL;
        }
        return ResponseCode.SUCCESS;
    }

    @Override
    public String getNodePath(String... paths) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.BACK_SLANT + Constants.APP_CONFIG_ROOT);
        for (String path : paths){
            sb.append(Constants.BACK_SLANT + path);
        }
        return sb.toString();
    }

    /**
     * 从zookeeper中获取对应key的配置值
     * @param appName
     * @param configKey
     * @return
     */
    @Override
    public String getMasterConfigKey(String appName, String configKey) {

        logger.info("appName [{}], configKey [{}]", appName, configKey);
        Stat stat = null;
        final String zookeeperPath = Constants.DATASOURCE_CONFIG_ROOT +
                Constants.BACK_SLANT + appName +
                Constants.BACK_SLANT + MySQLRole.MASTER.toString() +
                Constants.BACK_SLANT + Constants.DATASOURCE_CONFIG_NODE_NAME +
                Constants.BACK_SLANT + configKey;

        logger.info("zookeeperPath [{}]", zookeeperPath);
        return this.readNode(zookeeperPath);
    }

    public String getSlaveConfigKey(String appName, int indexId, String configKey){
        logger.info("appName [{}], configKey [{}]", appName, configKey);
        Stat stat = null;
        final String zookeeperPath = Constants.DATASOURCE_CONFIG_ROOT +
                Constants.BACK_SLANT + appName +
                Constants.BACK_SLANT + MySQLRole.SLAVE.toString() + indexId +
                Constants.BACK_SLANT + Constants.DATASOURCE_CONFIG_NODE_NAME +
                Constants.BACK_SLANT + configKey;

        logger.info("zookeeperPath [{}]", zookeeperPath);
        return this.readNode(zookeeperPath);
    }


    public int createDataSourceConfigNode(final String appName, final String configKey, final String configValue) {
        logger.info("appName [{}], configKey [{}], configValue {}", appName, configKey, configValue);
        Stat stat = null;
        final String nodePath = Constants.APP_CONFIG_ROOT + Constants.BACK_SLANT + appName + Constants.BACK_SLANT + configKey;
        try{
            if ((stat = client.checkExists().forPath(nodePath)) == null){
                logger.info("create zookeeper node path [{}]", nodePath);
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(nodePath,
                        configValue.getBytes(CharEncoding.UTF_8));
            } else {
                logger.info("update zookeeper node path [{}]", nodePath);
                client.setData().forPath(nodePath, configValue.getBytes(CharEncoding.UTF_8));
            }
        } catch (Exception e){
            logger.error("zookeeper operator fail, error {}", e);
            return ResponseCode.FAIL;
        }
        return ResponseCode.SUCCESS;
    }

    /**
     * 更新所有配置在zookeeper中
     * @param appName
     * @param configMap
     * @return
     */
    @Override
    public int refreshMasterConfig(String appName, Map<String, String> configMap) {
        for (String key : configMap.keySet()) {
            String value = configMap.get(key);
            value = StringUtils.trimToEmpty(value);
            int result = this.refreshMasterConfig(appName, key, value);
            if (ResponseCode.FAIL == result) {
                return ResponseCode.FAIL;
            }
        }
        return ResponseCode.SUCCESS;
    }

    @Override
    public int refreshSlaveConfig(String appName, int index, Map<String, String> configMap) {
        for (String key : configMap.keySet()) {
            String value = configMap.get(key);
            value = StringUtils.trimToEmpty(value);
            int result = this.refreshSlaveConfig(appName, index, key, value);
            if (ResponseCode.FAIL == result) {
                return ResponseCode.FAIL;
            }
        }
        return ResponseCode.SUCCESS;
    }

    @Override
    public int refreshNode(String nodePath, String value) {
        return 0;
    }
}
