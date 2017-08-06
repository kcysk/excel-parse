package net.zdsoft.dataimport.exception;

import lombok.Getter;

/**
 * 处理字段异常
 * @author shenke
 * @since 2017.08.01
 */
public class ImportFieldException extends Exception {

    @Getter private String field;
    @Getter private int index;

    public ImportFieldException() {
        super();
    }

    public ImportFieldException(String message) {
        super(message);
    }

    public ImportFieldException(String message, String field, int index) {
        this(message);
        this.field = field;
        this.index = index;
    }
}
