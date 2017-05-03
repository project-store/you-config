package miao.you.meng.config.common;

/**
 * Created by miaoyoumeng on 2017/5/3.
 */
public interface Constants {

    String FILE_NAME = "zookeeper-config.properties";

    String DEFAULT_ZOOKEEPER_CLUSTER_ADDRESS = "127.0.0.1:2181";

    String BACK_SLANT = "/";

    String APP_CONFIG_ROOT = BACK_SLANT + "SystemGlobals";

    int DEFAULT_RETRY_DELAY = 1000; //默认一秒重试一次

    int DEFAULT_RETRY_TIMES = 3; //默认重试次数
}
