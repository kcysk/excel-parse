package net.zdsoft.dataimport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author shenke
 * @since 2017.08.08
 */
@SpringBootApplication
@EnableWebMvc
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}
