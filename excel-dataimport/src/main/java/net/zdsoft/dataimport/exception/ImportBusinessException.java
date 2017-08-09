package net.zdsoft.dataimport.exception;

import lombok.Getter;

/**
 * 处理字段异常
 * @author shenke
 * @since 2017.08.01
 */
public class ImportBusinessException extends Exception {

    @Getter private String field;
    @Getter private int index;

    public ImportBusinessException() {
        super();
    }

    public ImportBusinessException(String message) {
        super(message);
    }

    public ImportBusinessException(String message, String field, int index) {
        this(message);
        this.field = field;
        this.index = index;
    }
}
