package miao.you.meng.config.dto;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * 数据库通用配置
 * Created by miaoyoumeng on 2017/5/7.
 */
public abstract class DataSourceConfigDTO implements Serializable {

    private static final long serialVersionUID = -7685603818170648705L;

    private  Map<String, String> connectionPool = Maps.newConcurrentMap();

    private String config;

    public Map<String, String> getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(Map<String, String> connectionPool) {
        this.connectionPool = connectionPool;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
