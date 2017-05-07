package miao.you.meng.config.mapper;

import miao.you.meng.config.entity.AppConfig;
import miao.you.meng.config.entity.ConfigHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */
public interface ConfigMapper {

    public List<AppConfig> listConfig(@Param("appId") int appId);

    public void insertConfigHistory(@Param("configHistory")ConfigHistory configHistory);

    public void saveParam(@Param("id") int id, @Param("description") String description, @Param("value") String value);

    public void addParam(@Param("config") AppConfig config);

    public AppConfig findConfigById(@Param("id") int id);

    public AppConfig findConfigByNameAndAId(@Param("name") String name, @Param("appId") int appId);
}
