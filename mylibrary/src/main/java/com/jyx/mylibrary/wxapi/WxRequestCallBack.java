package com.jyx.mylibrary.wxapi;

import android.content.Context;

import com.jyx.mylibrary.utils.JsonUtil;
import com.jyx.mylibrary.utils.StringUtils;
import com.jyx.mylibrary.widget.progress.MyProgress;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 监听回调
 */
public abstract class WxRequestCallBack extends Callback<String> {

    private Context context;

    public WxRequestCallBack(Context context) {
        this.context = context;
    }

    /**
     * UI Thread
     *
     * @param request
     */
    @Override
    public void onBefore(Request request, int id) {
        super.onBefore(request, id);
        MyProgress.getInstance().showActivityAnimation(context);
    }

    @Override
    public void onAfter(int id) {
        super.onAfter(id);
        MyProgress.getInstance().dismiss();
    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException {
        if (StringUtils.isObjectEmpty(response)) {
            return null;
        }

        if (StringUtils.isObjectEmpty(response.body())) {
            return null;
        }

        String responseStr = response.body().string();
        if (!JsonUtil.isJsonObject(responseStr)) {
            return null;
        }

        return responseStr;
    }
}
