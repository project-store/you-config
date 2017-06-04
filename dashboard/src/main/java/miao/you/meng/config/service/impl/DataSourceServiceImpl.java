package miao.you.meng.config.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import miao.you.meng.config.constants.MySQLKey;
import miao.you.meng.config.constants.ResponseCode;
import miao.you.meng.config.dto.DataSourceDTO;
import miao.you.meng.config.dto.mysql.MySQLClusterDTO;
import miao.you.meng.config.entity.DataSource;
import miao.you.meng.config.mapper.DataSourceMapper;
import miao.you.meng.config.service.IDataSourceService;
import miao.you.meng.config.service.IDataSourceXmlService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/25.
 */
public class DataSourceServiceImpl implements IDataSourceService {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);


    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Autowired
    private IDataSourceXmlService dataSourceXmlService;

    /**
     * 罗列所有数据源
     */
    @Override
    public List<DataSourceDTO> listDataSource() {
        List<DataSource> list = this.dataSourceMapper.listDataSource();
        List<DataSourceDTO> dtoList = Lists.newArrayList();
        if (list == null || list.isEmpty()) {
            logger.info(" config list is empty ");
            return dtoList;
        }
        for(DataSource dataSource : list){
            String configXml = dataSource.getConfig();
            if(StringUtils.isBlank(configXml)){
                logger.error("configXml is blank");
                continue;
            }
            MySQLClusterDTO cluster = this.dataSourceXmlService.parseDataSource(configXml);
            if (cluster == null || cluster.getMaster() == null) {
                logger.error("datasource master config is null");
                continue;
            }

            DataSourceDTO dto = new DataSourceDTO();
            Map<String, String> mapKey = cluster.getMaster().getConnectionPool();

            logger.debug("master config [{}]", mapKey);

            dto.setUpdateTime(dataSource.getTs());
            dto.setId(dataSource.getId());
            dto.setHost(mapKey.get(MySQLKey.HOST));
            dto.setUrl(mapKey.get(MySQLKey.URL));
            dto.setPassword(mapKey.get(MySQLKey.PASSWORD));
            dto.setUserName(mapKey.get(MySQLKey.USERNAME));
            dto.setAppName(dataSource.getAppName());

            dtoList.add(dto);
        }
        logger.info("master config [{}]", dtoList);
        return dtoList;
    }

    /**
     * 增加数据源
     */
    @Override
    public int addDataSource(DataSource dataSource) {
        this.dataSourceMapper.insertDataSource(dataSource);
        return 0;
    }

    /**
     * 通过appName 找具体的数据源
     */
    @Override
    public DataSource findDSByName(String appName) {
        DataSource ds = this.dataSourceMapper.findDataSourceByName(appName);
        return ds;
    }

    /**
     * 通过appName 找具体的数据源
     */
    @Override
    public DataSource findDataSourceById(int id) {
        DataSource dataSource = this.dataSourceMapper.findDataSourceById(id);
        return dataSource;
    }

    /**
     * 通过id找出json格式的数据源配置
     */
    @Override
    public MySQLClusterDTO getConfigDetailById(int id) {
        DataSource dataSource = this.dataSourceMapper.findDataSourceById(id);
        if (dataSource == null) {
            return null;
        }
        MySQLClusterDTO cluster = this.dataSourceXmlService.parseDataSource(dataSource.getConfig());
        if (cluster == null) {
            return null;
        }
        cluster.setName(dataSource.getAppName());
        return cluster;
    }

    /**
     * 更改具体数据源的配置
     */
    @Override
    public int updateDataSource(int id, MySQLClusterDTO cluster) {
        DataSource dataSource = this.dataSourceMapper.findDataSourceById(id);
        if (dataSource != null) {
            String xml = this.dataSourceXmlService.config2xml(cluster);
            logger.debug("update datasource [{}],  xml {}", id,  xml);
            this.dataSourceMapper.updateDataSource(id, xml);
            return ResponseCode.SUCCESS;
        }
        return ResponseCode.FAIL;
    }
}
