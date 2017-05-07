package miao.you.meng.config.service;

import miao.you.meng.config.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2017/4/28.
 */
public interface IUserService {
    public User getUserById(int id);
}
