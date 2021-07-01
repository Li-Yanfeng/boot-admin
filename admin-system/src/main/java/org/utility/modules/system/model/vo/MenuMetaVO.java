package org.utility.modules.system.model.vo;

import java.io.Serializable;

/**
 * @author Li Yanfeng
 * @since 2021-06-27
 */
public class MenuMetaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;

    private String icon;

    private Boolean noCache;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getNoCache() {
        return noCache;
    }

    public void setNoCache(Boolean noCache) {
        this.noCache = noCache;
    }

    public MenuMetaVO() {
    }

    public MenuMetaVO(String title, String icon, Boolean noCache) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
    }

    @Override
    public String toString() {
        return "MenuMetaVO{" +
                "title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", noCache=" + noCache +
                '}';
    }
}
