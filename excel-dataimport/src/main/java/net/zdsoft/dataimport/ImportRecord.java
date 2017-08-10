package net.zdsoft.dataimport;

import lombok.Getter;
import lombok.Setter;

/**
 * 导入记录
 * @author shenke
 * @since 2017.08.10
 */
public class ImportRecord {

    @Getter @Setter private String fileName;
    @Getter @Setter private long creationTime;
    @Getter @Setter private String originFilename;
    @Getter @Setter private boolean done;
    @Getter @Setter private String cacheId;

}
