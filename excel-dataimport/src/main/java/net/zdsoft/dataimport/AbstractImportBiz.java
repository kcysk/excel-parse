package net.zdsoft.dataimport;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.dataimport.annotation.Exporter;
import net.zdsoft.dataimport.biz.ImportState;
import net.zdsoft.dataimport.biz.QImportEntity;
import net.zdsoft.dataimport.biz.QImportError;
import net.zdsoft.dataimport.biz.Reply;
import net.zdsoft.dataimport.exception.ImportParseException;
import net.zdsoft.dataimport.verify.AnnotationVerify;
import net.zdsoft.dataimport.parse.ExcelParserFactory;
import net.zdsoft.dataimport.parse.Parser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import net.zdsoft.dataimport.annotation.ExcelCell;
import net.zdsoft.dataimport.annotation.Valid;
import net.zdsoft.dataimport.core.DataCell;
import net.zdsoft.dataimport.core.DataExcel;
import net.zdsoft.dataimport.core.DataRow;
import net.zdsoft.dataimport.core.DataSheet;
import net.zdsoft.dataimport.exception.ImportBusinessException;
import net.zdsoft.dataimport.process.ExcutorHolder;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.zdsoft.dataimport.BeanUtils.*;

/**
 * @author shenke
 * @since 2017.07.31
 */
public abstract class AbstractImportBiz<T extends QImportEntity>  implements InitializingBean{

    protected Logger logger = LoggerFactory.getLogger(AbstractImportBiz.class);

    private Map<String,Field> classCache;

    @Autowired private ExcutorHolder excutorHolder;
    @Autowired private ApplicationContext applicationContext;

    private List<AnnotationVerify> annotationVerifies = Lists.newArrayList();

    private AnnotationVerify<Valid> validAnnotationVerify;

    @Override
    public void afterPropertiesSet() throws Exception {
        initAnnotationCache();
        initAnnotationVerifyInterceptors();
    }

    public void setValidAnnotationVerify(AnnotationVerify<Valid> validAnnotationVerify) {
        this.validAnnotationVerify = validAnnotationVerify;
    }

    private void initAnnotationCache() {
        Class<T> tClass = getFirstGenericityType(this.getClass());
        List<Field> fields = getAnnotationWithAnnotation(tClass, ExcelCell.class);
        classCache = fields.stream().collect(Collectors.toMap(field -> field.getAnnotation(ExcelCell.class).header(), field->field));
    }

    private void initAnnotationVerifyInterceptors() {
        Map<String, AnnotationVerify> annotationVerifyInterceptorMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(applicationContext,AnnotationVerify.class, Boolean.TRUE, Boolean.TRUE);
        if ( annotationVerifyInterceptorMap != null && annotationVerifyInterceptorMap.size() > 0 ) {
            annotationVerifies.addAll(annotationVerifyInterceptorMap.values());
        }
        //默认校验
        if ( this.validAnnotationVerify == null ) {
            annotationVerifies.add(new ValidAnnotationVerify());
        }
        annotationVerifies.sort(Comparator.comparingInt(AnnotationVerify::order));
    }

    private void initDadaCell(List<DataCell> dataCells) {
        dataCells.forEach(e->{
            e.setFieldName(classCache.get(e.getHeader()).getName());
        });
    }

    Reply<T> execute(String filePath, String id) {
        final Reply reply = new Reply();
        reply.setId(id);
        excutorHolder.run(() -> {
            reply.setImportState(ImportState.ING);
            Parser parser = ExcelParserFactory.getParser();
            if ( filePath == null ) {
                reply.setGlobeError("文件路径不合法");
                logger.error("filePath 不能为空");
                return reply;
            }
            File excel = new File(filePath);
            if ( !excel.exists() ) {
                logger.error("文件不存在 {}", filePath);
                reply.setGlobeError("excel不存在");
                return reply;
            }
            long parseStart = System.currentTimeMillis();
            try {
                DataExcel dataExcel = parser.parse(new FileInputStream(excel), reply);
                List<DataSheet> dataSheetList = dataExcel.getDataSheetList();
                if ( dataSheetList.isEmpty() ) {
                    logger.error("导入Excle中无数据");
                    reply.setGlobeError("导入Excel不包含数据");
                    return reply;
                }
                List<T> objects = new ArrayList<>();
                List<T> correctObjects = new ArrayList<>();
                List<T> errorObjects = new ArrayList<>();
                for (DataSheet dataSheet : dataSheetList) {
                    reply.notifyClient(ImportState.ING.getCode(), "正在进行数据转换和校验");
                    objects.addAll(transferTo(dataSheet));
                    reply.setHeaders(Lists.newArrayList(dataSheet.getHeaders()));
                }
                objects.forEach(e->{
                    if ( e.createQImportError().isHasError() ) {
                        errorObjects.add(e);
                    } else {
                        correctObjects.add(e);
                    }
                });

                reply.setErrorObjects(errorObjects);
                reply.setJavaObjects(correctObjects);
                logger.debug("解析转换耗时：{}s", (System.currentTimeMillis() - parseStart)/1000);

            } catch (FileNotFoundException e) {
                logger.error("FileNotFound, {}, {}", e.getMessage(), filePath);
                reply.setGlobeError("excel不存在");
            } catch (ImportParseException e) {
                logger.error("文件解析异常 {}", e.getMessage());
                reply.setGlobeError(e.getMessage());
            } finally {
                if ( reply.getGlobeError() != null ) {
                    reply.setImportState(ImportState.DONE);
                    reply.notifyClient(ImportState.DONE.getCode(), "数据解析教研完成");
                }
            }
            //更新生成时间，延长存活时间
            reply.setCreationTime(System.currentTimeMillis());
            return reply;
        });
        return reply;
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
                checkForValid(t,dataCell.getHeader());
            }
            os.add(t);
        }
        return os;
    }

    /**
     * 注解校验
     * @param t
     * @param header
     */
    void checkForValid(T t, String header) {
        Field field = classCache.get(header);
        Object value = getFiledValue(t, field);
        //扩展注解校验 调用注解扩展
        for (AnnotationVerify e : annotationVerifies) {
            boolean ok = e.verify(value, getAnnotationByHeader(header, getFirstSuperInterfaceGenericityType(e.getClass())), t.createQImportError(), field );
            if ( ok ) {
                break;
            }
        }
    }

    /**
     * 导出模板
     * @return
     */
    Workbook exportTemplate(List<String> headers) {
        Class<T> tClass = getFirstGenericityType(this.getClass());
        List<Field> fields = Arrays.stream(tClass.getDeclaredFields())
                .filter(e->e.getAnnotation(Exporter.class)!=null).filter(e->{
                    return headers != null ? headers.contains(e.getAnnotation(ExcelCell.class).header()) : Boolean.TRUE;
                })
                .collect(Collectors.toList());

        fields.sort(Comparator.comparingInt(e1 -> e1.getAnnotation(Exporter.class).displayOrder()));

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "test");
        Row row = sheet.createRow(0);
        //CellRangeAddress rangeAddress = new CellRangeAddress(0, 0, 0 , exporters.size() - 1);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints( (short) 10);
        font.setBold(Boolean.TRUE);

        cellStyle.setFont(font);

        fields.forEach(e->{
            Cell cell = row.createCell(fields.indexOf(e));
            cell.setCellType(CellType.STRING);
            cell.setCellValue(e.getAnnotation(ExcelCell.class).header());
            cell.setCellStyle(cellStyle);
        });
        return workbook;
    }

    List<JSONObject> templates() {
        Class<T> tClass = getFirstGenericityType(this.getClass());
        List<Field> fields = Arrays.stream(tClass.getDeclaredFields())
                .filter(e->e.getAnnotation(Exporter.class)!=null)
                .collect(Collectors.toList());
        List<JSONObject> templates = Lists.newArrayList();
        fields.forEach(e->{
            JSONObject data = new JSONObject();
            Exporter exporter = e.getAnnotation(Exporter.class);
            ExcelCell excelCell = e.getAnnotation(ExcelCell.class);
            data.put(Exporter.HEADER, excelCell.header());
            data.put(Exporter.CHECKED, exporter.defaultChecked());
            String type = e.getType().equals(Date.class) ? "日期型 ": e.getType().equals(String.class) ? "字符型 " : "数字型 ";
            data.put(Exporter.DESCRIPTION, type + exporter.description());
            data.put(Exporter.EXAMPLE, exporter.example());
            templates.add(data);
        });
        return templates;
    }

    private String error(String msg, Object ... values) {
        for (Object value : values) {
            msg = msg.replaceFirst("\\{\\}", String.valueOf(value));
        }
        return msg;
    }

    protected <A extends Annotation> A getAnnotationByHeader(String header, Class<A> annotation) {
        Field field = classCache.get(header);
        return field.getAnnotation(annotation);
    }

    /**
     * 执行业务操作(保存等)，子类必须重写该方法
     *
     * @param os
     * @throws ImportBusinessException
     */
    public abstract void importData(List<T> os) throws ImportBusinessException;

    class ValidAnnotationVerify implements AnnotationVerify<Valid> {

        @Override
        public boolean verify(Object value, Valid valid, QImportError error, Field field) {

            ExcelCell excelCell = field.getAnnotation(ExcelCell.class);
            if ( valid == null || error.isHasError() ) {
                return Boolean.TRUE;
            }
            //空校验
            if ( valid.notNull() && value == null ) {
                logger.error("[{}]不能为空", excelCell.header());
                error.error(field.getName(), "[" + excelCell.header() + "] 不能为空");
                return Boolean.TRUE;
            }
            if ( value == null ) {
                return Boolean.TRUE;
            }

            if ( field.getType().equals(String.class) ) {
                if ( StringUtils.length(String.class.cast(value)) > valid.length() ) {
                    logger.error("[{}:{}] 超过最大长度 [{}]", excelCell.header(), value, valid.length() );
                    error.error(field.getName(), error("[{}] 超过最大长度 [{}]", excelCell.header(), valid.length() ) );
                }
            } else if ( isNumer(field.getType()) ){
                Number number = NumberUtils.toDouble(value.toString());
                if ( valid.nonNegative() && number.doubleValue() < 0 ) {
                    logger.error("[{}] 不能为负数 [{}]", excelCell.header(), value);
                    error.error(field.getName(), error("[{}] 不能为负数 [{}]", excelCell.header(), value) );
                }
                //小数校验
                if ( field.getType().equals(Double.class) || field.getType().equals(Float.class) ) {
                    String pre = value.toString().substring(0,value.toString().indexOf("."));
                    String dec = value.toString().substring(value.toString().indexOf(".") + 1);
                    if ( pre.length() > valid.precision() ) {
                        logger.error("[{}] 整数位超过最大值[{}]位", excelCell.header(), valid.precision());
                        error.error(field.getName(), error("[{}] 整数位超过最大值[{}]位", excelCell.header(), valid.precision() ) );
                    }
                    if ( dec.length() > valid.decimal() ) {
                        logger.error("[{}] 小数位超过长度[{}]位", excelCell.header(), valid.decimal());
                        error.error(field.getName(), error("[{}] 小数位超过长度[{}]位", excelCell.header(), valid.decimal() ) );
                    }
                }
            }
            return Boolean.TRUE;
        }

        @Override
        public int order() {
            return 1;
        }
    }
}
