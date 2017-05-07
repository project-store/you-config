package miao.you.meng.config.auth;

import miao.you.meng.config.entity.Role;
import miao.you.meng.config.entity.RoleResource;
import miao.you.meng.config.entity.User;
import miao.you.meng.config.service.IRoleService;
import miao.you.meng.config.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

//拦截器
public class AuthInterceptor extends HandlerInterceptorAdapter{

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    /**
     * 拦截器拦截所有页面
     * 进行权限对比
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception{
        int userId = 1;
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)){
            AuthPassport authPassport = ((HandlerMethod) handler).getMethodAnnotation(AuthPassport.class);  //如果出现了@AuthPassport则一定存在
            if (authPassport != null){   //只要存在就一定要设置
                String currentUrl = request.getServletPath();
                User user = userService.getUserById(userId);
                List<RoleResource> roleResourceList = roleService.getResourceByRoleId(user.getRoleId(), currentUrl);
                int flag = 0;
                for (RoleResource roleResource : roleResourceList){
                    flag = flag | roleResource.getMethodMask();
                }
                int valid = authPassport.check() | authPassport.drop() | authPassport.insert() | authPassport.update();
                if (valid != 0 && (valid & flag) == valid){
                    return true;
                }
                return true;
            } else {
                return true;
            }
        }
        return true;
    }
}
