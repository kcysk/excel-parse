package net.zdsoft.dataimport;

import lombok.Getter;

/**
 * @author shenke
 * @@since 17-8-10 下午11:51
 */
public enum ImportState {

    WAIT("等待中"),
    DONE("已完成"),
    ING("正在执行"),
    ERROR("错误");

    @Getter private String state;

    ImportState(String state) {
        this.state = state;
    }
}
