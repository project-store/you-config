package miao.you.meng.config.factory;

import miao.you.meng.config.entity.AppInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 使用工厂模式构造出数据库对应的实体，减少数据校验逻辑判断
 * Created by miaoyoumeng on 2017/5/6.
 */
public class EntityFactory {

    private EntityFactory() {}

    public static AppInfo createApp(String name, int business, String description, String email) {
        AppInfo app = new AppInfo();
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("argument name is blank");
        }

        name = StringUtils.trim(name);
        email = StringUtils.trimToEmpty(email);
        description = StringUtils.trimToEmpty(description);
        app.setName(name);
        app.setBusiness(business);
        app.setDescription(description);
        app.setEmail(email);
        app.setCreateTime(new Date());
        return app;
    }

}
