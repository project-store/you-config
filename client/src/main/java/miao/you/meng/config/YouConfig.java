package miao.you.meng.config;

import java.util.Iterator;
import java.util.Properties;

/**
 * Created by miaoyoumeng on 2017/4/12.
 */
interface YouConfig {
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

    String getString(String key);

    String getString(String key, String defaultValue);
}
