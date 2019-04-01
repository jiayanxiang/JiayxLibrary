package com.jyx.mylibrary.wxapi;

import android.content.Context;
import android.text.TextUtils;

import com.jyx.mylibrary.bean.PayForBean;
import com.jyx.mylibrary.utils.StringUtils;
import com.jyx.mylibrary.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付外层封装
 * Created by jyx
 * 单例实现,由于无需全局初始化,所以可以单例实现,id唯一
 */
public class WXPay {

    private static WXPay mWXPay;
    private IWXAPI mWXApi;
    private String mPayParam;
    private WXPayResultCallBack mCallback;

    public static final int NO_OR_LOW_WX = 1;   //未安装微信或微信版本过低
    public static final int ERROR_PAY_PARAM = 2;  //支付参数错误
    public static final int ERROR_PAY = 3;  //支付失败
    public static final int ORDER_CANCLE = -2;
    //外层封装回调
    public interface WXPayResultCallBack {
        void onSuccess(); //支付成功

        void onError(int error_code);   //支付失败

        void onCancel();    //支付取消
    }

    public WXPay(Context context, String wx_appid) {
        mWXApi = WXAPIFactory.createWXAPI(context, null);
        mWXApi.registerApp(wx_appid);

    }


    public static void init(Context context, String wx_appid) {
        if (mWXPay == null) {
            mWXPay = new WXPay(context, wx_appid);
        }
    }

    public static WXPay getInstance() {
        return mWXPay;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    /**
     * 发起微信支付
     */
    public void doPay(PayForBean payForBean, WXPayResultCallBack callback) {
        mCallback = callback;

        if (!check()) {
            if (mCallback != null) {
                mCallback.onError(NO_OR_LOW_WX);
            }
            return;
        }


        if (StringUtils.isObjectEmpty(payForBean) || StringUtils.isObjectEmpty(payForBean.getAppid()) || StringUtils.isObjectEmpty(payForBean.getNoncestr())
         || StringUtils.isObjectEmpty(payForBean.getPackageX()) || StringUtils.isObjectEmpty(payForBean.getPartnerid())
         || StringUtils.isObjectEmpty(payForBean.getPrepayid()) || StringUtils.isObjectEmpty(payForBean.getSign())
        || StringUtils.isObjectEmpty(payForBean.getTimestamp())) {

            if (mCallback != null) {
                mCallback.onError(ERROR_PAY_PARAM);
            }
            return;
        }

        PayReq req = new PayReq();
        req.appId = payForBean.getAppid();
        req.partnerId = payForBean.getPartnerid();
        req.prepayId = payForBean.getPrepayid();
        req.packageValue = payForBean.getPackageX();
        req.nonceStr = payForBean.getNoncestr();
        req.timeStamp = payForBean.getTimestamp();
        req.sign = payForBean.getSign();
        mWXApi.sendReq(req);
    }

    /**
     * 发起微信支付
     */
    public void doPay(String pay_param, WXPayResultCallBack callback) {
        mPayParam = pay_param;
        mCallback = callback;

        if (!check()) {
            if (mCallback != null) {
                mCallback.onError(NO_OR_LOW_WX);
            }
            return;
        }

        JSONObject param = null;
        try {
            param = new JSONObject(mPayParam);
        } catch (JSONException e) {
            e.printStackTrace();
            if (mCallback != null) {
                mCallback.onError(ERROR_PAY_PARAM);
            }
            return;
        }
        //异常检测
     /*   if (param == null){
            MyToast.getInstence().showWarningToast("---------------------1");
        }
        if (TextUtils.isEmpty(param.optString("appid"))){
            MyToast.getInstence().showWarningToast("---------------2");
        }  if (TextUtils.isEmpty(param.optString("partnerid"))){
            MyToast.getInstence().showWarningToast("---------------3");
        }  if (TextUtils.isEmpty(param.optString("prepayid"))){
            MyToast.getInstence().showWarningToast("---------------4");
        }  if (TextUtils.isEmpty(param.optString("package"))){
            MyToast.getInstence().showWarningToast("---------------5");
        }  if (TextUtils.isEmpty(param.optString("noncestr"))){
            MyToast.getInstence().showWarningToast("---------------5");
        }  if (TextUtils.isEmpty(param.optString("timestamp"))){
            MyToast.getInstence().showWarningToast("---------------7");
        } if (TextUtils.isEmpty(param.optString("sign"))){
            MyToast.getInstence().showWarningToast("---------------8");
        }*/


        if (param == null || TextUtils.isEmpty(param.optString("appid")) || TextUtils.isEmpty(param.optString("partnerid"))
                || TextUtils.isEmpty(param.optString("prepayid")) || TextUtils.isEmpty(param.optString("package")) ||
                TextUtils.isEmpty(param.optString("noncestr")) || TextUtils.isEmpty(param.optString("timestamp")) ||
                TextUtils.isEmpty(param.optString("sign"))) {

            if (mCallback != null) {
                mCallback.onError(ERROR_PAY_PARAM);
            }
            return;
        }

        PayReq req = new PayReq();
        req.appId = param.optString("appid");
        req.partnerId = param.optString("partnerid");
        req.prepayId = param.optString("prepayid");
        req.packageValue = param.optString("package");
        req.nonceStr = param.optString("noncestr");
        req.timeStamp = param.optString("timestamp");
        req.sign = param.optString("sign");
        mWXApi.sendReq(req);
    }

    //支付回调响应
    public void onResp(int error_code) {
        if (mCallback == null) {
            ToastUtils.show("微信回调信息异常");
            return;
        }

        if (error_code == 0) {   //成功
            mCallback.onSuccess();
        } else if (error_code == -1) {   //错误
            mCallback.onError(ERROR_PAY);
        } else if (error_code == -2) {   //取消
            mCallback.onCancel();
        }
        mCallback = null;
    }

    //检测是否支持微信支付
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }


    /*
     * 微信支付签名算法sign
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
//    public static String createSign(String characterEncoding,
//                                    SortedMap<Object, Object> parameters) {
//
//        StringBuffer sb = new StringBuffer();
//        Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
//        Iterator it = es.iterator();
//        while (it.hasNext()) {
//            @SuppressWarnings("rawtypes")
//            Map.Entry entry = (Map.Entry) it.next();
//            String k = (String) entry.getKey();
//            Object v = entry.getValue();
//            if (null != v && !"".equals(v) && !"sign".equals(k)
//                    && !"key".equals(k)) {
//                sb.append(k + "=" + v + "&");
//            }
//        }
//        sb.append("key=" + WxContents.PARTENR_KEY); //KEY是商户秘钥
//        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding)
//                .toUpperCase();
//        return sign; // D3A5D13E7838E1D453F4F2EA526C4766
//    }

    /**
     * 获取sign签名
     *
     * @return
     */
//    public static String genPayReq(PayForBean payForBean) {
//
//        // 获取参数的值
//        PayReq request = new PayReq();
//        request.appId = payForBean.getAppid();
//        request.partnerId = payForBean.getPartnerid();
//        request.prepayId = payForBean.getPrepayid();
//        request.packageValue = "Sign=WXPay";
//        request.nonceStr = payForBean.getNoncestr();
//        request.timeStamp = payForBean.getTimestamp();
//
//        // 把参数的值传进去SortedMap集合里面
//        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
////  {appid=wx34df375d7dae8c90, noncestr=3BF34EF2CA4A462DB8D4EA48E785CDC3,
////    package=Sign=WXPay, partnerid=1349967601,
////    prepayid=wx2016070910354542c7155d4e0846850809, timestamp=1468031760}
//        parameters.put("appid", request.appId);
//        parameters.put("noncestr", request.nonceStr);
//        parameters.put("package", request.packageValue);
//        parameters.put("partnerid", request.partnerId);
//        parameters.put("prepayid", request.prepayId);
//        parameters.put("timestamp", request.timeStamp);
//
//        String characterEncoding = "UTF-8";
//        String mySign = createSign(characterEncoding, parameters);
//        System.out.println("我的签名是：" + mySign);
//        return mySign;
//    }
}
