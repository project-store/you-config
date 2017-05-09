package miao.you.meng.config.dashboard.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import miao.you.meng.config.auth.AuthPassport;
import miao.you.meng.config.constants.AuthControl;
import miao.you.meng.config.constants.Constants;
import miao.you.meng.config.constants.HttpResponseCode;
import miao.you.meng.config.dto.DataSourceDTO;
import miao.you.meng.config.dto.MachineDTO;
import miao.you.meng.config.dto.DataSourceConfigCompareDTO;
import miao.you.meng.config.entity.AppInfo;
import miao.you.meng.config.entity.DataSource;
import miao.you.meng.config.model.JsonResponse;
import miao.you.meng.config.service.IAppService;
import miao.you.meng.config.service.IDataSourceService;
import miao.you.meng.config.util.JsonUtil;
import miao.you.meng.config.util.YamlUtil;
import miao.you.meng.config.util.ZookeeperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Administrator on 2017/4/25.
 * <p/>
 * 数据源配置文件的配置详情
 */
@Controller
@RequestMapping("/datasource/config")
public class DataSourceController {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceController.class);

    @Autowired
    private IDataSourceService dataSourceService;

    @Autowired
    private IAppService appService;

    /**
     * ds页面第一级
     * 罗列所有数据源配置
     */
    @AuthPassport(check = AuthControl.CHECK)
    @RequestMapping("/list")
    public String trunkDsList(ModelMap modelMap) {
        List<AppInfo> appList = this.appService.listApp();
        if (appList == null) {
            appList = Lists.newArrayList();
        }
        List<DataSourceDTO> dsList = dataSourceService.listDS();
        if (dsList == null) {
            dsList = Lists.newArrayList();
        }
        for (DataSourceDTO ds : dsList) {
            DataSourceDTO dataSource = new DataSourceDTO();
            Map<String, String> mapKey = Maps.newConcurrentMap();
            dataSource.setUpdateTime(ds.getUpdateTime());
            dataSource.setId(ds.getId());
            dataSource.setHost(mapKey.get("host"));
            dataSource.setPort(mapKey.get("port"));
            dataSource.setUrl(mapKey.get("url"));
            dataSource.setPassword(mapKey.get("password"));
            dataSource.setUserName(mapKey.get("username"));
        }
        modelMap.addAttribute("list", dsList);
        modelMap.addAttribute("appList", appList);
        return "/ds/list_dataSource";
    }

    /**
     * 增加新数据源
     *
     * @param appName
     * @param url
     * @param host
     * @param password
     * @param userName
     */
    @AuthPassport(insert = AuthControl.INSERT)
    @ResponseBody
    @RequestMapping("/add/trunk-env")
    public JsonResponse addParam(@RequestParam("appName") String appName, @RequestParam("url") String url, @RequestParam("host") String host,
                                 @RequestParam("password") String password, @RequestParam("userName") String userName) {
        JsonResponse response = new JsonResponse();

        return response;
    }

    /**
     * ds页面的第二级
     *
     * @param id 查看master 和所有 slave
     */
    @AuthPassport(check = AuthControl.CHECK)
    @RequestMapping("/detail/{id}")
    public String searchMachine(@PathVariable("id") int id, String appName, ModelMap modelMap) {
        DataSource dataSource = this.dataSourceService.findDataSourceById(id);
        if (dataSource == null) {

        }
        String dsJson = this.dataSourceService.searchJson(id);
        List<String> machines = JsonUtil.searchMachine(dsJson);
        List<MachineDTO> machineDTOList = new ArrayList<>();
        for (String machine : machines) {
            Map<String, String> map = JsonUtil.getMsg(dsJson, machine);
            MachineDTO machineDTO = new MachineDTO();
            machineDTO.setUserName(map.get("username"));
            machineDTO.setHost(map.get("host"));
            machineDTO.setPassword(map.get("password"));
            machineDTO.setMachine(machine);
            machineDTO.setUrl(map.get("url"));
            machineDTOList.add(machineDTO);
        }
        modelMap.addAttribute("list", machineDTOList);
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("appName", appName);
        return "/ds/list_ds_detail";
    }

    /**
     * ds页面的第三级
     * 数据源的详细配置
     * 在这个页面可以更新zookeeper
     */
    @AuthPassport(check = AuthControl.CHECK)
    @RequestMapping
    public String machineDetail(@PathVariable("id") int id, ModelMap modelMap) {
        DataSource dataSource = this.dataSourceService.findDataSourceById(id);
        if (dataSource == null) {
            return "/error/404";
        }
        String dsJson = this.dataSourceService.searchJson(id);
        Map<String, String> config = null;
        Map<String, String> other = new HashMap<>();
        config = JsonUtil.searchMachineDetail(dsJson, "", other);
        String nodePath = null;
        List<DataSourceConfigCompareDTO> configList = null;
        List<DataSourceConfigCompareDTO> otherList = null;
        if (config != null && other != null) {

        }
        modelMap.addAttribute("config", configList);
        modelMap.addAttribute("other", otherList);
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("machine", "");
        modelMap.addAttribute("appName", "");
        return "/ds/machine_detail";
    }

    /**
     * 对获取的详细配置map进行排序
     */
    public Map mapSort(Map<String, String> target) {
        Map<String, String> aim = new LinkedHashMap<>();
        List<String> list = new ArrayList<>(target.keySet());
        Collections.sort(list);
        for (String str : list) {
            aim.put(str, target.get(str));
        }
        return aim;
    }

    /**
     * 将详细配置map转换成界面dto
     */
    public List<DataSourceConfigCompareDTO> transferToDTO(Map ds, String nodePath) {
        List<DataSourceConfigCompareDTO> list = new ArrayList<>();
        Set<Map.Entry<String, String>> set = ds.entrySet();
        for (Map.Entry<String, String> entry : set) {
            StringBuilder sb = new StringBuilder(nodePath);
            sb.append(Constants.BACK_SLANT).append(entry.getKey());
            DataSourceConfigCompareDTO zooDSDTO = new DataSourceConfigCompareDTO();
            zooDSDTO.setKey(entry.getKey());
            zooDSDTO.setZooValue(ZookeeperUtil.readNode(sb.toString()));
            zooDSDTO.setMysqlValue(entry.getValue());
            if (zooDSDTO.getMysqlValue().equals(zooDSDTO.getZooValue())) {
                zooDSDTO.setCompare(1);
            } else {
                zooDSDTO.setCompare(0);
            }
            list.add(zooDSDTO);
        }
        return list;
    }

    /**
     * 修改mysql的值
     */
    @AuthPassport(update = AuthControl.UPDATE)
    @ResponseBody
    @RequestMapping("/alter/detail")
    public JsonResponse alertDetail(@RequestParam("id") int id, @RequestParam("machine") String machine, @RequestParam("key") String key,
                                    @RequestParam("value") String value, @RequestParam("appName") String appName) {
        value = value.trim();
        JsonResponse response = new JsonResponse();
        String dsJson = this.dataSourceService.searchJson(id);
        dsJson = JsonUtil.alterDetail(dsJson, machine, key, value, appName);
        if (dsJson == null) {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("修改失败");
        } else {
            this.dataSourceService.alertJson(id, dsJson);
            response.setCode(HttpResponseCode.SUCCESS);
            response.setMessageText("更新成功");
        }
        return response;
    }

    /**
     * 根据mysql的值更新单个zookeeper的值
     */
    @ResponseBody
    @RequestMapping("/update/zookeeper")
    public JsonResponse updateToZookeeper(@RequestParam("appName") String appName, @RequestParam("machine") String machine, @RequestParam("key") String key,
                                          @RequestParam("value") String value) {
        String nodePath = ZookeeperUtil.getDSPath(appName.trim(), machine.trim(), key.trim());
        JsonResponse response = new JsonResponse();
        if (ZookeeperUtil.reflushNode(nodePath, value.trim()) == 0) {
            response.setCode(HttpResponseCode.SUCCESS);
            response.setMessageText("zookeeper更新成功");
        } else {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("zookeeper更新失败");
        }
        return response;
    }

    /**
     * 根据mysql的值，更新所有zookeeper的值
     */
    @ResponseBody
    @RequestMapping("/reflush/all")
    public JsonResponse reflushAllToZookeeper(@RequestParam("appName") String appName, @RequestParam("id") int id) {
        JsonResponse response = new JsonResponse();
        String dsJson = this.dataSourceService.searchJson(id);
        if (JsonUtil.reflushToZoo(appName, dsJson) == 0) {
            response.setCode(HttpResponseCode.SUCCESS);
            response.setMessageText("刷新成功");
        } else {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("刷新失败");
        }
        return response;
    }
}
