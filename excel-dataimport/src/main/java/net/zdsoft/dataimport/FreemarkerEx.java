package net.zdsoft.dataimport;

import freemarker.template.SimpleHash;
import freemarker.template.TemplateModelException;
import net.zdsoft.dataimport.freemarker.ViewDataFreemarkerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shenke
 * @since 2017.08.10
 */
@Configuration
public class FreemarkerEx {
    private Logger logger = LoggerFactory.getLogger(FreemarkerEx.class);
    @Resource
    private freemarker.template.Configuration freeMarkerConfiguration;

    @PostConstruct
    public void addVariables() {
        Map<String,Object> map = new HashMap();
        map.put("ViewDataFreemarkerUtils", new ViewDataFreemarkerUtils());
        try {
            this.freeMarkerConfiguration.setAllSharedVariables(new SimpleHash(map));
        } catch (TemplateModelException e) {
            logger.error("ViewDataFreemarkerUtils freemarker共享失败");
        }
    }
}
