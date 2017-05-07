package miao.you.meng.config.constants;


/**
 * Created by miaoyoumeng on 2017/4/14.
 *
 * 开发组列表
 */
public enum BusinessType {


    DEVELOP_1(1, "应用开发"),
    JIAGOU(100, "架构组");

    private int value;
    private String name;

    BusinessType(int value, String name) {
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
