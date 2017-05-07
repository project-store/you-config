package miao.you.meng.config.dto;

import com.google.common.collect.Maps;
import miao.you.meng.config.constants.MySQL;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by miaoyoumeng on 2017/5/7.
 */
public class SlaveConfigDTO implements Serializable {

    private static final long serialVersionUID = -133196553140824900L;

    private Map<String, String> config = Maps.newConcurrentMap();

    private final String type = MySQL.SLAVE.toString();


}
