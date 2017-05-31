package miao.you.meng.config.enumeration;

/**
 * 数据库角色
 * Created by miaoyoumeng on 2017/5/7.
 */
public enum MySQLRole {

    MASTER("master"),

    SLAVE("slave");

    private String value;

    MySQLRole(String type){
        this.value = type;
    }

    @Override
    public String toString() {
        return value;
    }
}
