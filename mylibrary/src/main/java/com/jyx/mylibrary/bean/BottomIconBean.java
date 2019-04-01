package com.jyx.mylibrary.bean;

/**
 * @author jyx
 * @CTime 2019/3/12
 * @explain:
 */
public class BottomIconBean {

    private String strTitle;
    private Integer integerIcon;
    private Integer type;

    public BottomIconBean() {

    }

    public BottomIconBean(String strTitle, Integer integerIcon) {
        this.strTitle = strTitle;
        this.integerIcon = integerIcon;
    }

    public BottomIconBean(String strTitle, Integer integerIcon, Integer type) {
        this.strTitle = strTitle;
        this.integerIcon = integerIcon;
        this.type = type;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public Integer getIntegerIcon() {
        return integerIcon;
    }

    public void setIntegerIcon(Integer integerIcon) {
        this.integerIcon = integerIcon;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
