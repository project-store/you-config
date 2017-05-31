package miao.you.meng.config.dto.mysql;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * mysql 集群配置
 * Created by miaoyoumeng on 2017/5/31.
 */
public class MySQLClusterDTO implements Serializable{

    private static final long serialVersionUID = 3504421302879783047L;

    private MasterConfigDTO master;

    private List<SlaveConfigDTO> slaves = Lists.newArrayList();

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
}

