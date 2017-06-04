package miao.you.meng.config.service;

import miao.you.meng.config.dto.DataSourceDTO;
import miao.you.meng.config.dto.mysql.MySQLClusterDTO;
import miao.you.meng.config.entity.DataSource;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */
public interface IDataSourceService {
    public List<DataSourceDTO> listDataSource();

    public int addDataSource(DataSource ds);

    public DataSource findDSByName(String appName);

    public DataSource findDataSourceById(int id);

    public MySQLClusterDTO getConfigDetailById(int id);

    /**
     * 更新数据源配置
     * @param id
     * @param cluster
     * @return
     */
    public int updateDataSource(int id, MySQLClusterDTO cluster);

}
