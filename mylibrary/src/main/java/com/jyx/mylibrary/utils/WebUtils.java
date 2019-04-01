package com.jyx.mylibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jyx.mylibrary.widget.image.ImagePreviewUtlis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Web utils.
 *
 * @author:jyx created at 2019/2/20 23:33 explain:web加载数据封装
 */
public class WebUtils {

    private static String method = "jsCallJavaObj";

    /**
     * 离线加载的网页内容要加上的一些网页源码
     * 博主这里返回的网页源码是指包含body的内容的,所以这些网页的头和一些css样式可以直接拼接
     */
    private static String codePrefixOne = "<!DOCTYPE html >" +
            "<html>" +
            "<head>" +
            "<style type=\"text/css\" scoped>" +
            ".content {   " +
            " line-height: 30px!important;" +
            " font-size: 18px!important;" +
            " color: #444!important;" +
            " word-break: break-all!important;}" +
            " .content img {max-width: 100%!important;}" +
            " .content p span{font-size: 18px!important;}" +
            " .content .article-sub span{font-size: 18px!important;}";

    private static String codePrefixTwo = "</style>" + "</head>" + "<body><div class=\"content\">";  //</body>" + "</html>

    private static String codeSubfix = "</div></body></html>";

    private static String webUrlChange(String string) {

        return codePrefixOne + codePrefixTwo + string + codeSubfix;//+ "*{color:#444444" +";line-height:1.9"+ ";font-family:\"微软雅黑\"" + ";}body{word-wrap:break-word;}"
    }

    /**
     * 初始化webView
     *
     * @param contentInfoWebView the content info web view
     * @param webUrl             the web url
     */
    @SuppressLint("JavascriptInterface")
    public static void loadWeb(WebView contentInfoWebView, String webUrl) {

        //Arial
        webUrl = WebUtils.webUrlChange(webUrl);
        Log.e("WebUtils", webUrl);

//        //支持javascript
        contentInfoWebView.getSettings().setJavaScriptEnabled(true);
//        //自适应屏幕
//        contentInfoWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        contentInfoWebView.loadDataWithBaseURL(null, webUrl, "text/html", "UTF-8", null);

        String[] imgs = returnImageUrlsFromHtml(webUrl);

        contentInfoWebView.addJavascriptInterface(new ImageJavascriptInterface(contentInfoWebView.getContext(), imgs), method);
        contentInfoWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setWebImageClick(view, method);
            }
        });
    }

    /**
     * Return image urls from html string [ ].
     *
     * @param htmlCode the html code
     * @return the string [ ]
     */
    public static String[] returnImageUrlsFromHtml(String htmlCode) {
        List<String> imageSrcList = new ArrayList<String>();
        Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic|\\b)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);
        String quote = null;
        String src = null;
        while (m.find()) {
            quote = m.group(1);
            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("//s+")[0] : m.group(2);
            imageSrcList.add(src);
        }
        if (imageSrcList.size() == 0) {
            return null;
        }
        return imageSrcList.toArray(new String[imageSrcList.size()]);
    }

    /**
     * 设置网页中图片的点击事件
     *
     * @param view   the view
     * @param method the method
     */
    public static void setWebImageClick(WebView view, String method) {
        String jsCode = "javascript:(function(){" +
                "var imgs=document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<imgs.length;i++){" +
                "imgs[i].pos = i;" +
                "imgs[i].onclick=function(){" +
                "window." + method + ".openImage(this.src,this.pos);" +
                "}}})()";
        view.loadUrl(jsCode);
    }

    /**
     * WebView与android交互的接口设置实现类
     * 1、可接口然后具体实现，然后在webview设置时具体实现
     * 2、也可以直接实现，这样在同样需求的地方就可以直接复用了
     * Created by dl on 2018/8/9.
     */
    public static class ImageJavascriptInterface {
        private Context context;
        private String[] imageUrls;

        /**
         * Instantiates a new Image javascript interface.
         *
         * @param context   the context
         * @param imageUrls the image urls
         */
        public ImageJavascriptInterface(Context context, String[] imageUrls) {
            this.context = context;
            this.imageUrls = imageUrls;
        }

        /**
         * Open image.
         *
         * @param img the img
         * @param pos the pos
         */
//java回调js代码，不要忘了@JavascriptInterface这个注解，不然点击事件不起作用
        @JavascriptInterface
        public void openImage(String img, int pos) {
            List<String> stringList = new ArrayList<>();
            for (int i = 0; i < imageUrls.length; i++) {
                stringList.add(imageUrls[i]);
            }
            ImagePreviewUtlis.setMutliImage(context, stringList, pos);
        }
    }

    /**
     * 获取<p></p>标签内容
     *
     * @param strUrl
     * @return
     */
    public static String getUrlPStr(String strUrl) {
        String resultStr = "";
        Pattern pattern = Pattern.compile("<p>([^<]+)<", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(strUrl);
        if (matcher.find()) {
            String group1 = matcher.group(1);
            resultStr = group1;
        }

        return resultStr;
    }

    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
    private static final String regEx_nbsp = " ";

    /**
     * @param htmlStr
     * @return 删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签

        Pattern p_nbsp = Pattern.compile(regEx_nbsp, Pattern.CASE_INSENSITIVE);
        Matcher m_nbsp = p_nbsp.matcher(htmlStr);
        htmlStr = m_nbsp.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }

    public static String getTextFromHtml(String htmlStr) {
        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll(" ", "");
        //htmlStr = htmlStr.substring(0, htmlStr.indexOf("。")+1);
        return htmlStr;
    }
}
