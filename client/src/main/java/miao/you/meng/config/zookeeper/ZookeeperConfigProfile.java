package miao.you.meng.config.zookeeper;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import miao.you.meng.config.ConfigProfile;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * zookeeper 配置文件对应的对象
 * Created by miaoyoumeng on 2017/4/7.
 */
public class ZookeeperConfigProfile implements ConfigProfile {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfigProfile.class);
    private static final ExponentialBackoffRetry DEFAULT_RETRY_POLICY = new ExponentialBackoffRetry(1000, 3);

    private String version;
    /**
     * zookeeper地址
     */
    private final String connectStr;

    /**
     * 项目配置根目录
     */
    private final String rootNode;

    /**
     * 重试策略
     */
    private final RetryPolicy retryPolicy;

    /**
     * 一致性检查, 主动检查本地数据与zk中心数据的一致性, 防止出现因连接中断而丢失更新消息, 默认开启
     */
    private boolean consistencyCheck = true;

    /**
     * 检查频率, in milliseconds
     */
    private long consistencyCheckRate = 60 * 1000;


    public ZookeeperConfigProfile(final String connectStr, final String rootNode, final boolean openLocalCache) {
        this(connectStr, rootNode, null, openLocalCache, DEFAULT_RETRY_POLICY);
    }

    public ZookeeperConfigProfile(final String connectStr, final String rootNode, final String version) {
        this(connectStr, rootNode, version, false, DEFAULT_RETRY_POLICY);
    }

    public ZookeeperConfigProfile(final String connectStr, final String rootNode, final String version, final boolean openLocalCache,
                                  final RetryPolicy retryPolicy) {
//        super(version);
        this.connectStr = Preconditions.checkNotNull(connectStr);
        this.rootNode = Preconditions.checkNotNull(rootNode);
        this.retryPolicy = Preconditions.checkNotNull(retryPolicy);
    }

    public String getConnectStr() {
        return connectStr;
    }

    public String getRootNode() {
        return rootNode;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    public boolean isConsistencyCheck() {
        return consistencyCheck;
    }

    public long getConsistencyCheckRate() {
        return consistencyCheckRate;
    }

    public String getVersionedRootNode() {
        if (Strings.isNullOrEmpty(version)) {
            return rootNode;
        }
        return ZKPaths.makePath(rootNode, version);
    }

    @Override
    public String getVersion() {
        return version;
    }
}
