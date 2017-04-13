package miao.you.meng.config.zookeeper;

import com.google.common.base.Preconditions;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监听配置节点变化的listener
 * Created by miaoyoumeng on 2017/4/7.
 */
public class ConfigNodeEventListener implements CuratorListener {

    private static final Logger logger = LoggerFactory.getLogger(ConfigNodeEventListener.class);

    private final ZookeeperConfigApplication configApplication;

    public ConfigNodeEventListener(final ZookeeperConfigApplication configNode) {
        super();
        this.configApplication = Preconditions.checkNotNull(configNode);
    }

    @Override
    public void eventReceived(final CuratorFramework client, final CuratorEvent event) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(event.toString());
        }

        final WatchedEvent watchedEvent = event.getWatchedEvent();
        if (watchedEvent != null) {
            logger.debug("Watched event: {}", watchedEvent);

            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                boolean someChange = false;
                switch (watchedEvent.getType()) {
                    case NodeChildrenChanged:
                        configApplication.loadNode();
                        someChange = true;
                        break;
                    case NodeDataChanged:
                        configApplication.getProperty(watchedEvent.getPath());
                        someChange = true;
                        break;
                    default:
                        break;
                }
            }
        }
    }
}