package net.zdsoft.dataimport;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shenke
 * @since 17-8-10 下午11:06
 */
@Aspect
@Component
public class ImportActionAdvice {

    public static final String ACTION_NAME = "actionName";

    @After(value = "execution(* net.zdsoft.dataimport.AbstractImportAction.import*(..))")
    public void execute(JoinPoint joinPoint) {
        Class<?> action = joinPoint.getTarget().getClass();
        RequestMapping requestMapping = action.getAnnotation(RequestMapping.class);
        String[] values = requestMapping.value();
        getCurrentRequest().setAttribute(ACTION_NAME, values[0]);
    }

    private HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if ( requestAttributes == null ) {
            return null;
        }
        return requestAttributes.getRequest();
    }
}
