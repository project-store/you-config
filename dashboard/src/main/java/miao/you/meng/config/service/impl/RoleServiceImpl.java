package miao.you.meng.config.service.impl;

import miao.you.meng.config.entity.Role;
import miao.you.meng.config.entity.RoleResource;
import miao.you.meng.config.mapper.RoleMapper;
import miao.you.meng.config.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 通过id获取具体role
     */
    @Override
    public Role getRoleById(int id){
        Role role = roleMapper.getRoleById(id);
        return role;
    }

    /**
     * 通过url 和 id获取role的能力
     */
    @Override
    public List<RoleResource> getResourceByRoleId(int role_id, String url){
        List<RoleResource> roleResourceList = roleMapper.getResourceByRoleId(role_id, url);
        if (roleResourceList == null){
            return new ArrayList<>();
        }
        return  roleResourceList;
    }
}
