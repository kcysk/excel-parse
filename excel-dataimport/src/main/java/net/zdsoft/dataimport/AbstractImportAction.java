package net.zdsoft.dataimport;

import net.zdsoft.dataimport.cache.ReplyCache;
import net.zdsoft.dataimport.process.ExcutorHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * @author shenke
 * @since 2017.08.01
 */
public abstract class AbstractImportAction<T extends QImportEntity> {

    protected Logger logger = LoggerFactory.getLogger(AbstractImportAction.class);

    @Autowired protected ExcutorHolder excutorHolder;

    @RequestMapping(value = "import/index")
    public Object importIndex() {

        return "";
    }

    @ResponseBody
    @RequestMapping(value = "import/upload")
    public Object importUpload(CommonsMultipartFile excel) {
        //任务模式
        if ( hasTask() ) {

        }
        //非任务模式
        else {
            //将文件拷贝到指定目录下 FILE_PATH/temp/uuid.xls or xlsx
            String tempDir = "/temp/";
            try {
                excel.transferTo(new File(""));
            } catch (IOException e) {
                return error("导入文件获取失败，IO异常");
            }

            //执行非任务导入
            Future future = getImportBiz().execute(tempDir);

            //reply
            String uuid = UUID.randomUUID().toString();
            ReplyCache.put(uuid, future);
            JSONResponse response = success("导入成功");
            response.setBusinessValue(uuid);
            return response;
        }
        return "";
    }

    @RequestMapping(value = "import/update")
    public Object updateExcelData() {
        //校验 原错误信息和所有原Excel的值丢弃
        //getImportBiz().checkForValid();
        //getImportBiz().verify();

        return null;
    }

    @RequestMapping(value = "import/download")
    public Object importDownload() {

        return "";
    }

    @RequestMapping(value = "import/viewData")
    public Object importViewData(@RequestParam("cacheId") String cacheId) {
        Future<Reply<T>> future = ReplyCache.get(cacheId);
        ModelAndView mv = createMV("/dataImport/viewData");
        if ( future.isDone() ) {

        }
        try {
            Reply<T> reply = future.get();
            mv.addObject("errorObjects", reply.getErrorObjects());
            mv.addObject("javaObjects", reply.getJavaObjects());
            mv.addObject("reply", reply);
        } catch (Exception e) {
            return "error";
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "import/cancel")
    public Object importCancel(@RequestParam("cacheId") String cacheId) {
        Future<Reply> replyFuture = ReplyCache.get(cacheId);
        try {
            if ( replyFuture != null && !replyFuture.isCancelled()) {
                boolean success = replyFuture.cancel(Boolean.FALSE);
                return success ? "success" : "error";
            } else {
            }
        } catch (Exception e) {
            logger.error("cancel import error {}", e);
        } finally {
            ReplyCache.remove(cacheId); // remove之后页面切勿再次取值，否则可能造成状态错误
        }
        return success("操作成功");
    }

    //页面修改之后回调 校验
    public Object importVerify() {

        return null;
    }

    protected abstract AbstractImportBiz getImportBiz();

    protected abstract boolean hasTask();

    protected ModelAndView createMV(String viewName) {
        return new ModelAndView(viewName);
    }

    protected JSONResponse error(String msg) {
        return success(msg).setSuccess(Boolean.FALSE);
    }

    protected JSONResponse success(String msg) {
        JSONResponse response = new JSONResponse();
        response.setMsg(msg).setSuccess(Boolean.TRUE);
        return response;
    }
}
