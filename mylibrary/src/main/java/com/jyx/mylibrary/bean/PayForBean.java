package com.jyx.mylibrary.bean;

import com.google.gson.annotations.SerializedName;

public class PayForBean {

    /**
     * appid : wxb4ba3c02aa476ea1
     * partnerid : 1900006771
     * package : Sign=WXPay
     * noncestr : 027702d458657fa2791e80e39a247cc8
     * timestamp : 1548320962
     * prepayid : wx24170922923792926e3b4a110023051038
     * sign : 5C62617D166E2F049909260C69F906A0
     */

    private String appid;
    private String partnerid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private String timestamp;
    private String prepayid;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
