package net.zdsoft.dataimport;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * @author shenke
 * @since 17-8-6 下午7:43
 */
public class JSONResponse{

    @Getter private String msg;
    @Getter private boolean success;
    @Getter private Object businessValue;

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

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
