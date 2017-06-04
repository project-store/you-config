package miao.you.meng.config.mapper;

import miao.you.meng.config.dto.DataSourceDTO;
import miao.you.meng.config.entity.DataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */
public interface DataSourceMapper {

    /**
     * 分页查询所有数据库应用配置
     * @return
     */
    public List<DataSource> listDataSource();

    public void insertDataSource(@Param("ds") DataSource ds);

    public DataSource findDataSourceByName(@Param("appName") String appName);

    public DataSource findDataSourceById(@Param("id")int id);

    public String getConfigXmlById(@Param("id") int id);

    /**
     * 更新数据源配置
     * @param id
     * @param xml
     */
    public void updateDataSource(@Param("id") int id, @Param("config") String xml);

}
