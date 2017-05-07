package miao.you.meng.config.constants;

/**
 * Created by Administrator on 2017/4/14.
 *
 * zookeeper 路径属性
 */
public interface Constants {
    String BACK_SLANT = "/";

    String APP_CONFIG_ROOT_NODE_NAME = "AppConfigs";

    String APP_CONFIG_ROOT = BACK_SLANT + APP_CONFIG_ROOT_NODE_NAME;



    String DS_ROOT = "DataSource";

    String FILE_NAME = "zookeeper-config.properties";

    //以下是配置文件key常量
    String ZOOKEEPER_CLUSTER = "zookeeper.cluster";

    String APPLICATION_NAME = "application.name";

    String RETRY_DELAY = "retry.delay";

    String RETRY_TIMES = "retry.times";

    int DEFAULT_RETRY_DELAY = 1000; //默认一秒重试一次

    int DEFAULT_RETRY_TIMES = 3; //默认重试次数
}
