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

    public Map<String, String> getConnectionPool() {
        return connectionPool;
    }

    public void setConfig(String key, String value) {
        this.connectionPool.put(key, value);
    }

    public abstract String getType();

    public abstract String getDisplayName();


}
