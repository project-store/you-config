package miao.you.meng.config.service;

import miao.you.meng.config.entity.AppInfo;

import java.util.List;

/**
 * Created by miaoyoumeng on 2017/4/13.
 */
public interface IAppService {

    public Integer countApp();

    public List<AppInfo> listApp();

    public int changeParam(int id, int business, String description, String email);

    public String getNameById(int id);

    public int addParam(AppInfo app);

    public AppInfo findAppByName(String name);
}
