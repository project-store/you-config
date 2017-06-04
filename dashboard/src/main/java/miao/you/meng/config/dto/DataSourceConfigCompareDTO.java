package miao.you.meng.config.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/3.
 *
 * 数据源详细配置的dto
 */
public class DataSourceConfigCompareDTO implements Serializable {
    private static final long serialVersionUID = -111779201904261538L;

    private String key;

    private String zkValue;

    private String mysqlValue;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getZkValue() {
        return zkValue;
    }

    public void setZkValue(String zkValue) {
        this.zkValue = zkValue;
    }

    public String getMysqlValue() {
        return mysqlValue;
    }

    public void setMysqlValue(String mysqlValue) {
        this.mysqlValue = mysqlValue;
    }

    /**
     * zookeeper和数据库中对应的值是否相同
     * @return
     */
    public boolean isSame() {
        return StringUtils.equals(zkValue, mysqlValue);
    }

}
