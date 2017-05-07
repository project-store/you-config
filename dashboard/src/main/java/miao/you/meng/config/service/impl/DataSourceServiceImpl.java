package miao.you.meng.config.service.impl;

import miao.you.meng.config.entity.DataSource;
import miao.you.meng.config.mapper.DataSourceMapper;
import miao.you.meng.config.service.IDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */
public class DataSourceServiceImpl implements IDataSourceService {

    @Autowired
    private DataSourceMapper dsMapper;

    /**
     * 罗列所有数据源
     */
    @Override
    public List<DataSource> listDS(){
        List<DataSource> list = dsMapper.listDataSource();
        if (list == null){
            return new ArrayList<DataSource>();
        }
        return list;
    }

    /**
     * 增加数据源
     */
    @Override
    public int addParam(DataSource ds){
        dsMapper.addParam(ds);
        return 0;
    }

    /**
     * 通过appName 找具体的数据源
     */
    @Override
    public DataSource findDSByName(String appName){
        DataSource ds = dsMapper.findDSByName(appName);
        return ds;
    }

    /**
     * 通过id找出json格式的数据源配置
     */
    @Override
    public String searchJson(int id){
        String s = dsMapper.searchJson(id);
        return s;
    }

    /**
     * 更改具体数据源的配置
     */
    @Override
    public int alertJson(int id, String dsJson){
        dsMapper.alertJson(id, dsJson);
        return 0;
    }
}
