package net.zdsoft.dataimport;

import net.zdsoft.ApplicationT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
public class UserImportTest {

    @Autowired UserImportBiz userImportBiz;

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
}
