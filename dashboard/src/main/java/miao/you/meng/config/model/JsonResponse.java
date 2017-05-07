package miao.you.meng.config.model;

import java.io.Serializable;

/**
 * Created by miaoyoumeng on 2017/4/14.
 */
public class JsonResponse implements Serializable {

    private static final long serialVersionUID = -8888635159337415810L;

    private int code;

    private String messageText;

    private Object object;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
