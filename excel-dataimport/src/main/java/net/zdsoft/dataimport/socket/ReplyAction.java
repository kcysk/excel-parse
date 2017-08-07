package net.zdsoft.dataimport.socket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import net.zdsoft.dataimport.Reply;
import net.zdsoft.dataimport.cache.ReplyCache;

import java.util.concurrent.Future;

/**
 * @author shenke
 * @since 2017.08.04
 */
//@Controller
//@MessageMapping(value = "dataImport")
public class ReplyAction  {

    @MessageMapping(value = "status")
    public Object execute(String cacheId) {
        Future<Reply> replyFuture = ReplyCache.get(cacheId);
        if ( replyFuture != null ) {
            if ( replyFuture.isDone() ) {
                return "success";
            } else {
                return "ing";
            }
        } else {
            return "ing";
        }
    }
}
