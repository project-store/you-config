package miao.you.meng.config.service;

/**
 * Created by miaoyoumeng on 2017/5/6.
 */
public interface IZookeeperService {

    int createAppConfigNode(String appName, String configKey, String configValue);

    String getAppConfigNode(final String appName, final String configKey);

    int createNode(String nodePath, String value);

    int deleteNode(String nodePath);


    String readNode(String nodePath);

    int reflushNode(String nodePath, String value);

    String getNodePath(String... paths);

    String getDSPath(String... paths);
}
