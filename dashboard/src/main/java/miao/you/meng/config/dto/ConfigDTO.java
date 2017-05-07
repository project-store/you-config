package miao.you.meng.config.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/24.
 */
public class ConfigDTO implements Serializable {
    private static final long serialVersionUID = -988243176879348458L;

    private int id;

    private String name;

    private String value;

    private String description;

    private int compareCode;

    private Date ts;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCompareCode() {
        return compareCode;
    }

    public void setCompareCode(int compareCode) {
        this.compareCode = compareCode;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}
