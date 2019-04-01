package com.jyx.mylibrary.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * @author jyx
 * @ctime 2017/10/24:14:37
 * @explain 手机组件调用工具类
 */

public class PhoneUtil {
    private static long lastClickTime;

    private PhoneUtil() {
        throw new Error("Do not need instantiate!");
    }

    /**
     * 调用系统发短信界面
     *
     * @param activity    Activity
     * @param phoneNumber 手机号码
     * @param smsContent  短信内容
     */
    public static void sendMessage(Context activity, String phoneNumber, String smsContent) {
        if (smsContent == null || phoneNumber.length() < 4) {
            return;
        }
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", smsContent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 判断是否为连击
     *
     * @return boolean
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 获取手机型号
     *
     * @param context 上下文
     * @return String
     */
    public static String getMobileModel(Context context) {
        try {
            String model = android.os.Build.MODEL;
            return model;
        } catch (Exception e) {
            return "未知！";
        }
    }

    /**
     * 获取手机品牌
     *
     * @param context 上下文
     * @return String
     */
    public static String getMobileBrand(Context context) {
        try {
            // android系统版本号
            String brand = android.os.Build.BRAND;
            return brand;
        } catch (Exception e) {
            return "未知";
        }
    }

    /**
     * 拍照打开照相机！
     *
     * @param requestcode 返回值
     * @param activity    上下文
     * @param fileName    生成的图片文件的路径
     */
    public static void toTakePhoto(int requestcode, Activity activity, String fileName) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2);// 调用前置摄像头
        intent.putExtra("autofocus", true);// 自动对焦
        intent.putExtra("fullScreen", false);// 全屏
        intent.putExtra("showActionIcons", false);
        try {
            //创建一个当前任务id的文件，然后里面存放任务的照片和路径！这主文件的名字是用uuid到时候再用任务id去查路径！
            File file = new File(fileName);
            //如果这个文件不存在就创建一个文件夹！
            if (!file.exists()) {
                file.mkdirs();
            }
            Uri uri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, requestcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开相册
     *
     * @param requestcode 响应码
     * @param activity    上下文
     */
    public static void toTakePicture(int requestcode, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, requestcode);
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public static void callPhone(Context context,String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    public static void actionCallPhone(Context context,String phoneNum){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }

    //添加到手机联系人
    public static void addContact(Context context,String name,String phone){
        Uri insertUri = android.provider.ContactsContract.Contacts.CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_INSERT, insertUri);
        intent.putExtra(android.provider.ContactsContract.Intents.Insert.NAME, name);//名字显示在名字框
        intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE,phone);//号码显示在号码框
        context.startActivity(intent);
    }

    //保存至已有联系人
    public static void saveExistContact(Context context,String phone) {
        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        intent.setType("vnd.android.cursor.item/person");
        intent.setType("vnd.android.cursor.item/contact");
        intent.setType("vnd.android.cursor.item/raw_contact");
        intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, phone);
        intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE_TYPE, 3);
        context.startActivity(intent);
    }

    //内容到剪切板
    public static void setPrimaryClip(Context context,String string){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", string);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }
}
