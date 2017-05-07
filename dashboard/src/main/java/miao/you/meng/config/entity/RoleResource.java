package miao.you.meng.config.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/4/28.
 */
public class RoleResource implements Serializable {
    private static final long serialVersionUID = 2427665587077692339L;
    private int id;
    private int roleId;
    private String urlPattern;
    private String urlDescription;
    private int methodMask;
    private int envId;
    private int configType;
    private Timestamp ts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    public String getUrlDescription() {
        return urlDescription;
    }

    public void setUrlDescription(String urlDescription) {
        this.urlDescription = urlDescription;
    }

    public int getMethodMask() {
        return methodMask;
    }

    public void setMethodMask(int methodMask) {
        this.methodMask = methodMask;
    }

    public int getEnvId() {
        return envId;
    }

    public void setEnvId(int envId) {
        this.envId = envId;
    }

    public int getConfigType() {
        return configType;
    }

    public void setConfigType(int configType) {
        this.configType = configType;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }
}
