package miao.you.meng.config.util;

import miao.you.meng.config.constants.Constants;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Created by Administrator on 2017/4/14.
 */
public class ZookeeperUtil {

    /**
     * 启动静态加载zookeeper
     */
    private static CuratorFramework client = null;
    static{
        client = CuratorFrameworkFactory.newClient("10.155.20.164:12181", new RetryNTimes(10, 5000));
        client.start();
    }

    /**
     * 添加zookeeper节点
     */
    public static int createNode(String nodePath, String value){
        CuratorFramework client = getConnection();
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

    /**
     * 删除zookeeper节点
     */
    public static int deleteNode(String nodePath){
        CuratorFramework client = getConnection();
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

    /**
     * 读取zookeeper具体值
     */
    public static String readNode(String nodePath){
        CuratorFramework client = getConnection();
        Stat stat = null;
        try{
            if ((stat = client.checkExists().forPath(nodePath)) != null){
                byte[] bytes = client.getData().forPath(nodePath);
                return new String(bytes);
            } else {
                return null;
            }
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新zookeeper的节点值
     */
    public static int reflushNode(String nodePath, String value){
        CuratorFramework client = getConnection();
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

    /**
     * 静态启动zookeeper
     */
    public static CuratorFramework getConnection(){
        return client;
    }

    /**
     * 获取非数据源的路径
     */
    public static String getNodePath(String... paths){
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.BACK_SLANT + Constants.APP_CONFIG_ROOT);
        for (String path : paths){
            sb.append(Constants.BACK_SLANT + path);
        }
        return sb.toString();
    }

    /**
     * 获取数据源路径
     */
    public static String getDSPath(String... paths){
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.BACK_SLANT + Constants.DS_ROOT);
        for (String path : paths){
            sb.append(Constants.BACK_SLANT + path);
        }
        return sb.toString();
    }

}
