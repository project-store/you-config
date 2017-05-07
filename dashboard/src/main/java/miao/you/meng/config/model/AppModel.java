package miao.you.meng.config.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by miaoyoumeng on 2017/5/6.
 */
public class AppModel implements Serializable {
    private static final long serialVersionUID = -5415483606741645788L;

    private String name;

    private int business;

    private String description;

    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBusiness() {
        return business;
    }

    public void setBusiness(int business) {
        this.business = business;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
