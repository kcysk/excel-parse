package net.zdsoft.dataimport;

import lombok.Getter;
import lombok.Setter;
import net.zdsoft.dataimport.annotation.ExcelCell;
import net.zdsoft.dataimport.annotation.Valid;
import net.zdsoft.dataimport.biz.QImportEntity;

/**
 * @author shenke
 * @since 17-8-6 下午9:51
 */
public class User implements QImportEntity<UserError> {

    @ExcelCell(header = "姓名")
    @Valid(notNull = true)
    @Getter @Setter
    private String userName;

    @ExcelCell(header = "性别")
    @Getter @Setter
    private Integer sex;

    public User() {

    }

    @Override
    public UserError createQImportError() {
        return UserError.userError;
    }
}
