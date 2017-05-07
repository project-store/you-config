package miao.you.meng.config.service;

import miao.you.meng.config.entity.Role;
import miao.you.meng.config.entity.RoleResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */
public interface IRoleService {
    public Role getRoleById(int id);

    public List<RoleResource> getResourceByRoleId(int role_id, String url);
}
