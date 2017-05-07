package miao.you.meng.config.mapper;

import miao.you.meng.config.entity.Role;
import miao.you.meng.config.entity.RoleResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */
public interface RoleMapper {
    public Role getRoleById(@Param("id") int id);

    public List<RoleResource> getResourceByRoleId(@Param("role_id") int role_id, @Param("url") String url);
}
