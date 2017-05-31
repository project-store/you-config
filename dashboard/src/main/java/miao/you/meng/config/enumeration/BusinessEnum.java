package miao.you.meng.config.enumeration;


/**
 * Created by miaoyoumeng on 2017/4/14.
 *
 * 开发组列表
 */
public enum BusinessEnum {


    DEVELOP_1(1, "应用开发"),
    JIAGOU(100, "架构组");

    private int value;
    private String name;

    BusinessEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
