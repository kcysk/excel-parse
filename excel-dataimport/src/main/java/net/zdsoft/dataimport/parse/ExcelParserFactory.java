package net.zdsoft.dataimport.parse;

/**
 * @author shenke
 * @since 2017.07.31
 */
public class ExcelParserFactory {

    public static Parser getParser() {
        return new ExcelParser();
    }
}
