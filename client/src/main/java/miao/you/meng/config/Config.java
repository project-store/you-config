package miao.you.meng.config;

import org.apache.commons.configuration.ConversionException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Created by miaoyoumeng on 2017/4/12.
 */
public interface Config {
    boolean isEmpty();


    boolean containsKey(String key);


    Object getProperty(String key);


    Iterator<String> getKeys(String prefix);


    Iterator<String> getKeys();


    Properties getProperties(String key);


    boolean getBoolean(String key);


    boolean getBoolean(String key, boolean defaultValue);


    Boolean getBoolean(String key, Boolean defaultValue);


    byte getByte(String key);


    byte getByte(String key, byte defaultValue);


    Byte getByte(String key, Byte defaultValue);


    double getDouble(String key);


    double getDouble(String key, double defaultValue);


    Double getDouble(String key, Double defaultValue);


    float getFloat(String key);


    float getFloat(String key, float defaultValue);


    Float getFloat(String key, Float defaultValue);


    int getInt(String key);


    int getInt(String key, int defaultValue);

    Integer getInteger(String key, Integer defaultValue);

    long getLong(String key);

    long getLong(String key, long defaultValue);


    Long getLong(String key, Long defaultValue);


    short getShort(String key);


    short getShort(String key, short defaultValue);


    Short getShort(String key, Short defaultValue);


    BigDecimal getBigDecimal(String key);


    BigDecimal getBigDecimal(String key, BigDecimal defaultValue);


    BigInteger getBigInteger(String key);


    BigInteger getBigInteger(String key, BigInteger defaultValue);


    String getString(String key);


    String getString(String key, String defaultValue);


    String[] getStringArray(String key);

    List<Object> getList(String key);


    List<Object> getList(String key, List<Object> defaultValue);
}