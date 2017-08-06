package net.zdsoft.dataimport.core;

import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;

/**
 * @author shenke
 * @since 2017.07.31
 */
public abstract class Node<E extends Annotation> {

    @Getter @Setter private E annotation;
}
