package miao.you.meng.config;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.PropertyConverter;
import org.apache.commons.lang.ObjectUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by miaoyoumeng on 2017/4/12.
 */
public class YouStandardConfig implements Config {

    private final Map<String, Object> store = Maps.newLinkedHashMap();

    @Override
    public boolean isEmpty() {
        return store.isEmpty();
    }

    @Override
    public boolean containsKey(String key) {
        return store.containsKey(key);
    }

    @Override
    public Object getProperty(String key) {
        return null;
    }

    @Override
    public Iterator<String> getKeys(String prefix) {
        return null;
    }

    @Override
    public Iterator<String> getKeys() {
        return store.keySet().iterator();
    }

    @Override
    public Properties getProperties(String key) {
        return null;
    }

    @Override
    public boolean getBoolean(String key) {
        return false;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        Boolean b = getBoolean(key, null);
        if (b != null) {
            return b.booleanValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        Object value = resolveContainerStore(key);

        if (value == null) {
            return defaultValue;
        } else {
            try {
                return PropertyConverter.toBoolean(interpolate(value));
            } catch (ConversionException e) {
                throw new ConversionException('\'' + key + "' doesn't map to a Boolean object", e);
            }
        }
    }

    @Override
    public byte getByte(String key) {
        Byte b = getByte(key, null).byteValue()
        if (b != null) {
            return b.byteValue();
        } else {
            throw new NoSuchElementException('\'' + key + " doesn't map to an existing object");
        }
    }

    @Override
    public byte getByte(String key, byte defaultValue) {
        return getByte(key, new Byte(defaultValue)).byteValue();
    }

    @Override
    public Byte getByte(String key, Byte defaultValue) {
        return null;
    }

    @Override
    public double getDouble(String key) {
        Double d = getDouble(key, null);
        if (d != null) {
            return d.doubleValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return getDouble(key, new Double(defaultValue)).doubleValue();
    }

    @Override
    public Double getDouble(String key, Double defaultValue) {
        return null;
    }

    @Override
    public float getFloat(String key) {
        Float f = getFloat(key, null);
        if (f != null) {
            return f.floatValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return getFloat(key, new Float(defaultValue)).floatValue();
    }

    @Override
    public Float getFloat(String key, Float defaultValue) {
        return null;
    }

    @Override
    public int getInt(String key) {
        Integer i = getInteger(key, null);
        if (i != null) {
            return i.intValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    @Override
    public int getInt(String key, int defaultValue) {
        Integer i = getInteger(key, null);

        if (i == null) {
            return defaultValue;
        }

        return i.intValue();
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        return null;
    }

    @Override
    public long getLong(String key) {
        Long l = getLong(key, null);
        if (l != null) {
            return l.longValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return getLong(key, new Long(defaultValue)).longValue();
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        Object value = resolveContainerStore(key);

        if (value == null) {
            return defaultValue;
        } else {
            try {
                return PropertyConverter.toLong(interpolate(value));
            } catch (ConversionException e) {
                throw new ConversionException('\'' + key + "' doesn't map to a Long object", e);
            }
        }
    }

    @Override
    public short getShort(String key) {
        Short s = getShort(key, null);
        if (s != null) {
            return s.shortValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    @Override
    public short getShort(String key, short defaultValue) {
        return getShort(key, new Short(defaultValue)).shortValue();
    }

    @Override
    public Short getShort(String key, Short defaultValue) {
        return null;
    }

    @Override
    public BigDecimal getBigDecimal(String key) {
        BigDecimal number = getBigDecimal(key, null);
        if (number != null) {
            return number;
        } else if (isThrowExceptionOnMissing()) {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return null;
    }

    @Override
    public BigInteger getBigInteger(String key) {
        BigInteger number = getBigInteger(key, null);
        if (number != null) {
            return number;
        } else if (isThrowExceptionOnMissing()) {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        } else {
            return null;
        }
    }

    @Override
    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        return null;
    }

    @Override
    public String getString(String key) {
        String s = getString(key, null);
        if (s != null) {
            return s;
        } else if (isThrowExceptionOnMissing()) {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        } else {
            return null;
        }
    }

    @Override
    public String getString(String key, String defaultValue) {
        return null;
    }

    @Override
    public String[] getStringArray(String key) {
        Object value = getProperty(key);

        String[] array;

        if (value instanceof String) {
            array = new String[1];

            array[0] = interpolate((String) value);
        } else if (value instanceof List) {
            List<?> list = (List<?>) value;
            array = new String[list.size()];

            for (int i = 0; i < array.length; i++) {
                array[i] = interpolate(ObjectUtils.toString(list.get(i), null));
            }
        } else if (value == null) {
            array = new String[0];
        } else if (isScalarValue(value)) {
            array = new String[1];
            array[0] = value.toString();
        } else {
            throw new ConversionException('\'' + key + "' doesn't map to a String/List object");
        }
        return array;
    }

    @Override
    public List<Object> getList(String key) {
        return null;
    }

    @Override
    public List<Object> getList(String key, List<Object> defaultValue) {
        return null;
    }

    private boolean isThrowExceptionOnMissing() {
        return true;
    }

    protected Object resolveContainerStore(String key) {
        Object value = getProperty(key);
        if (value != null) {
            if (value instanceof Collection) {
                Collection<?> collection = (Collection<?>) value;
                value = collection.isEmpty() ? null : collection.iterator().next();
            } else if (value.getClass().isArray() && Array.getLength(value) > 0) {
                value = Array.get(value, 0);
            }
        }

        return value;
    }

    protected String interpolate(String base) {
        Object result = interpolate((Object) base);
        return (result == null) ? null : result.toString();
    }

    protected Object interpolate(Object value)
    {
        return PropertyConverter.interpolate(value, this);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(store);
    }
}
