package miao.you.meng.config.auth;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/4/28.
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthPassport {
    int insert() default 0;
    int drop() default 0;
    int check() default 0;
    int update() default 0;
}
