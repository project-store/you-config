package miao.you.meng.config.service;

import miao.you.meng.config.entity.AppConfig;
import miao.you.meng.config.entity.ConfigHistory;

import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */
public interface IConfigService {

    public List<AppConfig> listAppConfig(int appId);

    public int insertConfigHistory(ConfigHistory configHistory);

    public int saveAppConfig(int id,  String description, String value, AppConfig config);

    public int addAppConfig(AppConfig config);

    public AppConfig findConfigById(int id);

    public AppConfig findConfigByNameAndAId(String name, int appId);
}
