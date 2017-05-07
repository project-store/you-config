package miao.you.meng.config.service.impl;

import miao.you.meng.config.constants.Constants;
import miao.you.meng.config.constants.ResponseCode;
import miao.you.meng.config.service.IZookeeperService;
import org.apache.commons.lang3.CharEncoding;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
    public int createNode(String nodePath, String value) {
        Stat stat = null;
        try{
            if ((stat = client.checkExists().forPath(nodePath)) == null){
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(nodePath, value.getBytes());
            }
        } catch (Exception e){
            e.printStackTrace();
            return 1;
        }
        return 0;
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

    @Override
    public String readNode(String nodePath) {
        logger.info("nodePath [{}]", nodePath);
        Stat stat = null;
        try{
            if ((stat = client.checkExists().forPath(nodePath)) != null){
                byte[] bytes = client.getData().forPath(nodePath);
                String value = new String(bytes);
                logger.info("nodePath [{}], value {} ", nodePath, value);
                return value;
            } else {
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int reflushNode(String nodePath, String value) {
        Stat stat = null;
        try{
            if ((stat = client.checkExists().forPath(nodePath)) != null){
                client.setData().forPath(nodePath, value.getBytes());
            } else {
                createNode(nodePath, value);
            }
        } catch (Exception e){
            e.printStackTrace();
            return 1;
        }
        return 0;
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

    @Override
    public String getDSPath(String... paths) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.BACK_SLANT + Constants.DS_ROOT);
        for (String path : paths){
            sb.append(Constants.BACK_SLANT + path);
        }
        return sb.toString();
    }


}
