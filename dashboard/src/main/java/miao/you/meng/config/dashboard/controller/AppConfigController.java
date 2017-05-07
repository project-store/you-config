package miao.you.meng.config.dashboard.controller;

import com.google.common.collect.Lists;
import miao.you.meng.config.auth.AuthPassport;
import miao.you.meng.config.constants.AuthControl;
import miao.you.meng.config.dto.ConfigDTO;
import miao.you.meng.config.dto.AppConfigCompareDTO;
import miao.you.meng.config.entity.AppConfig;
import miao.you.meng.config.constants.HttpResponseCode;
import miao.you.meng.config.model.JsonResponse;
import miao.you.meng.config.service.IAppService;
import miao.you.meng.config.service.IConfigService;
import miao.you.meng.config.service.IZookeeperService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 *
 * app中的config配置详情
 */
@Controller
@RequestMapping("/app/config")
public class AppConfigController {

    private static final Logger logger = LoggerFactory.getLogger(AppConfigController.class);

    @Autowired
    private IConfigService configService;

    @Autowired
    private IAppService appService;

    @Autowired
    private IZookeeperService zookeeperService;

    /**
     * 列举全部config
     * @param appId
     */
    @AuthPassport(check = AuthControl.CHECK)
    @RequestMapping(value = "/show/all/{appId}", method = RequestMethod.GET)
    public String showConfig(@PathVariable("appId") int appId, ModelMap modelMap){
        String appName = appService.getNameById(appId);
        if (StringUtils.isBlank(appName)) {
            return "/error/404";
        }
        List<AppConfig> configList =  configService.listAppConfig(appId);
        List<ConfigDTO> ConfigDTOList = Lists.newArrayList();
        for (AppConfig config : configList){
            ConfigDTO configDTO = new ConfigDTO();
            configDTO.setId(config.getId());
            configDTO.setName(config.getName());
            configDTO.setDescription(config.getDescription());
            configDTO.setValue(config.getValue());
            configDTO.setTs(new Date());
            ConfigDTOList.add(configDTO);
        }
        modelMap.addAttribute("configList", ConfigDTOList);
        modelMap.addAttribute("appId", appId);
        modelMap.addAttribute("appName", appName);
        return "/config/list_config";
    }

    /**
     * 保存更改的值
     * @param id
     * @param value
     * @param description
     */
    @AuthPassport(update = AuthControl.UPDATE)
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResponse saveParam(@RequestParam("id") int id, @RequestParam("value") String value,
                                  @RequestParam("description") String description){

        AppConfig config = configService.findConfigById(id);
        int flag = configService.saveAppConfig(id, description.trim(), value.trim(), config);
        JsonResponse response = new JsonResponse();
        if (flag == 0){
            response.setCode(HttpResponseCode.SUCCESS);
            response.setMessageText("保存成功");
        } else {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("保存失败");
        }
        return response;
    }

    /**
     * 添加新的config
     * @param config
     */
    @AuthPassport(insert = AuthControl.INSERT)
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResponse addParam(AppConfig config){
        JsonResponse response = new JsonResponse();
        if (configService.findConfigByNameAndAId(config.getName().trim(), config.getAppId()) != null){
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("配置项已经存在");
            return response;
        }
        if (StringUtils.isBlank(config.getName())){
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("配置项包为空串");
            return response;
        }
        config.setCreateTime(new Date());

        int flag = configService.addAppConfig(config);
        if (flag == 0){
            response.setCode(HttpResponseCode.SUCCESS);
            response.setMessageText("新增成功");
        } else {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("新增失败");
        }
        return response;
    }

    /**
     * 更新所有单条配置节点
     * @param appId
     * @param configId
     * @return
     */
    @AuthPassport(update = AuthControl.UPDATE)
    @ResponseBody
    @RequestMapping(value = "/synchronous/zookeeper/{appId}/{configId}", method = RequestMethod.POST)
    public JsonResponse synchronousZookeeper(@PathVariable("appId") int appId,
                                             @PathVariable("configId") int configId) {
        JsonResponse response = new JsonResponse();
        AppConfig appConfig = this.configService.findConfigById(configId);
        if (appConfig == null || appConfig.getAppId() != appId) {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("刷新失败");
            return response;
        }
        String appName = this.appService.getNameById(appId);
        if (StringUtils.isBlank(appName)) {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("应用名字不存在");
            return response;
        }
        String zkName = appConfig.getName();
        String zkValue = appConfig.getValue();
        int code = this.zookeeperService.createAppConfigNode(appName, zkName, zkValue);
        if (code == 0){
            response.setCode(HttpResponseCode.SUCCESS);
            response.setMessageText("新增成功");
        } else {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("新增失败");
        }
        return response;
    }

    /**
     * app页面中更新所有zookeeper的值
     */
    @AuthPassport(update = AuthControl.UPDATE)
    @ResponseBody
    @RequestMapping(value = "/refresh/zookeeper/all/{appId}", method = RequestMethod.POST)
    public JsonResponse refreshAllZookeeper(@PathVariable("appId") int appId){
        String appName = appService.getNameById(appId);
        List<AppConfig> configList = configService.listAppConfig(appId);
        boolean flag = true;
        for (AppConfig config : configList){
            String zkName = config.getName();
            String zkValue = config.getValue();
            int responseCode = this.zookeeperService.createAppConfigNode(appName, zkName, zkValue);
            if (responseCode != 0) {
                flag = false;
            }
        }
        JsonResponse jsonResponse = new JsonResponse();
        if (flag == true){
            jsonResponse.setCode(HttpResponseCode.SUCCESS);
            jsonResponse.setMessageText("更新成功");
        } else {
            jsonResponse.setCode(HttpResponseCode.FAIL);
            jsonResponse.setMessageText("更新不完全");
        }
        return jsonResponse;
    }

    @AuthPassport(check = AuthControl.CHECK)
    @RequestMapping("/compare/{appId}")
    public String compareWithMysql(@PathVariable("appId") int appId, ModelMap modelMap){
        String appName = appService.getNameById(appId);
        if (StringUtils.isBlank(appName)) {
            return "/error/404";
        }
        List<AppConfig> configList = configService.listAppConfig(appId);
        List<AppConfigCompareDTO> compareList = Lists.newArrayList();
        for (AppConfig config : configList){
            AppConfigCompareDTO compareDto = new AppConfigCompareDTO();
            compareDto.setId(config.getId());
            compareDto.setName(config.getName());
            compareDto.setMysqlValue(config.getValue());
            String value = this.zookeeperService.getAppConfigNode(appName, config.getName());
            compareDto.setZooValue(value);
            compareList.add(compareDto);
        }
        modelMap.addAttribute("appId", appId);
        modelMap.addAttribute("appName", appName);
        modelMap.addAttribute("list", compareList);
        return "/config/list_mysql_zoo";
    }
}
