package net.zdsoft.dataimport;

import lombok.Getter;
import lombok.Setter;
import net.zdsoft.dataimport.annotation.ExcelCell;
import net.zdsoft.dataimport.annotation.Exporter;
import net.zdsoft.dataimport.annotation.Valid;
import net.zdsoft.dataimport.biz.QImportEntity;

import java.util.Date;

/**
 * @author shenke
 * @since 17-8-6 下午9:51
 */
public class User implements QImportEntity<UserError> {

    @Getter @Setter
    @Valid(notNull = true)
    @ExcelCell(header = "姓名")
    @Exporter( displayOrder = 1, description = "姓名", example = "张三")
    private String userName;

    @Getter @Setter
    @ExcelCell(header = "性别")
    @Exporter(displayOrder = 2, description = "性别", example = "男")
    private Integer sex;

    @Getter @Setter
    @ExcelCell(header = "出生日期")
    @Exporter(displayOrder = 3, description = "出生日期", defaultChecked = false, example = "1998-02-01")
    private Date birthday;

    public User() {

    }

    @Override
    public UserError createQImportError() {
        return UserError.userError;
    }
}
