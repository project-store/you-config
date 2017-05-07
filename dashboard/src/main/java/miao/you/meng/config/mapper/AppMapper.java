package miao.you.meng.config.mapper;

import miao.you.meng.config.entity.AppInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by miaoyoumeng on 2017/4/13.
 */
public interface AppMapper {

    public Integer countAppInfo();

    public List<AppInfo> listAppInfo();

    public void updateAppInfo(@Param("id") int id, @Param("business") int business,
                            @Param("description") String description,  @Param("email") String email);

    public String getNameById(@Param("id") int id);

    public void addAppInfo(@Param("app") AppInfo app);

    public AppInfo findAppInfoByName(@Param("name") String name);

}
