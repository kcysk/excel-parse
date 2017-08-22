package net.zdsoft;

import net.zdsoft.dataimport.biz.AbstractImportBiz;
import net.zdsoft.dataimport.User;
import net.zdsoft.dataimport.exception.ImportBusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenke
 * @since 17-8-6 下午9:56
 */
@Service
public class UserImportBiz extends AbstractImportBiz<User> {

    @Override
    public void importData(List<User> os) throws ImportBusinessException {

    }

    @Override
    public boolean globeVerify(List<User> os) {
        return false;
    }
}
