package net.zdsoft.dataimport;

import net.zdsoft.dataimport.core.DataCell;
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
    protected void verify(User user) {

    }

    @Override
    protected void verify(List<User> os) {

    }

    @Override
    public void importData(List<User> os) throws ImportFieldException {

    }
}
