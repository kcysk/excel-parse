package net.zdsoft.dataimport.core;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @since 2017.07.31
 */
public class DataExcel {

    @Getter @Setter private String excelName;
    @Getter @Setter private List<DataSheet> dataSheetList;

    public DataExcel() {
        this.dataSheetList = new ArrayList<>();
    }

    public void addDataSheet(DataSheet dataSheet) {
        this.dataSheetList.add(dataSheet);
    }

    public void addDataSheetIfNotNull(DataSheet dataSheet) {
        if ( dataSheet != null ) {
            addDataSheet(dataSheet);
        }
    }
}
