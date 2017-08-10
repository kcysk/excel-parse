package net.zdsoft.dataimport.freemarker;

import freemarker.ext.beans.HashAdapter;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateModelException;
import net.zdsoft.dataimport.annotation.Exporter;

import java.util.Collection;
import java.util.Map;

/**
 * @author shenke
 * @since 17-8-5 下午9:53
 */
public class ViewDataFreemarkerUtils {

    public static String parseTemplate(Object object) throws TemplateModelException {
        if ( object instanceof SimpleHash ) {
            SimpleHash simpleHash = (SimpleHash)object;
            Map map = simpleHash.toMap();
            Collection values = map.values();
            StringBuilder td = new StringBuilder();
            for (Object value : values) {
                td.append("<td>").append(value.toString()).append("</td>");
            }
            return td.toString();
        } else if ( object instanceof HashAdapter ) {
            HashAdapter hashAdapter = (HashAdapter)object;
            StringBuilder td = new StringBuilder();
            for (Object o : hashAdapter.entrySet()) {
                if( ((Map.Entry)o).getKey().equals(Exporter.CHECKED) ){
                    continue;
                }
                td.append("<td>").append(((Map.Entry)o).getValue()).append("</td>");
            }
            return td.toString();
        }
        return "";
    }

}
