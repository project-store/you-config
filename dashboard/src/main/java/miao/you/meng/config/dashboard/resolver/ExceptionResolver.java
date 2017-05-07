package miao.you.meng.config.dashboard.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常捕捉，将异常的url，输出到日志中去
 */
public class ExceptionResolver extends SimpleMappingExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        // 记录异常
        if (ex != null) {
            //调试的时候，需要将异常栈信息打印出来
            if (logger.isInfoEnabled()) {
                ex.printStackTrace();
            }
            logger.error("request url: {},  Exception: {}", request.getServletPath(), ex);
        }
        return super.resolveException(request, response, handler, ex);
    }
}