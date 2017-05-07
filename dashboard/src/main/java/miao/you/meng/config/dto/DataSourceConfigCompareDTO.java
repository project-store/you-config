package miao.you.meng.config.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/3.
 *
 * 数据源详细配置的dto
 */
public class DataSourceConfigCompareDTO implements Serializable {
    private static final long serialVersionUID = -111779201904261538L;

    private String key;

    private String zooValue;

    private String mysqlValue;

    private int compare;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getZooValue() {
        return zooValue;
    }

    public void setZooValue(String zooValue) {
        this.zooValue = zooValue;
    }

    public String getMysqlValue() {
        return mysqlValue;
    }

    public void setMysqlValue(String mysqlValue) {
        this.mysqlValue = mysqlValue;
    }

    public int getCompare() {
        return compare;
    }

    public void setCompare(int compare) {
        this.compare = compare;
    }
}
