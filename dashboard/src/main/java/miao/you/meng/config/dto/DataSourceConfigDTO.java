package miao.you.meng.config.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by miaoyoumeng on 2017/5/7.
 */
public class DataSourceConfigDTO implements Serializable {

    private static final long serialVersionUID = -7685603818170648705L;

    private Map<String, String> config;

    private String type;
}