package net.zdsoft.dataimport;

import net.zdsoft.ApplicationT;
import net.zdsoft.UserImportBiz;
import net.zdsoft.dataimport.biz.ImportRecord;
import net.zdsoft.dataimport.cache.ViewCache;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * @author shenke
 * @since 17-8-6 下午10:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationT.class)
@AutoConfigureMockMvc
public class UserImportTest {

    @Autowired
    UserImportBiz userImportBiz;

    @Test
    public void testImport() {
        String path = this.getClass().getResource("/conf/dd.xls").getPath();
    }

    @Test
    public void testNewBean() {
        Field[] fields = UserError.class.getDeclaredFields();
        Arrays.stream(fields).forEach(e->{

            //System.out.println(e.getType().getGenericInterfaces());
        });
    }

    @Autowired
    MockMvc mockMvc;


    @Autowired private ViewCache viewCache;

    @Test
    public void testViewCache() {
    }
}
