package miao.you.meng.config;

import com.google.common.collect.Lists;
import miao.you.meng.config.factory.CuratorClientFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * Created by miaoyoumeng on 2017/4/12.
 */
public class ZookeeperConfigProperties {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfigProperties.class);

    private static final String CONFIG_ROOT="AppConfig";

    private static final String BACK_SLANT="/";


    public static boolean isExist(String path) throws Exception {
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
            String appPath = BACK_SLANT + CONFIG_ROOT + BACK_SLANT + application;
            if (!isExist(appPath)) {
                return;
            }
            YouStandardConfig config = new YouStandardConfig();
            GetChildrenBuilder childrenBuilder = CuratorClientFactory.getInstance().getChildren();
            List<String> keys = childrenBuilder.forPath(appPath);

            if (keys == null) {
                keys = Lists.newArrayList();
            }

            for (String key : keys) {
                byte[] data = CuratorClientFactory.getInstance().getData().forPath(appPath + BACK_SLANT + key);
                String value = new String(data);
                config.setProperty(key, value);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
