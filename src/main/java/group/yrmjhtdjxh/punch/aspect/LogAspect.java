package group.yrmjhtdjxh.punch.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


/**
 * AOP切面使用日志记录调用的方法
 */
@Aspect
@Component
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.yrmjhtdjxh.punch.web.*.*(..))")
    public void log(){}

    /**
     * 调用方法记录
     * @param joinPoint
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String url = request.getRequestURI().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() +"." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        // 使用内部类，方便记录
        RequestLog requestLog = new RequestLog(url,ip,classMethod,args);

        logger.info("Result : {}",requestLog);

    }

    @After("log()")
    public void doAfter(){
        //logger.info("--------------doAfter--------------");
    }

    /**
     * 方法返回记录
     * @param result
     */
    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result){
        logger.info("Result:{}",result);
    }

    /**
     * 内部类，方便记录
     */
    private class RequestLog{

        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
