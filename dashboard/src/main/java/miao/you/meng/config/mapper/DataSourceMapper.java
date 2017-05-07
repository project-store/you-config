package miao.you.meng.config.mapper;

import miao.you.meng.config.entity.DataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */
public interface DataSourceMapper {
    public List<DataSource> listDataSource();

    public void addParam(@Param("ds") DataSource ds);

    public DataSource findDSByName(@Param("appName") String appName);

    public String searchJson(@Param("id") int id);

    public void alertJson(@Param("id") int id, @Param("dsJson") String dsJson);

}
