package net.zdsoft.dataimport;

import net.zdsoft.dataimport.parse.ExcelParserFactory;
import net.zdsoft.dataimport.parse.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import net.zdsoft.dataimport.annotation.ExcelCell;
import net.zdsoft.dataimport.annotation.Valid;
import net.zdsoft.dataimport.core.DataCell;
import net.zdsoft.dataimport.core.DataExcel;
import net.zdsoft.dataimport.core.DataRow;
import net.zdsoft.dataimport.core.DataSheet;
import net.zdsoft.dataimport.exception.ImportFieldException;
import net.zdsoft.dataimport.process.ExcutorHolder;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static net.zdsoft.dataimport.BeanUtils.*;

/**
 * @author shenke
 * @since 2017.07.31
 */
public abstract class AbstractImportBiz<T extends QImportEntity> {

    private static Map<String,Field> fieldMap;

    @Autowired private ExcutorHolder excutorHolder;

    @PostConstruct
    private void initAnnotationCache() {
        Class<T> tClass = getFirstGenericityType(this.getClass());
        List<Field> fields = getAnnotationWithAnnotation(tClass, ExcelCell.class);
        fieldMap = fields.stream().collect(Collectors.toMap(field -> field.getAnnotation(ExcelCell.class).header(),field->field));
    }

    Future<Reply<T>> execute(String filePath) {
        return excutorHolder.run(() -> {
            Reply<T> reply = null;
            Parser parser = ExcelParserFactory.getParser();
            if ( filePath == null ) {
                reply = Reply.<T>buildGlobeErrorReply("文件路径不合法");
                return reply;
            }
            File excel = new File(filePath);
            if ( !excel.exists() ) {
                reply = Reply.<T>buildGlobeErrorReply("excel不存在");
                return reply;
            }
            try {
                DataExcel dataExcel = parser.parse(new FileInputStream(excel));
                List<DataSheet> dataSheetList = dataExcel.getDataSheetList();
                if ( dataSheetList.isEmpty() ) {
                    return Reply.buildGlobeErrorReply("导入Excel不包含数据");
                }
                List<T> objects = new ArrayList<>();
                List<T> errorObjects = new ArrayList<>();
                for (DataSheet dataSheet : dataSheetList) {
                    objects.addAll(transferTo(dataSheet));
                }
                objects.forEach(e->{
                    if ( e.createQImportError().isHasError() ) {
                        errorObjects.add(e);
                    } else {
                        objects.add(e);
                    }
                });

            } catch (FileNotFoundException e) {
                reply = Reply.buildGlobeErrorReply("excel不存在");
            }
            return reply;
        });
    }

    /**
     * 转换为java对象，所有入库之前校验在此完成，记录错误信息页面展示</br>
     * @see QImportEntity#createQImportError()
     * @see QImportError
     *
     * @param dataSheet
     * @return
     */
    public List<T> transferTo(DataSheet dataSheet) {
        List<T> os = new ArrayList<T>(dataSheet.getDataRowList().size());
        for (DataRow dataRow : dataSheet.getDataRowList()) {
            T t = newBean(getFirstGenericityType(this.getClass()));
            QImportError qImportError = t.createQImportError();
            for (DataCell dataCell : dataRow.getDataCellList()) {
                setProperty(t, dataCell.getFieldName(), dataCell.getData());
                checkForValid(t, dataCell.getHeader(), qImportError);
                qImportError.value(dataCell.getFieldName(), dataCell.getData());
            }
            os.add(t);
        }

        verify(os);
        return os;
    }

    //valid 校验
    void checkForValid(T t, String header, QImportError qImportError) {

        Field field = fieldMap.get(header);

        ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
        Valid valid = excelCell.valid();
        //空校验
        if ( valid.notNull() && getFiledValue(t, field) == null ) {
            qImportError.error(field.getName(), "[" + excelCell.header() + "]不能为空");
            return ;
        }

        //其他校验

        //用户扩展校验
    }

    /**
     * 业务校验
     * @param javaObjects
     */
    protected abstract void verify(List<T> javaObjects);

    /**
     * 执行业务操作(保存等)，子类必须重写该方法
     *
     * @param javaObjects
     * @throws ImportFieldException
     */
    public abstract void importData(List<T> javaObjects) throws ImportFieldException;
}
