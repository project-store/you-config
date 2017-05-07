package miao.you.meng.config.service.impl;

import miao.you.meng.config.entity.User;
import miao.you.meng.config.mapper.UserMapper;
import miao.you.meng.config.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/4/28.
 */
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 通过id 获取用户
     */
    @Override
    public User getUserById(int id){
        User user = userMapper.getUserById(id);
        return user;
    }
}
