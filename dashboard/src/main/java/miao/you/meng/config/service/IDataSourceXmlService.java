package miao.you.meng.config.service;

import miao.you.meng.config.dto.mysql.MySQLClusterDTO;
import org.dom4j.Document;

import java.io.File;

/**
 * Created by miaoyoumeng on 2017/6/3.
 */
public interface IDataSourceXmlService {

    /**
     * 将xml文件解析Java对象
     * @param xmlFile
     * @return
     */
    public MySQLClusterDTO parseDataSource(File xmlFile);

    /**
     * 将xml字符串解析Java对象
     * @param xml
     * @return
     */
    public MySQLClusterDTO parseDataSource(String xml);

    public MySQLClusterDTO parseDataSource(Document document);

    /**
     *
     * 将MySQLClusterDTO对象转换为xml 字符串
     * @param cluster
     * @return
     */
    public String config2xml(MySQLClusterDTO cluster);
}
