package miao.you.meng.config.constants;

/**
 * Created by miaoyoumeng on 2017/5/7.
 */
public enum MySQL {
    MASTER("master"),
    SLAVE("slave");

    private String value;

    MySQL(String type){
        this.value = type;
    }

    @Override
    public String toString() {
        return value;
    }
}
