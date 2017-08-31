package net.zdsoft.cache.ap;

import net.zdsoft.cache.annotation.CacheClear;
import net.zdsoft.cache.annotation.Cacheabel;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @since 2017.08.30
 */
@Component
public class UDao {

    @Cacheabel(key = "#root.args[0]")
    public String getUById(String id) {

        return "a";
    }

    @CacheClear(key = "$$")
    public String findUByName() {
        return "t";
    }

    @Cacheabel(key = "#root.args[0]")
    public Teacher getTeacher(String name) {
        Teacher teacher = new Teacher();
        teacher.setName(name);
        return teacher;
    }

    @Cacheabel(key = "dd")
    public List<Teacher> getTList(String name) {
        return new ArrayList<Teacher>(){
            {
                Teacher teacher = new Teacher();
                teacher.setName(name);
                add(teacher);
            }
        };


    }

    public void expression(List<Teacher> teachers) {

    }
}
