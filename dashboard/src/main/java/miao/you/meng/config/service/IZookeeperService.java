package miao.you.meng.config.service;

import java.util.Map;

/**
 * Created by miaoyoumeng on 2017/5/6.
 */
public interface IZookeeperService {

    int createAppConfigNode(String appName, String configKey, String configValue);

    String getAppConfigNode(final String appName, final String configKey);

    int createNode(String nodePath, String value);

    int deleteNode(String nodePath);

    int refreshMasterConfig(String appName, String key, String value);

    /**
     * 将所有master节点配置更新到zookeeper
     * @param appName
     * @param configMap
     * @return
     */
    int refreshMasterConfig(String appName, Map<String, String> configMap);


    int refreshSlaveConfig(String appName, int index, String key, String value);

    int refreshSlaveConfig(String appName, int index, Map<String, String> configMap);

    String getNodePath(String... paths);

    int refreshNode(String nodePath, String value);

    /**
     * 获取主库连接池配置
     * @param appName
     * @param configKey
     * @return
     */
    String getMasterConfigKey(String appName, String configKey);

    /**
     * 获取从库库连接池配置
     * @param appName
     * @param indexId   从库标识。从1开始。
     * @param configKey
     * @return
     */
    String getSlaveConfigKey(String appName, int indexId, String configKey);
}
