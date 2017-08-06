package net.zdsoft.dataimport.core;

import lombok.Getter;
import lombok.Setter;
import net.zdsoft.dataimport.annotation.ExcelCell;

/**
 * @author shenke
 * @since 2017.07.31
 */
public class DataCell extends Node<ExcelCell> {

    @Getter @Setter private Object data;
    @Getter @Setter private Class type;
    @Getter @Setter private String fieldName;
    @Getter @Setter private String header;

}
