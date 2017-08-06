package net.zdsoft.dataimport;

import net.zdsoft.dataimport.exception.ImportFieldException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenke
 * @since 17-8-6 下午9:56
 */
@Service
public class UserImportBiz extends AbstractImportBiz<User> {

    @Override
    protected void verify(List<User> javaObjects) {

    }

    @Override
    public void importData(List<User> javaObjects) throws ImportFieldException {
        System.out.println("入库");
    }
}
