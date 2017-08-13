package net.zdsoft.dataimport;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author shenke
 * @since 17-8-13 下午3:08
 */
@Component
public class ImportApplicationContext implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ImportApplicationContext.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplication() {
        return applicationContext;
    }
}
