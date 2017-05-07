package miao.you.meng.config.dashboard.controller;

import miao.you.meng.config.auth.AuthPassport;
import miao.you.meng.config.constants.AuthControl;
import miao.you.meng.config.dto.AppConfigCompareDTO;
import miao.you.meng.config.entity.AppConfig;
import miao.you.meng.config.constants.HttpResponseCode;
import miao.you.meng.config.model.JsonResponse;
import miao.you.meng.config.service.IAppService;
import miao.you.meng.config.service.IConfigService;
import miao.you.meng.config.util.ZookeeperUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14.
 */
@Controller
@RequestMapping("/zookeeper")
public class ZookeeperController {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperController.class);

    @Autowired
    private IAppService appService;

    @Autowired
    private IConfigService configService;

    /**
     * app页面中更新单个zookeeper的值
     */
    @AuthPassport(update = AuthControl.UPDATE)
    @ResponseBody
    @RequestMapping("/reflush/single")
    public JsonResponse reflushZookeeper(@RequestParam("appId") int appId,
                                         @RequestParam("configName") String configName,
                                         @RequestParam("configValue") String configValue){
        JsonResponse jsonResponse = new JsonResponse();
        String appName = appService.getNameById(appId);
        String nodePath = ZookeeperUtil.getNodePath(appName, StringUtils.trimToEmpty(configName));
        int flag = ZookeeperUtil.reflushNode(nodePath, configValue);

        if (flag == 0){
            jsonResponse.setCode(HttpResponseCode.SUCCESS);
            jsonResponse.setMessageText("更新成功");
        } else {
            jsonResponse.setCode(HttpResponseCode.FAIL);
            jsonResponse.setMessageText("更新失败");
        }
        return jsonResponse;
    }

    /**
     * app页面中更新所有zookeeper的值
     */
    @AuthPassport(update = AuthControl.UPDATE)
    @ResponseBody
    @RequestMapping("/reflush/all")
    public JsonResponse reflushAllZookeeper(@RequestParam("appId") int appId){
        String appName = appService.getNameById(appId);
        List<AppConfig> configList = configService.listAppConfig(appId);
        boolean flag = true;
        for (AppConfig config : configList){
            String nodePath = ZookeeperUtil.getNodePath(appName, StringUtils.trimToEmpty(config.getName()));
            int tk = ZookeeperUtil.reflushNode(nodePath, StringUtils.trimToEmpty(config.getValue()));
            if (tk != 0){
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

    /**
     * app页面中进行mysql 值和zookeeper值的对比
     */
    @AuthPassport(check = AuthControl.CHECK)
    @RequestMapping("/compare/mysql")
    public String compareWithMysql(@RequestParam("appId") int appId, ModelMap modelMap){
        String appName = appService.getNameById(appId);
        List<AppConfig> configList = configService.listAppConfig(appId);
        AppConfigCompareDTO zooMysqlListDTO = null;
        List<AppConfigCompareDTO> zooMysqlListDTOList = new ArrayList<AppConfigCompareDTO>();
        for (AppConfig config : configList){
            zooMysqlListDTO = new AppConfigCompareDTO();
            zooMysqlListDTO.setId(config.getId());
            zooMysqlListDTO.setName(config.getName());
            zooMysqlListDTO.setMysqlValue(config.getValue());
            String nodePath = ZookeeperUtil.getNodePath(appName, config.getName());
            logger.info("nodePath : [{}]  data : [{}] ", nodePath , ZookeeperUtil.readNode(nodePath));
            zooMysqlListDTO.setZooValue(ZookeeperUtil.readNode(nodePath));
            zooMysqlListDTOList.add(zooMysqlListDTO);
        }
        modelMap.addAttribute("appId", appId);
        modelMap.addAttribute("list", zooMysqlListDTOList);
        return "/config/list_mysql_zoo";
    }

}
