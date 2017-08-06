package net.zdsoft.dataimport;

/**
 * @author shenke
 * @since 17-8-6 下午9:54
 */
public class UserError extends QImportError {

    public static UserError userError = new UserError();

    private UserError() {

    }

    public ImportFieldError userName = createFieldError("userName");

    public ImportFieldError sex = createFieldError("sex");
}
