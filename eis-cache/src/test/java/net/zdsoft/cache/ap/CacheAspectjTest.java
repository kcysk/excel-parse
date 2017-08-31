package net.zdsoft.cache.ap;

import com.sun.org.apache.xalan.internal.extensions.ExpressionContext;
import net.zdsoft.cache.expression.CacheEvaluationContext;
import net.zdsoft.cache.expression.CacheExpressionEvaluator;
import net.zdsoft.cache.expression.ExpressionRoot;
import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @since 2017.08.30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheAspectjTest {

    @Autowired
    private UDao uDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void getUById() {

        Teacher teacher = uDao.getTeacher("tt");
        System.out.println(teacher.getName());
    }

    @Test
    public void getUListById() {
        List<Teacher> teachers = uDao.getTList("tt");
        System.out.println(teachers.size());
    }

    @Test
    public void expressionTest() throws NoSuchMethodException {
        Teacher teacher = uDao.getTeacher("tt");
        Method method = UDao.class.getMethod("expression", List.class);
        List<Teacher> teachers = new ArrayList<Teacher>(){
            {
                Teacher t = new Teacher();
                Teacher t2 = new Teacher();
                t.setName("tt");
                t2.setName("tt2");
                add(t);
                add(t2);
            }
        };
        ExpressionRoot expressionRoot = new ExpressionRoot(uDao, method, new Object[]{teachers}, UDao.class);
        CacheExpressionEvaluator evaluator = new CacheExpressionEvaluator(new SpelExpressionParser());
        //StandardEvaluationContext context = new StandardEvaluationContext();
        //context.setVariable("args", new Object[]{teachers});
        CacheEvaluationContext context = new CacheEvaluationContext(expressionRoot, method, new Object[]{teachers}, evaluator.getParameterNameDiscoverer());
        String[] obj = evaluator.getExpression("#root.args[0].![#this.name]").getValue(context, String[].class);

        for (String s : obj) {
            System.out.println(s);
        }
    }
}
