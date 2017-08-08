package net.zdsoft;

import net.zdsoft.dataimport.BeanUtils;
import net.zdsoft.dataimport.User;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author shenke
 * @since 2017.08.08
 */
public class UtilsTest {

    @Test
    public void testGetFieldVal() {
        User user = new User();
        user.setUserName("tt");
        Arrays.stream(User.class.getDeclaredFields()).forEach(e->{
            if ( e.getName().equals("userName") ){
                try {
                    e.setAccessible(Boolean.TRUE);
                    System.out.println(e.get(user));
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    @Test
    public void testError() {
        System.out.println(error("测试{}，数据{}","99",2));
    }
    protected String error(String msg, Object ... values) {
        for (Object value : values) {
            msg = msg.replaceFirst("\\{\\}", String.valueOf(value));
        }
        return msg;
    }

}
