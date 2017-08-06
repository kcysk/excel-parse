package net.zdsoft.dataimport;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shenke
 * @since 2017.08.03
 */
public class Reply<T extends QImportEntity> {

    @Getter @Setter private String id;
    @Getter @Setter private String message;
    @Getter @Setter private String globeError;
    @Getter @Setter private List<T> javaObjects;
    @Getter @Setter private List<T> errorObjects;

    public static <O extends QImportEntity> Reply<O> buildGlobeErrorReply(String error) {
        Reply<O> reply = new Reply<O>();
        reply.setGlobeError(error);
        return reply;
    }
}