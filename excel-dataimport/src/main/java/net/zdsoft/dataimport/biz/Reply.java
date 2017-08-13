package net.zdsoft.dataimport.biz;

import lombok.Getter;
import lombok.Setter;
import net.zdsoft.dataimport.ImportApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

/**
 * @author shenke
 * @since 2017.08.03
 */
public class Reply<QT extends QImportEntity> {

    @Getter @Setter private String id;
    @Getter private String globeError;
    @Getter @Setter private Long creationTime;
    @Getter @Setter private ImportState importState;

    private SimpMessagingTemplate simpMessagingTemplate;

    private static TaskReply taskReply;

    public Reply() {
        this.creationTime = System.currentTimeMillis();
        this.importState = ImportState.WAIT;
    }

    public static Reply buildGlobeErrorReply(String globeError) {
        Reply reply = new Reply();
        reply.setGlobeError(globeError);
        Reply.taskReply = bulidTaskReply();
        return reply;
    }

    public static Reply.TaskReply bulidTaskReply() {
        return new TaskReply();
    }

    public Reply setJavaObjects(List<QT> javaObjects) {
        Reply.taskReply.setJavaObjects(javaObjects);
        return this;
    }

    public List<QT> getJavaObjects() {
        return Reply.taskReply.getJavaObjects();
    }

    public Reply setErrorObjects(List<QT> errorObjects) {
        Reply.taskReply.setErrorObjects(errorObjects);
        return this;
    }

    public List<QT> getErrorObjects() {
        return Reply.taskReply.getErrorObjects();
    }

    public Reply setHeaders(List<String> headers) {
        Reply.taskReply.setHeaders(headers);
        return this;
    }

    public List<String> getHeaders() {
        return Reply.taskReply.getHeaders();
    }

    /**
     * 该方法内部回实时通知客户端
     * @param globeError
     * @return
     */
    public Reply setGlobeError(String globeError) {
        this.globeError = globeError;
        this.importState = ImportState.ERROR;
        notifyClient(ImportState.ERROR.getCode(), globeError);
        return this;
    }

    /**
     * 通知客户端解析状态
     */
    public void notifyClient(int importStateCode , String msg) {
        JSONResponse response = new JSONResponse()
                .setImportStateCode(importStateCode)
                .setBusinessValue(this.id)
                .setMsg(msg);
        getSimpMessagingTemplate().convertAndSend("/import/status", response);
    }

    private SimpMessagingTemplate getSimpMessagingTemplate() {
        if ( simpMessagingTemplate == null ) {
            this.simpMessagingTemplate = ImportApplicationContext.getApplication().getBean(SimpMessagingTemplate.class);
        }
        return simpMessagingTemplate;
    }

    public static class TaskReply<QT> {

        private TaskReply() {

        }

        @Getter @Setter private List<QT> javaObjects;
        @Getter @Setter private List<QT> errorObjects;
        @Getter @Setter private List<String> headers;
    }
}