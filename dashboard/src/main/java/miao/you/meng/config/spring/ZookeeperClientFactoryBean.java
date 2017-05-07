package miao.you.meng.config.spring;

import miao.you.meng.commons.lang.LangUtils;
import miao.you.meng.config.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by miaoyoumeng on 2017/5/6.
 */
public class ZookeeperClientFactoryBean implements FactoryBean<CuratorFramework>, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperClientFactoryBean.class);

    private String zookeeperCluster;

    private int retryDelay;

    private int retryTimes;

    private CuratorFramework client;

    public void setZookeeperCluster(String zookeeperCluster) {
        this.zookeeperCluster = zookeeperCluster;
    }

    public void setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    @Override
    public void destroy() throws Exception {
        if (client != null) {
            this.client.close();
        }
    }

    @Override
    public CuratorFramework getObject() throws Exception {
        return this.client;
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //zk 集群地址
        if (StringUtils.isBlank(zookeeperCluster)) {
            throw new RuntimeException("zk 'cluster' config is null!");
        }


        //重试策略
        if (retryDelay < 0){
            retryDelay = Constants.DEFAULT_RETRY_DELAY;
        }
        //重试次数
        if (retryTimes < 0){
            retryTimes = Constants.DEFAULT_RETRY_TIMES;
        }

        logger.info("cluster [{}], retryDelay [{}], retryTimes [{}] ", zookeeperCluster, retryDelay, retryTimes);

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(retryDelay, retryTimes);

        this.client = CuratorFrameworkFactory.newClient(zookeeperCluster, new ExponentialBackoffRetry(1000, 3));
        this.client.start();
    }
}
