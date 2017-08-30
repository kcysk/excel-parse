package net.zdsoft.cache.ap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author shenke
 * @since 2017.08.30
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheAspectjTest {

    @Autowired
    private UDao uDao;

    @Test
    public void getUById() {
        uDao.getUById("tt");
    }
}
