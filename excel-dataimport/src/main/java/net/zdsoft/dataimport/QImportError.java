package net.zdsoft.dataimport;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenke
 * @since 2017.08.01
 */
public abstract class QImportError {

    private Map<String,ImportFieldError> errorFiledCache = new HashMap<>();

    @Getter private volatile boolean hasError;

    protected ImportFieldError createFieldError(String name) {
        return new ImportFieldError(name);
    }

    protected QImportError error(final String name, String error) {
        initCache();
        hasError = Boolean.TRUE;
        ImportFieldError importFieldError = errorFiledCache.get(name);
        importFieldError.addError(error);
        return this;
    }

    protected void value(String name, Object value) {
        initCache();
        ImportFieldError importFieldError = errorFiledCache.get(name);
        importFieldError.setValue(value);
    }

    private void initCache() {
        if ( errorFiledCache.isEmpty() ) {
            Field[] fields = this.getClass().getDeclaredFields();
            Arrays.stream(fields).forEach(e->{
                try {
                    errorFiledCache.put(e.getName(), (ImportFieldError) BeanUtils.getProperty(this, e));
                } catch (Exception e1) {

                }
            });
        }
    }

    public class ImportFieldError {

        @Getter private List<String> errors;
        @Getter private String name;
        @Setter
        @Getter private Object value;   //该字段对应的Excel中的值

        public ImportFieldError(String name) {
            this.errors = new ArrayList<>();
            this.name = name;
        }

        public void addError(String error) {
            setHasError(Boolean.TRUE);
            this.errors.add(error);
        }
    }

    private void setHasError(boolean error) {
        hasError = error;
    }
}
