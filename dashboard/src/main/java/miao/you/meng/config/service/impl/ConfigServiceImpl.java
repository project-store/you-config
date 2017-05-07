package miao.you.meng.config.service.impl;

import miao.you.meng.config.constants.DeleteCode;
import miao.you.meng.config.constants.ResponseCode;
import miao.you.meng.config.entity.AppConfig;
import miao.you.meng.config.entity.ConfigHistory;
import miao.you.meng.config.mapper.ConfigMapper;
import miao.you.meng.config.service.IConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */
public class ConfigServiceImpl implements IConfigService {

    @Autowired
    private ConfigMapper configMapper;

    /**
     * 罗列具体app的所有config
     */
    @Override
    public List<AppConfig> listAppConfig(int appId){

        return configMapper.listConfig(appId);
    }

    @Override
    public int insertConfigHistory(ConfigHistory configHistory){
        configMapper.insertConfigHistory(configHistory);
        return 0;
    }

    /**
     * 更改config的值，同时添加config历史记录 configHistory
     */
    /*Transactional 属于事务管理*/
    @Transactional
    @Override
    public int saveAppConfig(int id, String description, String value, AppConfig config){
        configMapper.saveParam(id, description, value);
        ConfigHistory configHistory = new ConfigHistory();
        configHistory.setCreateTime(new Date());
        configHistory.setConfigId(id);
        configHistory.setOldValue(config.getValue());
        configHistory.setNewValue(value);
        configMapper.insertConfigHistory(configHistory);
        return ResponseCode.SUCCESS;
    }

    /**
     * 增加config
     */
    @Override
    public int addAppConfig(AppConfig config){
        config.setName(StringUtils.trimToEmpty(config.getName()));
        config.setValue(StringUtils.trimToEmpty(config.getValue()));
        config.setDeleted(DeleteCode.NOT_DELETE);
        configMapper.addParam(config);
        return 0;
    }

    /**
     * 通过id寻找具体config
     */
    @Override
    public AppConfig findConfigById(int id){
        return configMapper.findConfigById(id);
    }

    /**
     * 通过name 和 appid 找具体config
     */
    @Override
    public AppConfig findConfigByNameAndAId(String name, int appId){
        return configMapper.findConfigByNameAndAId(name, appId);
    }
}
