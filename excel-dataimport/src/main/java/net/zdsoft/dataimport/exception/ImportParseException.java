package net.zdsoft.dataimport.exception;

/**
 * @author shenke
 * @since 2017.08.09
 */
public class ImportParseException extends Exception {

    public ImportParseException() {
        super();
    }

    public ImportParseException(String message) {
        super(message);
    }

    public ImportParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImportParseException(Throwable cause) {
        super(cause);
    }
}
