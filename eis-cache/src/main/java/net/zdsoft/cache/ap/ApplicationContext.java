package net.zdsoft.cache.ap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shenke
 * @since 2017.08.30
 */
@SpringBootApplication
@ComponentScan(basePackages = {"net.zdsoft.cache.config","net.zdsoft.cache.ap"})
public class ApplicationContext {

    public static void main(String[] args){
        SpringApplication.run(ApplicationContext.class, args);
    }
}
