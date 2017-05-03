package miao.you.meng.config;

import org.apache.curator.framework.CuratorFramework;

import java.util.concurrent.CountDownLatch;

/**
 * Created by miaoyoumeng on 2017/4/11.
 */
public class Main {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        final CuratorFramework client = CuratorClientFactory.getInstance();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
