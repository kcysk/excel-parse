package net.zdsoft.dataimport;

import net.zdsoft.dataimport.exception.ImportBusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenke
 * @since 2017.08.09
 */
@Service
public class UserImportBiz extends AbstractImportBiz<User> {

    @Override
    public void importData(List<User> os) throws ImportBusinessException {

    }
}
