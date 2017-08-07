package net.zdsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author shenke
 * @since 17-8-6 下午10:07
 */
@SpringBootApplication
@EnableWebMvc
public class ApplicationT {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationT.class, args);
    }
}
