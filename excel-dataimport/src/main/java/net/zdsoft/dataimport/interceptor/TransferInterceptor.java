package net.zdsoft.dataimport.interceptor;

import net.zdsoft.dataimport.QImportEntity;
import net.zdsoft.dataimport.core.DataCell;
import net.zdsoft.dataimport.core.DataSheet;

import java.util.List;

/**
 * 转换java对象拦截器
 * @author shenke
 * @since 2017.08.08
 */
public interface TransferInterceptor {

    void preTransfer(List<DataCell> dataCellList, DataSheet dataSheet, Class<? extends QImportEntity> tClass);

    void afterTransfer(Object t, List<DataCell> dataCellList);
}
