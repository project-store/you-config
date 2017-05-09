package miao.you.meng.config.service;

import miao.you.meng.config.dto.DataSourceDTO;
import miao.you.meng.config.entity.DataSource;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */
public interface IDataSourceService {
    public List<DataSourceDTO> listDS();

    public int addParam(DataSource ds);

    public DataSource findDSByName(String appName);

    public DataSource findDataSourceById(int id);

    public String searchJson(int id);

    public int alertJson(int id, String dsJson);

}
