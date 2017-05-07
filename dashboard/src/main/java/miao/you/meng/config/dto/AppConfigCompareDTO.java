package miao.you.meng.config.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/18.
 *
 * app中zookeeper 和 mysql 对比界面的dto
 */
public class AppConfigCompareDTO implements Serializable {

    private static final long serialVersionUID = 6435725325663475092L;

    private int id;

    private String name;

    private String zooValue;

    private String mysqlValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
