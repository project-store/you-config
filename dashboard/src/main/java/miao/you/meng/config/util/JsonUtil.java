package miao.you.meng.config.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * Created by Administrator on 2017/4/27.
 */
public class JsonUtil {

    private JsonUtil() {}

    /**
     * 将machine的数据抽取出来,一般抽取 username ， url, host, password  ， machine就是master， slave
     */
    public static Map getMsg(String objString, String machine){
        JSONObject obj = JSON.parseObject(objString);
        JSONObject obj1 = null;
        Map map = null;
        Iterator<String> it = obj.keySet().iterator();
        while(it.hasNext()){
            String rootKey = it.next();
            if (machine.equals(rootKey)){
                obj1 = obj.getJSONObject(rootKey);
                map = searchConfig(obj1);
            }
        }
        return map;
    }

    /**
     * getMsg 的函数的补充
     */
    public static Map searchConfig(JSONObject obj){
        Iterator<String> it1 = obj.keySet().iterator();
        JSONObject obj2 = null;
        Map<String, String> mapKey = null;
        while(it1.hasNext()){
            String k = it1.next();
            if ("config".equals(k)){
                obj2 = obj.getJSONObject(k);
                Iterator<String> tt = obj2.keySet().iterator();
                mapKey = new HashMap<>();
                while(tt.hasNext()){
                    String ak = tt.next();
                    if ("url".equals(ak) || "host".equals(ak) || "password".equals(ak) || "username".equals(ak)){
                        mapKey.put(ak, obj2.get(ak).toString());
                    }
                }
                break;
            }
        }
        return mapKey;
    }

    /**
     * 将所有的machine 遍历
     */
    public static List<String> searchMachine(String dsJson){
        JSONObject obj = JSON.parseObject(dsJson);
        List<String> list = new ArrayList<>();
        Iterator<String> it = obj.keySet().iterator();
        while(it.hasNext()){
            list.add(it.next());
        }
        return list;
    }

    /**
     * 获取json格式的数据源的具体配置
     */
    public static Map searchMachineDetail(String dsJson, String machine, Map<String, String> other){
        JSONObject obj = JSON.parseObject(dsJson);
        Iterator<String> it = obj.keySet().iterator();
        Map<String, String> config = new HashMap<>();
        while(it.hasNext()){
            String key = it.next();
            if (machine.equals(key)){
                JSONObject obj1 = obj.getJSONObject(key);
                Iterator<String> item = obj1.keySet().iterator();
                while(item.hasNext()){
                    String root = item.next();
                    if ("config".equals(root)){
                        config = getMachineConfig(obj1.get(root).toString());
                        continue;
                    }
                    other.put(root, obj1.get(root).toString());
                }
            }
        }
        return config;
    }

    /**
     * 获取具体数据源的config配置
     */
    public static Map getMachineConfig(String dsJson){
        JSONObject obj = JSON.parseObject(dsJson);
        Map<String, String> config = new HashMap<>();
        Iterator<String> item = obj.keySet().iterator();
        while(item.hasNext()){
            String key = item.next();
            config.put(key, obj.get(key).toString());
        }
        return config;
    }

    /**
     * 更改数据源的具体配置
     */
    public static String alterDetail(String dsJson, String machine, String key, String value, String appName){
        JSONObject obj = JSON.parseObject(dsJson);
        JSONObject obj1 = null;
        Iterator<String> item = obj.keySet().iterator();
        boolean flag = false;
        while(item.hasNext()){
            String root = item.next();
            if (machine.equals(root)){
                obj1 = obj.getJSONObject(root);
                Iterator<String> item1 = obj1.keySet().iterator();
                while(item1.hasNext()){
                    String root1 = item1.next();
                    if (key.equals(root1)){
                        obj1.put(key, value);
                        flag = true;
                        break;
                    }
                }
                if (flag == true){
                    break;
                } else {
                    JSONObject conf = obj1.getJSONObject("config");
                    Iterator<String> itemConf = conf.keySet().iterator();
                    while(itemConf.hasNext()){
                        String keyConf = itemConf.next();
                        if (key.equals(keyConf)){
                            conf.put(key, value);
                            obj1.put("config", conf);
                            flag = true;
                            break;
                        }
                    }
                }
            }
        }
        if (flag == false){
            return null;
        }
        obj.put(machine, obj1);
        return obj.toString();
    }

    /**
     * 更加mysql中数据源的值，更新zookeeper中的值
     */
    public static int reflushToZoo(String appName, String dsJson){
        JSONObject obj = JSON.parseObject(dsJson);
        JSONObject obj1 = null;
        String nodePath = null;
        Iterator<String> item = obj.keySet().iterator();
        try{
            while(item.hasNext()){
                String root = item.next();
                obj1 = obj.getJSONObject(root);
                Iterator<String> it = obj1.keySet().iterator();
                while(it.hasNext()){
                    String key = it.next();
                    if (!"config".equals(key)){
                        nodePath = ZookeeperUtil.getDSPath(appName, root, key);
                        ZookeeperUtil.reflushNode(nodePath, obj1.get(key).toString());
                    }
                }
                JSONObject conf = obj1.getJSONObject("config");
                Iterator<String> itemConf = conf.keySet().iterator();
                while(itemConf.hasNext()){
                    String keyConf = itemConf.next();
                    nodePath = ZookeeperUtil.getDSPath(appName, root, "config", keyConf);
                    ZookeeperUtil.reflushNode(nodePath, conf.get(keyConf).toString());
                }
            }
        } catch(Exception e){
            return 1;
        }
        return 0;
    }
}
