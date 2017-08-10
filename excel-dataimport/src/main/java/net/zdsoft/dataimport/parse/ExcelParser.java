package net.zdsoft.dataimport.parse;

import net.zdsoft.dataimport.core.DataCell;
import net.zdsoft.dataimport.core.DataExcel;
import net.zdsoft.dataimport.core.DataRow;
import net.zdsoft.dataimport.core.DataSheet;
import net.zdsoft.dataimport.exception.ImportParseException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shenke
 * @since 2017.07.31
 */
public class ExcelParser implements Parser {

    private static Logger logger = LoggerFactory.getLogger(ExcelParser.class);

    @Override
    public DataExcel parse(InputStream inputStream) throws ImportParseException {
        long parseStart = System.currentTimeMillis();
        Workbook workbook = read2Workbook(inputStream);
        if ( workbook == null ) {
            return null;
        }
        int sheetNumber = workbook.getNumberOfSheets();
        DataExcel dataExcel = new DataExcel();
        for ( int i=0; i<sheetNumber; i++ ) {
            DataSheet dataSheet = parseForDataSheet(workbook.getSheetAt(i));
            dataExcel.addDataSheetIfNotNull(dataSheet);
        }
        logger.debug("解析Excel耗时：{}s", (System.currentTimeMillis() - parseStart)/1000);
        return dataExcel;
    }

    private Workbook read2Workbook(InputStream inputStream) throws ImportParseException {
        Workbook workbook = null;
        try {
            workbook = new HSSFWorkbook(inputStream);
        } catch (Exception e){
            try {
                workbook = new XSSFWorkbook(inputStream);
            } catch (IOException e1) {
                //logger.error("解析");
                throw new ImportParseException(e);
            }
        }
        return workbook;
    }

    public DataSheet parseForDataSheet(Sheet sheet) {
        if ( sheet == null ) {
            return null;
        }
        DataSheet dataSheet = new DataSheet();
        int rowNumber = sheet.getPhysicalNumberOfRows();

        for ( int i=0; i<rowNumber; i++) {
            DataRow dataRow = parseForDataRow(sheet.getRow(i), i == 0 ? null : dataSheet.getHeaders().toArray(new String[0]) );
            if ( i == 0) {
                List<String> headers = new ArrayList<String>();
                for (DataCell dataCell : dataRow.getDataCellList()) {
                    logger.debug("header-{},:[{}]",i,dataCell.getData());
                    headers.add(dataCell.getData().toString());
                }
                dataSheet.setHeaders(headers);
            } else {
                dataSheet.addDataRowIfNotEmpty(dataRow);
            }
        }
        return dataSheet;
    }

    public DataRow parseForDataRow(Row row, String[] headers) {
        if ( row == null ) {
            return null;
        }
        int cellNumber = row.getPhysicalNumberOfCells();
        boolean emptyRow = Boolean.TRUE;
        DataRow dataRow = new DataRow();
        for (int i=0; i< cellNumber; i++ ) {
            Cell cell = row.getCell(i);
            DataCell dataCell = parseForDataCell(cell);
            if ( dataCell == null ) {
                break;
            }
            if ( dataCell.getData() != null ) {
                emptyRow = Boolean.FALSE;
            }
            if ( headers != null && headers.length > 0 ) {
                dataCell.setHeader(headers[i]);
            }
            dataRow.addDataCellIfNotnull(dataCell);
        }
        return emptyRow ? null : dataRow;
    }

    public DataCell parseForDataCell(Cell cell) {
        if ( cell == null ) {
            return null;
        }
        DataFormatter dataFormatter = new DataFormatter();
        DataCell dataCell = new DataCell();
        switch ( cell.getCellType() ) {
            case Cell.CELL_TYPE_STRING :
                dataCell.setData(cell.getStringCellValue());
                dataCell.setType(String.class);
                break;
            case Cell.CELL_TYPE_NUMERIC:
                Object value;
                if ( DateUtil.isCellDateFormatted(cell) ) {
                    value = cell.getDateCellValue();
                    dataCell.setType(Date.class);
                } else {
                    value = dataFormatter.formatCellValue(cell);
                    dataCell.setType(Number.class);
                }
                dataCell.setData(value);
                break;
            case Cell.CELL_TYPE_BLANK:
            case Cell.CELL_TYPE_ERROR:
                dataCell.setType(Void.class);
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                dataCell.setData(cell.getBooleanCellValue());
                dataCell.setType(Boolean.class);
                break;
            case Cell.CELL_TYPE_FORMULA:
                dataCell.setData(cell.getNumericCellValue());
                dataCell.setType(Number.class);
                break;
        }
        return dataCell;
    }

    private static final List<String> formarts = new ArrayList<String>(){
        {
            add("yyyy-MM");
            add("yyyy-MM-dd");
            add("yyyy-MM-dd hh:mm");
            add("yyyy-MM-dd hh:mm:ss");
        }
    };
    private Object format(Date date) {
        return DateFormatUtils.format(date, "yyyy-MM-dd");
    }

}
