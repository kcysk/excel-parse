package net.zdsoft.dataimport;

import net.zdsoft.ApplicationT;
import net.zdsoft.UserImportBiz;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
        Future<Reply<User>> replyFuture = userImportBiz.execute(path);
        System.out.println(replyFuture.isDone());
        try {
            Thread.sleep(1000);
            Reply<User> reply = replyFuture.get();
            reply.getJavaObjects().forEach(e-> System.out.println(e.getUserName()));
            reply.getErrorObjects().forEach(e-> System.out.println(e.getSex()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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

    @Test
    public void testUserImportIndex() {
        try {
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/import/index").accept(MediaType.TEXT_HTML_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
            mvcResult.getResponse().getOutputStream().println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
