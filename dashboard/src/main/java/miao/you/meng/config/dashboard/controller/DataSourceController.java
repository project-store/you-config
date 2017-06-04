package miao.you.meng.config.dashboard.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import miao.you.meng.commons.lang.LangUtils;
import miao.you.meng.config.auth.AuthPassport;
import miao.you.meng.config.constants.*;
import miao.you.meng.config.dto.DataSourceConfigDTO;
import miao.you.meng.config.dto.DataSourceDTO;
import miao.you.meng.config.dto.DataSourceConfigCompareDTO;
import miao.you.meng.config.dto.mysql.MasterConfigDTO;
import miao.you.meng.config.dto.mysql.MySQLClusterDTO;
import miao.you.meng.config.dto.mysql.SlaveConfigDTO;
import miao.you.meng.config.entity.DataSource;
import miao.you.meng.config.enumeration.MySQLRole;
import miao.you.meng.config.model.JsonResponse;
import miao.you.meng.config.service.IDataSourceService;
import miao.you.meng.config.service.IDataSourceXmlService;
import miao.you.meng.config.service.IZookeeperService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by Administrator on 2017/4/25.
 * <p/>
 * 数据源配置文件的配置详情
 */
@Controller
@RequestMapping("/datasource/config")
public class DataSourceController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(DataSourceController.class);

    private static final String BASE_DIR = "/ds/";

    @Autowired
    private IDataSourceService dataSourceService;

    @Autowired
    private IDataSourceXmlService dataSourceXmlService;

    @Autowired
    private IZookeeperService zookeeperService;

    /**
     * 数据源列表页第一级，罗列所有数据源配置
     * @param modelMap
     * @return
     */
    @AuthPassport(check = AuthControl.CHECK)
    @RequestMapping("/list")
    public String trunkDsList(ModelMap modelMap) {
        List<DataSourceDTO> dsList = dataSourceService.listDataSource();
        if (dsList == null) {
            dsList = Lists.newArrayList();
        }
        modelMap.addAttribute("list", dsList);
        return BASE_DIR + "list_data_source";
    }

    /**
     * 增加新数据源
     * @param appName
     * @param url
     * @param host
     * @param password
     * @param userName
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResponse addDataSource(@RequestParam("appName") String appName,
                                      @RequestParam("url") String url,
                                      @RequestParam("host") String host,
                                      @RequestParam("password") String password,
                                      @RequestParam("userName") String userName) {
        JsonResponse response = new JsonResponse();
        if (StringUtils.isBlank(appName)) {
            response.setCode(ResponseCode.FAIL);
            response.setMessageText("数据源名称为空");
        } else if (StringUtils.isBlank(url)) {
            response.setCode(ResponseCode.FAIL);
            response.setMessageText("url为空");
        } else if (StringUtils.isBlank(host)) {
            response.setCode(ResponseCode.FAIL);
            response.setMessageText("host为空");
        } else if (StringUtils.isBlank(password)) {
            response.setCode(ResponseCode.FAIL);
            response.setMessageText("密码为空");
        } else if (StringUtils.isBlank(userName)) {
            response.setCode(ResponseCode.FAIL);
            response.setMessageText("用户名为空");
        }

        String filePath = Thread.currentThread().getContextClassLoader().getResource("/").getPath() + "/datasource-template.xml";
        logger.info("datasource-template path : [{}]", filePath);
        MySQLClusterDTO cluster = this.dataSourceXmlService.parseDataSource(new File(filePath));
        logger.info("config :[ {} ]", cluster);

        if (cluster != null &&  cluster.getMaster() != null) {
            cluster.getMaster().setConfig(MySQLKey.HOST, host);
            cluster.getMaster().setConfig(MySQLKey.URL, url);
            cluster.getMaster().setConfig(MySQLKey.PASSWORD, password);
            cluster.getMaster().setConfig(MySQLKey.USERNAME, userName);
        }
        String xml = this.dataSourceXmlService.config2xml(cluster);
        logger.info("xml config :[ {} ]", xml);

        DataSource dataSource = new DataSource();
        dataSource.setCreateTime(new Date());
        dataSource.setConfig(xml);
        dataSource.setAppName(appName);
        this.dataSourceService.addDataSource(dataSource);
        response.setMessageText("添加成功");
        return response;
    }


    /**
     * 数据源页面的第二级
     * @param id appId
     * @param modelMap
     * @return
     */
    @AuthPassport(check = AuthControl.CHECK)
    @RequestMapping("/show/{id}")
    public String showDataSource(@PathVariable("id") int id, ModelMap modelMap) {

        MySQLClusterDTO cluster = this.dataSourceService.getConfigDetailById(id);
        if (cluster == null) {
            return "/error/404";
        }
        List<DataSourceConfigDTO> list = Lists.newArrayList();
        //
        if (cluster.getMaster() != null) {
            list.add(cluster.getMaster());
        }
        if (cluster.getSlaves() != null && !cluster.getSlaves().isEmpty()) {
            list.addAll(cluster.getSlaves());
        }
        modelMap.addAttribute("configList", list);
        modelMap.addAttribute("appName", cluster.getName());
        return BASE_DIR + "list_data_source_show";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/update")
    public JsonResponse updateDataSource(@RequestParam("dataSourceId") int dataSourceId,
                                         @RequestParam("type") String type,
                                         @RequestParam("key") String key,
                                         @RequestParam("value") String value,
                                         HttpServletRequest request) {
        JsonResponse response = new JsonResponse();
        if (StringUtils.isBlank(key)) {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("key为空");
            return response;
        }
        Integer index = LangUtils.parseInt(request.getParameter("index"));
        value = StringUtils.trimToEmpty(value);

        MySQLClusterDTO cluster = this.dataSourceService.getConfigDetailById(dataSourceId);
        if (!StringUtils.equals(type, MySQLRole.MASTER.toString())
                && !StringUtils.equals(type, MySQLRole.SLAVE.toString())){
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("数据类型不存在");
            return response;
        }

        if (StringUtils.equals(type, MySQLRole.MASTER.toString()) && cluster.getMaster() != null) {
            logger.info("update key [{}], value {}", key, value);
            cluster.getMaster().getConnectionPool().put(key, value);
        } else if (StringUtils.equals(type, MySQLRole.SLAVE.toString())
                && cluster.getSlaves() != null
                && !cluster.getSlaves().isEmpty()
                && index != null
                && !(index > cluster.getSlaves().size() || index < 1)) {
            logger.info("update key [{}], value {}", key, value);
            SlaveConfigDTO slave = cluster.getSlaves().get(index - 1);
            slave.getConnectionPool().put(key, value);
        }

        int result = this.dataSourceService.updateDataSource(dataSourceId, cluster);
        if (ResponseCode.SUCCESS == result){
            response.setMessageText("更新成功");
        } else {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("更新失败");
        }
        return response;
    }

    /**
     * 数据源页面的第三级
     * @param id appId
     * @param modelMap
     * @return
     */
    @AuthPassport(check = AuthControl.CHECK)
    @RequestMapping("/detail/master/{id}")
    public String detail(@PathVariable("id") int id, ModelMap modelMap) {
        MySQLClusterDTO cluster = this.dataSourceService.getConfigDetailById(id);
        if (cluster == null || cluster.getMaster() == null) {
            return "/error/404";
        }
        List<DataSourceConfigCompareDTO> list = Lists.newArrayList();
        Set<String> keySet = Sets.newHashSet();
        for(String key : cluster.getMaster().getConnectionPool().keySet()) {
            keySet.add(key);
        }
        Ordering<String> naturalOrdering = Ordering.natural();
        List<String> keyList = naturalOrdering.sortedCopy(keySet);
        Map<String, String> configMap = cluster.getMaster().getConnectionPool();
        for(String key : keyList) {
            DataSourceConfigCompareDTO compare = new DataSourceConfigCompareDTO();

            compare.setKey(key);
            compare.setMysqlValue(configMap.get(key));
            String zkValue = zookeeperService.getMasterConfigKey(cluster.getName(), key);
            compare.setZkValue(zkValue);
            list.add(compare);
        }
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("appName", cluster.getName());
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("type", MySQLRole.MASTER.toString());
        return BASE_DIR + "list_data_source_detail";
    }

    /**
     * 数据源页面的第三级
     * @param id appId
     * @param modelMap
     * @return
     */
    @AuthPassport(check = AuthControl.CHECK)
    @RequestMapping("/detail/slave/{id}/{index}")
    public String detail(@PathVariable("id") int id,
                         @PathVariable("index") int index,
                         ModelMap modelMap) {
        MySQLClusterDTO cluster = this.dataSourceService.getConfigDetailById(id);
        if (cluster == null || cluster.getSlaves() == null
                || index > cluster.getSlaves().size()
                || index < 1) {
            return "/error/404";
        }
        List<DataSourceConfigCompareDTO> list = Lists.newArrayList();
        Set<String> keySet = Sets.newHashSet();
        SlaveConfigDTO slave = cluster.getSlaves().get(index - 1 );
        for(String key : slave.getConnectionPool().keySet()) {
            keySet.add(key);
        }
        Ordering<String> naturalOrdering = Ordering.natural();
        List<String> keyList = naturalOrdering.sortedCopy(keySet);

        Map<String, String> configMap = slave.getConnectionPool();
        for(String key : keyList) {
            DataSourceConfigCompareDTO compare = new DataSourceConfigCompareDTO();

            compare.setKey(key);
            compare.setMysqlValue(configMap.get(key));
            String zkValue = zookeeperService.getSlaveConfigKey(cluster.getName(), index, key);
            compare.setZkValue(zkValue);
            list.add(compare);
        }
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("appName", cluster.getName());
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("type", MySQLRole.SLAVE.toString());
        modelMap.addAttribute("index", index);
        return BASE_DIR + "list_data_source_detail";
    }
    /**
     * 根据mysql的值更新单个zookeeper的值
     */
    @ResponseBody
    @RequestMapping(value = "/zookeeper/master/key/{id}", method = RequestMethod.POST )
    public JsonResponse updateMasterToZookeeper(@PathVariable("id") int id,
                                          @RequestParam("key") String key) {
        MySQLClusterDTO cluster = this.dataSourceService.getConfigDetailById(id);
        JsonResponse response = new JsonResponse();
        if (cluster == null || cluster.getMaster() == null || StringUtils.isBlank(key)) {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("数据不存在");
            return response;
        }

        MasterConfigDTO master = cluster.getMaster();
        String appName = cluster.getName();

        String value = null;
        for(String config : master.getConnectionPool().keySet()) {
            if (StringUtils.equals(config, key)) {
                value = master.getConnectionPool().get(key);
                break;
            }
        }
        logger.info("appName [{}], master config key [{}], value {}", appName, key, value);
        int result = ResponseCode.FAIL;
        result = this.zookeeperService.refreshMasterConfig(appName, key, value);
        if (ResponseCode.SUCCESS == result){
            response.setMessageText("同步成功");
        } else {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("同步失败");
        }
        return response;
    }


    /**
     * 根据mysql的值更新单个zookeeper的值
     */
    @ResponseBody
    @RequestMapping(value = "/zookeeper/slave/key/{id}", method = RequestMethod.POST )
    public JsonResponse updateSlaveToZookeeper(@PathVariable("id") int id,
                                               @RequestParam("index") int index,
                                               @RequestParam("key") String key) {

        MySQLClusterDTO cluster = this.dataSourceService.getConfigDetailById(id);
        JsonResponse response = new JsonResponse();
        if (cluster == null || StringUtils.isBlank(key) || cluster.getSlaves() == null ||
                cluster.getSlaves().isEmpty() || index > cluster.getSlaves().size()
                || index < 1) {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("数据不存在");
            return response;
        }

        SlaveConfigDTO slave = cluster.getSlaves().get(index - 1);
        String appName = cluster.getName();

        String value = null;
        for(String config : slave.getConnectionPool().keySet()) {
            if (StringUtils.equals(config, key)) {
                value = slave.getConnectionPool().get(key);
                break;
            }
        }
        logger.info("appName [{}], master config key [{}], value {}", appName, key, value);
        int result = this.zookeeperService.refreshSlaveConfig(appName, index, key, value);
        if (ResponseCode.SUCCESS == result){
            response.setMessageText("同步成功");
        } else {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("同步失败");
        }
        return response;
    }

    /**
     * 根据mysql的值，更新所有zookeeper的值
     */
    @ResponseBody
    @RequestMapping(value = "/refresh/master/all", method = RequestMethod.POST)
    public JsonResponse refreshMasterAll2Zookeeper(@RequestParam("id") int id) {
        MySQLClusterDTO cluster = this.dataSourceService.getConfigDetailById(id);
        JsonResponse response = new JsonResponse();
        if (cluster == null || cluster.getMaster() == null ) {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("数据不存在");
            return response;
        }

        MasterConfigDTO master = cluster.getMaster();
        String appName = cluster.getName();
        int result = this.zookeeperService.refreshMasterConfig(appName, cluster.getMaster().getConnectionPool());
        if (ResponseCode.SUCCESS == result){
            response.setMessageText("同步成功");
        } else {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("同步失败");
        }
        return response;
    }
    /**
     * 根据mysql的值，更新所有zookeeper的值
     */
    @ResponseBody
    @RequestMapping(value = "/refresh/slave/all", method = RequestMethod.POST)
    public JsonResponse refreshSlaveAll2Zookeeper(@RequestParam("id") int id,
                                                  @RequestParam("index") int index) {
        JsonResponse response = new JsonResponse();
        MySQLClusterDTO cluster = this.dataSourceService.getConfigDetailById(id);
        if (cluster == null || cluster.getSlaves() == null ||
                cluster.getSlaves().isEmpty() || index > cluster.getSlaves().size()
                || index < 1) {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("数据不存在");
            return response;
        }

        SlaveConfigDTO slave = cluster.getSlaves().get(index - 1);
        String appName = cluster.getName();
        int result = this.zookeeperService.refreshSlaveConfig(appName, index, slave.getConnectionPool());
        if (ResponseCode.SUCCESS == result){
            response.setMessageText("同步成功");
        } else {
            response.setCode(HttpResponseCode.FAIL);
            response.setMessageText("同步失败");
        }
        return response;
    }
}
