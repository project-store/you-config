package miao.you.meng.config.dashboard.controller;

import com.google.common.collect.Lists;
import miao.you.meng.config.auth.AuthPassport;
import miao.you.meng.config.constants.AuthControl;
import miao.you.meng.config.constants.BusinessType;
import miao.you.meng.config.entity.AppInfo;
import miao.you.meng.config.constants.HttpResponseCode;
import miao.you.meng.config.factory.EntityFactory;
import miao.you.meng.config.model.AppModel;
import miao.you.meng.config.model.JsonResponse;
import miao.you.meng.config.service.IAppService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by miaoyoumeng on 2017/4/13.
 * <p/>
 * 基本app操作
 */
@Controller
@RequestMapping("/app/info")
public class AppInfoController {

    private static final Logger logger = LoggerFactory.getLogger(AppInfoController.class);

    @Autowired
    private IAppService appService;

    /**
     * 列示所有turnk环境的app
     *
     * @param modelMap
     * @return
     */
    @AuthPassport(check = AuthControl.CHECK)
    @RequestMapping("/list")
    public String trunkAppList(ModelMap modelMap) {
        List<AppInfo> appList = appService.listApp();
        modelMap.addAttribute("appList", appList);
        List<Pair> businessList = Lists.newArrayList();
        for (BusinessType business : BusinessType.values()) {
            Pair pair = new ImmutablePair(business.getValue(), business.getName());
            businessList.add(pair);
        }
        modelMap.addAttribute("businessList", businessList);
        return "/app/list";
    }

    /**
     * 修改app的信息
     *
     * @param id
     * @param business
     * @param email
     * @param description
     */
    @AuthPassport(update = AuthControl.UPDATE)
    @ResponseBody
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public JsonResponse saveApp(@PathVariable("id") int id,
                                  @RequestParam("business") int business,
                                  @RequestParam(value = "email",required = false) String email,
                                  @RequestParam(value = "description",required = false) String description) {
        JsonResponse response = new JsonResponse();
        String appName = this.appService.getNameById(id);
        if (StringUtils.isBlank(appName)) {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("没有对应appId的数据");
            return response;
        }
        int flag = appService.changeParam(id, business, description, email);
        if (flag == 0) {
            response.setCode(HttpResponseCode.SUCCESS);
            response.setMessageText("保存成功");
        } else {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("保存失败");
        }
        return response;
    }

    /**
     * 新增app
     *
     * @param appModel
     */
    @AuthPassport(insert = AuthControl.INSERT)
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResponse addApp(AppModel appModel) {
        JsonResponse response = new JsonResponse();

        if (appService.findAppByName(StringUtils.trimToEmpty(appModel.getName())) != null) {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("该名字已经存在");
            return response;
        }
        AppInfo app = EntityFactory.createApp(appModel.getName(), appModel.getBusiness(), appModel.getDescription(), appModel.getEmail());
        int flag = appService.addParam(app);
        if (flag == 0) {
            response.setCode(HttpResponseCode.SUCCESS);
            response.setMessageText("新增成功");
        } else {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("新增失败");
        }
        return response;
    }
}
