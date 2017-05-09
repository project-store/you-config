package miao.you.meng.config.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/25.
 */
public class DataSource implements Serializable {
    private static final long serialVersionUID = 7978242819178704541L;

    private int id;

    private int appId;

    private String connectionPool;

    private Date createTime;

    private Date ts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(String connectionPool) {
        this.connectionPool = connectionPool;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}
