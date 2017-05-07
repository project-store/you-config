package miao.you.meng.config.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by miaoyoumeng on 2017/5/4.
 */
public class YamlUtil {
    public static String insertYamlToZookKeeper(String host, String url, String password, String appName, String userName) throws FileNotFoundException {
        new FileInputStream(new File("src/main/resources/ds-template.yml"));

        return "";
    }
}
