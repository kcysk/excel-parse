package net.zdsoft.dataimport.core;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenke
 * @since 2017.07.31
 */
public class DataSheet {

    @Getter @Setter private String sheetName;
    @Getter @Setter private String title;               // maybe not exists
    @Getter @Setter private List<String> headers;
    @Getter @Setter private List<DataRow> dataRowList;

    public DataSheet() {
        this.dataRowList = new ArrayList<>();
    }

    public void addDataRowIfNotEmpty(DataRow dataRow) {

        if ( dataRow != null && dataRow.getDataCellList() != null && !dataRow.getDataCellList().isEmpty() ) {
            this.dataRowList.add(dataRow);
        }
    }

}
