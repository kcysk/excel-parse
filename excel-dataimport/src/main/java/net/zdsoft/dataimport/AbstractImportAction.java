package net.zdsoft.dataimport;

import net.zdsoft.dataimport.cache.ReplyCache;
import net.zdsoft.dataimport.process.ExcutorHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2017.08.01
 */
public abstract class AbstractImportAction<T extends QImportEntity> {

    protected Logger logger = LoggerFactory.getLogger(AbstractImportAction.class);

    @Autowired protected ExcutorHolder excutorHolder;

    @RequestMapping(value = "import/index")
    public Object importIndex() {

        return createMV("dataImport/index").addObject("action", getHandler());
    }

    @ResponseBody
    @RequestMapping(value = "import/upload")
    public Object importUpload(@RequestParam("file") MultipartFile excel) {

        //任务模式
        if ( hasTask() ) {
            return success("文件上传成功，导入进度请在导入文件列表中查看");
        }
        //非任务模式
        else {
            //将文件拷贝到指定目录下 FILE_PATH/temp/uuid.xls or xlsx
            String uuid = UUID.randomUUID().toString();
            String tempDir = getTempDir() ;
            String newFile = tempDir + uuid + getFileEx(excel.getOriginalFilename());
            try {
                excel.transferTo(new File(newFile));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //执行非任务导入
            Future<Reply<T>> future = getImportBiz().execute(newFile);
            //reply
            ReplyCache.put(uuid, future);
            JSONResponse response = success("导入成功");
            response.setBusinessValue(uuid);
            return response;
        }
    }

    String getFileEx(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @RequestMapping(value = "/import/template/page")
    public Object importTemplate() {
        return createMV("dataImport/template")
                .addObject("datas", getImportBiz().templates());
    }

    @ResponseBody
    @RequestMapping(value = "import/download", produces = "application/data;charset=UTF-8")
    public Object importDownload(HttpServletResponse response, String header) {
        File file = null;
        try {
            Workbook workbook = getImportBiz().exportTemplate(Arrays.stream(StringUtils.split(header, ",")).collect(Collectors.toList()));
            response.setHeader("Content-Disposition", "attachment; filename=test.xls");
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            return null;
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "import/viewData")
    public Object importViewData(@RequestParam("cacheId") String cacheId) {
        Future<Reply<T>> future = ReplyCache.get(cacheId);
        ModelAndView mv = createMV("/dataImport/viewData");
        try {
            Reply<T> reply = future.get();
            mv.addObject("headers", reply.getHeaders());
            mv.addObject("errorObjects", reply.getErrorObjects());
            mv.addObject("javaObjects", reply.getJavaObjects());
            mv.addObject("reply", reply);
        } catch (Exception e) {
            e.printStackTrace();
            return error("解析Excel数据失败，请联系系统管理员！");
        }
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "import/cancel")
    public Object importCancel(@RequestParam("cacheId") String cacheId) {
        Future<Reply> replyFuture = ReplyCache.get(cacheId);
        try {
            if ( replyFuture != null && !replyFuture.isCancelled()) {
                replyFuture.cancel(Boolean.FALSE);
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
    @RequestMapping(value = "import/verify")
    public Object importVerify(T value, String header) {
        getImportBiz().checkForValid(value, header);
        if ( value.createQImportError().isHasError() ) {
            //错误 FIXME 错误数据
            return error("校验失败");
        }
        return success("校验成功");
    }

    protected String getHandler() {
        RequestMapping requestMapping = this.getClass().getAnnotation(RequestMapping.class);
        if ( requestMapping != null ) {
            String[] mapping = requestMapping.value();
            String hd = mapping[0].replaceFirst("/","");
            return hd.endsWith("/") ? hd.substring(0, hd.lastIndexOf("/")) : hd;
        }
        return StringUtils.EMPTY;
    }

    @RequestMapping(value = "/import/export/template")
    public Object exportTemplate() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Object importList() {
        return null;
    }

    protected abstract AbstractImportBiz getImportBiz();

    protected abstract boolean hasTask();

    protected String getTempDir() {
        //获取当前环境缓存目录
        String tmpDir = System.getProperty("java.io.tmpdir");
        if ( StringUtils.isBlank(tmpDir) ) {
            throw new RuntimeException("can not get cache dir");
        }
        tmpDir = tmpDir + "import" + File.separator + "temp";
        File dir = new File(tmpDir);
        if ( !dir.exists() ) {
            dir.mkdirs();
        }
        return tmpDir;
    }

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
