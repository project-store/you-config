package miao.you.meng.config.service.impl;

import com.google.common.collect.Maps;
import miao.you.meng.config.constants.XMLNodeName;
import miao.you.meng.config.dto.DataSourceConfigDTO;
import miao.you.meng.config.dto.mysql.MasterConfigDTO;
import miao.you.meng.config.dto.mysql.MySQLClusterDTO;
import miao.you.meng.config.dto.mysql.SlaveConfigDTO;
import miao.you.meng.config.service.IDataSourceXmlService;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by miaoyoumeng on 2017/6/3.
 */
public class DataSourceXmlServiceImpl implements IDataSourceXmlService {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceXmlServiceImpl.class);

    public MySQLClusterDTO parseDataSource(String xml) {
        if (StringUtils.isBlank(xml)) {
            logger.error("xml String is blank .......");
            return null;
        }
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new ByteArrayInputStream(xml.getBytes(CharEncoding.UTF_8)));
        } catch (DocumentException e) {
            logger.error("read xml string error  .......[{}]", e);
            return null;
        } catch (UnsupportedEncodingException e){
            logger.error("read xml string UnsupportedEncodingException  .......[{}]", e);
            return null;
        }
        return parseDataSource(document);
    }

    public MySQLClusterDTO parseDataSource(File xmlFile) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(xmlFile);
        } catch (DocumentException e) {
            logger.error("read xmlFile error  .......[{}]", e);
            return null;
        }
        return parseDataSource(document);
    }
    /**
     * 解析xml字符串，获取数据源
     * @param document
     */
    public MySQLClusterDTO parseDataSource(Document document) {
        if (document == null){
            return null;
        }

        MySQLClusterDTO cluster = new MySQLClusterDTO();
        Element root = document.getRootElement();

        List<Element> configList = root.elements();
        if (configList == null || configList.isEmpty()) {
            return null;
        }
        for (Element element : configList) {
            //master 配置节点
            if(XMLNodeName.MASTER.equals(element.getName())) {
                MasterConfigDTO master  = parseMaster(element);
                cluster.setMaster(master);
            } else if(XMLNodeName.SLAVE.equals(element.getName())){
                SlaveConfigDTO slave = parseSlave(element);
                cluster.addSlave(slave);
            }
        }
        root.getQName(XMLNodeName.MASTER);

        return cluster;
    }

    /**
     * 读取主节点配置
     * @param masterConfig
     * @return
     */
    private MasterConfigDTO parseMaster(Element masterConfig) {
        List<Element> configList = masterConfig.elements();
        if (configList == null || configList.isEmpty()) {
            return null;
        }
        MasterConfigDTO master = new MasterConfigDTO();
        parseDataSourceConnectionPool(masterConfig, master);
        return master;
    }

    /**
     * 读取从节点配置
     * @param slaveConfig
     * @return
     */
    private SlaveConfigDTO parseSlave(Element slaveConfig) {
        List<Element> configList = slaveConfig.elements();
        if (configList == null || configList.isEmpty()) {
            return null;
        }
        SlaveConfigDTO slave = new SlaveConfigDTO();
        parseDataSourceConnectionPool(slaveConfig, slave);
        return slave;
    }

    /**
     * 解析数据源配置
     * @param element
     * @param config
     */
    private void parseDataSourceConnectionPool(Element element, DataSourceConfigDTO config) {

        Element configEle = element.element(XMLNodeName.CONFIG);
        if (configEle == null) {
            return;
        }
        List<Element> configList = configEle.elements();
        if (configList == null || configList.isEmpty()) {
            return;
        }
        Map<String, String> map = Maps.newConcurrentMap();
        for (Element child : configList) {
            //master 配置节点
            String key = child.getName();
            String value = child.getText().trim();

            if (StringUtils.isNotBlank(key)) {
                config.setConfig(key, value);
            }
        }
    }

    /**
     *
     * 将MySQLClusterDTO对象转换为xml 字符串
     * @param cluster
     * @return
     */
    public String config2xml(MySQLClusterDTO cluster) {
        Element root = DocumentHelper.createElement(XMLNodeName.ROOT);
        Document document = DocumentHelper.createDocument(root);


        Element masterEle = root.addElement(XMLNodeName.MASTER);
        Element masterConfigEle = masterEle.addElement(XMLNodeName.CONFIG);

        for(String key :cluster.getMaster().getConnectionPool().keySet()) {
            Element keyEle = masterConfigEle.addElement(key);
            keyEle.setText(StringUtils.trimToEmpty(cluster.getMaster().getConnectionPool().get(key)));
        }

        List<SlaveConfigDTO> slaveConfigList = cluster.getSlaves();
        if (slaveConfigList != null && !slaveConfigList.isEmpty()) {
            for (int index = 0; index < slaveConfigList.size(); index++ ){
                Element slaveEle = root.addElement(XMLNodeName.SLAVE);
                Element slaveConfigEle = slaveEle.addElement(XMLNodeName.CONFIG);
                SlaveConfigDTO slave = slaveConfigList.get(index);
                slaveEle.addAttribute("id", String.valueOf(index + 1));
                for(String key :slave.getConnectionPool().keySet()) {
                    Element keyEle = slaveConfigEle.addElement(key);
                    keyEle.setText(StringUtils.trimToEmpty(slave.getConnectionPool().get(key)));
                }
            }
        }

        OutputFormat format = new OutputFormat("    ",true);
        format.setEncoding( CharEncoding.UTF_8);//设置编码格式
        StringWriter stringWriter = new StringWriter();
        try {
            XMLWriter xmlWriter = new XMLWriter(stringWriter, format);
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }
}
