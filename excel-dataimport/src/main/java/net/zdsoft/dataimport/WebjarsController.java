package net.zdsoft.dataimport;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
import org.webjars.WebJarAssetLocator;
import org.webjars.WebJarAssetLocatorEx;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理静态资源
 * @author shenke
 * @since 2017.08.07
 */
@Controller
public class WebjarsController {

    private final WebJarAssetLocator webJarAssetLocator = new WebJarAssetLocatorEx();

    @ResponseBody
    @RequestMapping("/webjarsLocator/{webJar}/**")
    public Object loadWebJars(@PathVariable("webJar") String webJar, HttpServletRequest request) {
        try {
            String prefix = "/webjarsLocator/" + webJar + "/";
            String mvcPath = (String)request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
            String realPath = webJarAssetLocator.getFullPath(webJar, mvcPath.substring(prefix.length()));
            return new ResponseEntity<>(new ClassPathResource(realPath), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
