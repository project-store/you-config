package miao.you.meng.config;

import com.google.common.collect.Maps;
import miao.you.meng.commons.lang.LangUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by miaoyoumeng on 2017/4/12.
 */
public class YouStandardConfig implements YouConfig {

    private final Map<String, Object> store = Maps.newLinkedHashMap();

    private boolean throwExceptionOnMissing;

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
        return store.get(key);
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
        Boolean b = getBoolean(key, null);
        if (b != null) {
            return b.booleanValue();
        } else {
            throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
        }
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(key, defaultValue ? Boolean.TRUE : Boolean.FALSE).booleanValue();
    }

    @Override
    public Boolean getBoolean(String key, Boolean defaultValue) {
        Object value = resolveContainerStore(key);
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return LangUtils.parseBoolean(value);
            } catch (ClassCastException e) {
                throw new NoSuchElementException('\'' + key + "' doesn't map to a Boolean object");
            }
        }
    }

    @Override
    public byte getByte(String key) {
        Byte b = getByte(key, null).byteValue();
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
        Object value = resolveContainerStore(key);
        if (value == null) {
            return defaultValue;
        } else {
            try {
                return LangUtils.parseByte(value);
            } catch (ClassCastException e) {
                throw new NoSuchElementException('\'' + key + "' doesn't map to a Boolean object");
            }
        }
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
        Object value = resolveContainerStore(key);

        if (value == null) {
            return defaultValue;
        } else {
            return LangUtils.parseDouble(value);
        }
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
        Object value = resolveContainerStore(key);

        if (value == null) {
            return defaultValue;
        } else {
            return LangUtils.parseFloat(value);
        }
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
        return getInteger(key, new Integer(defaultValue)).intValue();
    }

    @Override
    public Integer getInteger(String key, Integer defaultValue) {
        Object value = resolveContainerStore(key);
        if (value == null) {
            return defaultValue;
        } else {
            return LangUtils.parseInt(value);
        }
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
            return LangUtils.parseLong(value);
        }
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
        Object value = resolveContainerStore(key);

        if (value == null) {
            return defaultValue;
        } else if (value instanceof String) {
            return value.toString();
        } else {
            throw new ClassCastException('\'' + key + "' doesn't map to a String object");
        }
    }

    private Object resolveContainerStore(String key) {
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

    protected void setProperty(String key, Object value) {
        store.put(key, value);
    }

    protected void removeProperty(String key) {
        store.remove(key);
    }

    public boolean isThrowExceptionOnMissing() {
        return throwExceptionOnMissing;
    }
}
