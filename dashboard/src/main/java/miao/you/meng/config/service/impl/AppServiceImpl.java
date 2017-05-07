package miao.you.meng.config.service.impl;

import com.google.common.collect.Lists;
import miao.you.meng.config.entity.AppInfo;
import miao.you.meng.config.mapper.AppMapper;
import miao.you.meng.config.service.IAppService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by miaoyoumeng on 2017/4/13.
 */

public class AppServiceImpl implements IAppService {

    @Autowired
    private AppMapper appMapper;

    /**
     * app的数量
     */
    @Override
    public Integer countApp() {
        return this.appMapper.countAppInfo();
    }

    /**
     * 罗列所有app
     */
    @Override
    public List<AppInfo> listApp(){
        List<AppInfo> appList = appMapper.listAppInfo();
        if (appList == null) {
            appList = Lists.newArrayList();
        }
        return appList;
    }

    /**
     * 更改app的 business 和 description
     */
    @Override
    public int changeParam(int id, int business, String description, String email){
        appMapper.updateAppInfo(id, business, description, email);
        return 0;
    }

    /**
     * 通过id获取app name
     */
    @Override
    public String getNameById(int id){
        return appMapper.getNameById(id);
    }

    /**
     * 增加app
     */
    @Override
    public int addParam(AppInfo app){
        appMapper.addAppInfo(app);
        return 0;
    }

    /**
     * 通过appName 和 envId 获取app
     */
    @Override
    public AppInfo findAppByName(String name){
        return appMapper.findAppInfoByName(name);
    }
}
