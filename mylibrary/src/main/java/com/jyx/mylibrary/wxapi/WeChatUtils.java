package com.jyx.mylibrary.wxapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jyx.mylibrary.R;
import com.jyx.mylibrary.bean.PayForBean;
import com.jyx.mylibrary.utils.BitmapUtil;
import com.jyx.mylibrary.utils.JsonUtil;
import com.jyx.mylibrary.utils.StringUtils;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;

/**
 * @author:jyx created at 2019/1/24 15:48
 * explain:微信工具类
 */
public class WeChatUtils {

    public static final String TAG = "WeChatUtils";

    private static IWXAPI iwxApi = null;
    private Context context;

    public WeChatUtils(Context context) {
        this.context = context;
        getIwxApi(context);
    }

    public WeChatUtils(Context context, String strAppId) {
        this.context = context;
        getIwxApi(context, strAppId);
    }

    /**
     * 注册到微信
     */
    public static IWXAPI getIwxApi(Context context) {
        if (iwxApi == null) {
            // 通过WXAPIFactory工厂，获取IWXAPI的实例--IWXAPI 是第三方app和微信通信的openApi接口
            iwxApi = WXAPIFactory.createWXAPI(context, WxContents.APP_ID);
            // 将应用的appId注册到微信
            iwxApi.registerApp(WxContents.APP_ID);
        }
        return iwxApi;
    }

    /**
     * 注册到微信
     */
    public static IWXAPI getIwxApi(Context context, String strAppId) {
        if (iwxApi == null) {
            // 通过WXAPIFactory工厂，获取IWXAPI的实例--IWXAPI 是第三方app和微信通信的openApi接口
            iwxApi = WXAPIFactory.createWXAPI(context, strAppId);
            // 将应用的appId注册到微信
            iwxApi.registerApp(strAppId);
        }
        return iwxApi;
    }

    /**
     * 打开微信
     */
    public void openWXApp() {
        getIwxApi(context).openWXApp();
    }

    /**
     * 当前微信版本是否支持微信支付
     *
     * @return
     */
    public Boolean isWXAppSupportAPI() {
        boolean isPaySupported = getIwxApi(context).getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        return isPaySupported;
    }

    public PayForBean getPayForData() {
        PayForBean payForBean = new PayForBean();
        payForBean.setAppid("wxb4ba3c02aa476ea1");
        payForBean.setPartnerid("1900006771");
        payForBean.setPackageX("Sign=WXPay");
        payForBean.setNoncestr("41eef25a3714b36630118249b579ed7f");
        payForBean.setPrepayid("wx19150636530707c2bd5f31b81230008062");
        payForBean.setSign("B8F5E7868847099F23ACCA3C295E0FA9");
        payForBean.setTimestamp(1552979196 + "");
        return payForBean;
    }

    public void wxPayFor(PayForBean payForBean) {
        if (!isWXAppSupportAPI()) {
            Log.e(TAG, "微信支付版本过低");
            return;
        }

//        String url = "{\"appid\":\"wxb4ba3c02aa476ea1\",\"partnerid\":\"1900006771\",\"package\":\"Sign=WXPay\",\"noncestr\":\"8a0a42c49d47ebbbb42e053f7240c3b2\",\"timestamp\":1548398924,\"prepayid\":\"wx25144844897410576ca72e960951804231\",\"sign\":\"3A8BA3C77C9E8F40D893EC4E1A489CC1\"}";

        PayReq req = new PayReq();
        req.appId = payForBean.getAppid();
        req.partnerId = payForBean.getPartnerid();
        req.prepayId = payForBean.getPrepayid();
        req.nonceStr = payForBean.getNoncestr();
        req.timeStamp = payForBean.getTimestamp() + "";
        req.packageValue = payForBean.getPackageX();
        req.sign = payForBean.getSign();

        Log.e(TAG, JsonUtil.objectToJson(req));
        boolean b = getIwxApi(context).sendReq(req);
        Log.e(TAG, b + "-----");
    }

    //分享相关-文字
    public Boolean wxShareTxt(Context context, String strTxt) {
        //初始化一个 WXTextObject 对象，填写分享的文本内容
        WXTextObject textObj = new WXTextObject();
        textObj.text = strTxt;

//用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = strTxt;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = WXSceneSession;
//调用api接口，发送数据到微信
        return getIwxApi(context).sendReq(req);
    }

    //分享-图片
    public Boolean wxShareImage(Bitmap bitmap) {

//初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

//设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        bitmap.recycle();
        msg.thumbData = BitmapUtil.bitmapToByte(thumbBmp);

//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = WXSceneSession;
        req.userOpenId = WxContents.opend_id;
//调用api接口，发送数据到微信
        return getIwxApi(context).sendReq(req);
    }

    //分享-网页链接
    public Boolean wxShareTypeWebUrl(String strTitle, String strMessage, int type, Bitmap bitmap, String strUrl) {
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = strUrl;

//用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = strTitle;
        msg.description = strMessage;
        msg.thumbData = BitmapUtil.bitmapToByte(bitmap);

        if (StringUtils.isObjectEmpty(msg.thumbData)) {
            Bitmap thumbBmp = BitmapUtil.drawableToBitmap(context.getResources().getDrawable(R.drawable.course_logo));
            msg.thumbData = BitmapUtil.bitmapToByte(thumbBmp);
        }

//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        req.userOpenId = WxContents.opend_id;

//调用api接口，发送数据到微信
        return getIwxApi(context).sendReq(req);
    }

    //分享-网页链接
    public void wxShareTypeWebUrl(final String strTitle, final String strMessage, final int type, final String imageUrl, final String strUrl) {
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = strUrl;

//用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        final WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = strTitle;
        msg.description = strMessage;

        Bitmap thumbBmp = BitmapUtil.drawableToBitmap(context.getResources().getDrawable(R.drawable.course_logo));
        if (!StringUtils.isObjectEmpty(imageUrl)) {
            Glide.with(context).load(imageUrl + "?w=100&h=100").asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    int bitmapSize = BitmapUtil.getBitmapSize(resource);
                    if (bitmapSize > 35*1024) {
                        resource = BitmapUtil.getZoomImage(resource, 30);
                        wxShareTypeWebUrl(strTitle, strMessage, type, resource, strUrl);
                    } else {
                        wxShareTypeWebUrl(strTitle, strMessage, type, resource, strUrl);
                    }
                }
            });
            return;
        }
        msg.thumbData = bmpToByteArray(thumbBmp, true);

//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        req.userOpenId = WxContents.opend_id;

//调用api接口，发送数据到微信
        getIwxApi(context).sendReq(req);
    }

    //分享-网页链接
    public Boolean wxShareTypeWebUrl(String strTitle, String strMessage, int type, String strUrl) {
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = strUrl;

//用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = strMessage;
        msg.description = strTitle;
        Bitmap thumbBmp = BitmapUtil.drawableToBitmap(context.getResources().getDrawable(R.drawable.course_logo));
//        BitmapFactory.decodeResource(context.getResources(), R.drawable.course_logo);
        msg.thumbData = bmpToByteArray(thumbBmp, true);

//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        req.userOpenId = WxContents.opend_id;

//调用api接口，发送数据到微信
        return getIwxApi(context).sendReq(req);
    }

    //分享-网页链接
    public Boolean wxShareTypeWebUrl(String strMessage, int type, String strUrl) {
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = strUrl;

//用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "";
        msg.description = strMessage;
        Bitmap thumbBmp = BitmapUtil.drawableToBitmap(context.getResources().getDrawable(R.drawable.course_logo));
//        BitmapFactory.decodeResource(context.getResources(), R.drawable.course_logo);
        msg.thumbData = bmpToByteArray(thumbBmp, true);

//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        req.userOpenId = WxContents.opend_id;

//调用api接口，发送数据到微信
        return getIwxApi(context).sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 10,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                // F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }

    private RequestResultListener requestResultListener;

    public void setRequestResultListener(RequestResultListener requestResultListener) {
        this.requestResultListener = requestResultListener;
    }

    public interface RequestResultListener {

        void onRespons(String string);

        void onError(String errorStr);
    }

}
