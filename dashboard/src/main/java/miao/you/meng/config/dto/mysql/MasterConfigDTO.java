package miao.you.meng.config.dto.mysql;

import miao.you.meng.config.dto.DataSourceConfigDTO;
import miao.you.meng.config.enumeration.MySQLRole;

/**
 * 主库配置
 * Created by miaoyoumeng on 2017/5/7.
 */
public class MasterConfigDTO extends DataSourceConfigDTO {

    private static final long serialVersionUID = -133196553140824900L;

    private final String type = MySQLRole.MASTER.toString();

    public String getType() {
        return type;
    }

    public String getDisplayName(){
        return type;
    }
}
