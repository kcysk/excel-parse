package net.zdsoft.dataimport;

import net.zdsoft.UserImportBiz;
import net.zdsoft.dataimport.biz.AbstractImportAction;
import net.zdsoft.dataimport.biz.AbstractImportBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author shenke
 * @since 2017.08.07
 */
@Controller
@RequestMapping("/user")
public class UserImportAction extends AbstractImportAction {

    @Autowired private UserImportBiz userImportBiz;

    @Override
    protected AbstractImportBiz getImportBiz() {
        return userImportBiz;
    }

    @Override
    protected boolean hasTask() {
        return false;
    }
}
