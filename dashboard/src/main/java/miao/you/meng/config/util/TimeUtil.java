package miao.you.meng.config.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/14.
 */
public class TimeUtil {
    public static String getCurrentTime(){
        Date now = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return time.format(now);
    }
}
