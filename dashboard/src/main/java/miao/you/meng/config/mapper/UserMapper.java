package miao.you.meng.config.mapper;

import miao.you.meng.config.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2017/4/28.
 */
public interface UserMapper {
    public User getUserById(@Param("id") int id);
}
