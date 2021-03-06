package net.zdsoft.dataimport.biz;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shenke
 * @since 17-8-6 下午7:43
 */
public class JSONResponse{

    @Getter private String msg;
    @Getter private boolean success;
    @Getter private Object businessValue;
    @Getter @Setter private int importStateCode;

    public JSONResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public JSONResponse setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public JSONResponse setBusinessValue(Object value) {
        this.businessValue = value;
        return this;
    }

    public JSONResponse setImportStateCode(int importStateCode) {
        this.importStateCode = importStateCode;
        return this;
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
