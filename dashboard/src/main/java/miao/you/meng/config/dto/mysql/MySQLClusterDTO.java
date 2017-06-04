package miao.you.meng.config.dto.mysql;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * mysql 集群配置
 * Created by miaoyoumeng on 2017/5/31.
 */
public class MySQLClusterDTO implements Serializable{

    private static final long serialVersionUID = 3504421302879783047L;

    private String name;

    private MasterConfigDTO master;

    private List<SlaveConfigDTO> slaves = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MasterConfigDTO getMaster() {
        return master;
    }

    public void setMaster(MasterConfigDTO master) {
        this.master = master;
    }

    public List<SlaveConfigDTO> getSlaves() {
        return slaves;
    }


    public void addSlave(SlaveConfigDTO slave) {
        this.slaves.add(slave);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

