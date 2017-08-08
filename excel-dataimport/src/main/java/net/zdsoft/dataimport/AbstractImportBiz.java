package net.zdsoft.dataimport;

import net.zdsoft.dataimport.parse.ExcelParserFactory;
import net.zdsoft.dataimport.parse.Parser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import net.zdsoft.dataimport.annotation.ExcelCell;
import net.zdsoft.dataimport.annotation.Valid;
import net.zdsoft.dataimport.core.DataCell;
import net.zdsoft.dataimport.core.DataExcel;
import net.zdsoft.dataimport.core.DataRow;
import net.zdsoft.dataimport.core.DataSheet;
import net.zdsoft.dataimport.exception.ImportFieldException;
import net.zdsoft.dataimport.process.ExcutorHolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
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
public abstract class AbstractImportBiz<T extends QImportEntity>  implements InitializingBean{

    private static Map<String,Field> classCache;

    @Autowired private ExcutorHolder excutorHolder;

    @Override
    public void afterPropertiesSet() throws Exception {
        initAnnotationCache();
    }

    private void initAnnotationCache() {
        Class<T> tClass = getFirstGenericityType(this.getClass());
        List<Field> fields = getAnnotationWithAnnotation(tClass, ExcelCell.class);
        classCache = fields.stream().collect(Collectors.toMap(field -> field.getAnnotation(ExcelCell.class).header(), field->field));
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
                reply = new Reply<T>();
                reply.setErrorObjects(errorObjects);
                reply.setJavaObjects(objects);
                reply.setMessage("解析完成");

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
            initDadaCell(dataRow.getDataCellList());
            T t = newBean(getFirstGenericityType(this.getClass()));
            QImportError qImportError = t.createQImportError();
            for (DataCell dataCell : dataRow.getDataCellList()) {
                if (!setProperty(t, dataCell.getFieldName(), dataCell.getData()) ){
                    qImportError.error(dataCell.getFieldName(), "赋值转换失败");
                }
                qImportError.value(dataCell.getFieldName(), dataCell.getData());
            }
            os.add(t);
        }
        return os;
    }

    private void initDadaCell(List<DataCell> dataCells) {
        dataCells.forEach(e->{
            e.setFieldName(classCache.get(e.getHeader()).getName());
        });
    }

    //valid 校验
    public void checkForValid(T t, String header) {

        Field field = classCache.get(header);

        ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
        Valid valid = field.getAnnotation(Valid.class);
        if ( valid == null || t.createQImportError().isHasError() ) {
            return ;
        }
        Object value = getFiledValue(t, field);
        //空校验
        if (valid.notNull() && value == null ) {
            t.createQImportError().error(field.getName(), "[" + excelCell.header() + "] 不能为空");
            return ;
        }

        if ( field.getType().equals(String.class) ) {
            if ( StringUtils.length(String.class.cast(value)) > valid.length() ) {
                t.createQImportError().error(field.getName(), error("[{}] 超过最大长度 [{}]", excelCell.header(), valid.length() ) );
            }
        }


        //其他校验

        //用户扩展校验
    }

    protected String error(String msg, Object ... values) {
        for (Object value : values) {
            msg = msg.replaceFirst("\\{\\}", String.valueOf(value));
        }
        return msg;
    }

    public boolean verifyEveryHeader(Object value, String header, QImportError error) {
        Field field = classCache.get(header);

        return true;
    }

    protected <A extends Annotation> A getAnnotationByHeader(String header, Class<A> annotation) {
        Field field = classCache.get(header);
        return field.getAnnotation(annotation);
    }

    /**
     *  单值业务校验
     * @param t
     * @return
     */
    protected abstract void verify(T t);

    /**
     * 业务校验
     * @param os
     */
    protected abstract void verify(List<T> os);

    /**
     * 执行业务操作(保存等)，子类必须重写该方法
     *
     * @param os
     * @throws ImportFieldException
     */
    public abstract void importData(List<T> os) throws ImportFieldException;

}
