package net.zdsoft.dataimport;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 导入记录
 * @author shenke
 * @since 2017.08.10
 */
public class ImportRecord {

    @Getter @Setter private String fileName;
    @Getter @Setter private Date creationTime;
    @Getter @Setter private String originFilename;
    @Getter @Setter private String cacheId;
    @Getter @Setter private int stateCode;

    @Getter @Setter private String stateMsg; //状态显示

    @Getter @Setter private long redisIndex;
}
