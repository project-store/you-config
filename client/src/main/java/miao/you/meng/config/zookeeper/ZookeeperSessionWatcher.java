package miao.you.meng.config.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 *
 * Zookeeper Watcher，监听zookeeper 节点变化，重新初始化数据
 * Created by miaoyoumeng on 2017/4/7.
 */
class ZookeeperSessionWatcher implements Watcher {

    public void process(WatchedEvent event) {
        // 连接上zookeeper节点
        if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {

        } else if (event.getState() == Event.KeeperState.Expired) {
            // zookeeper节点连接失效

        }
    }
}