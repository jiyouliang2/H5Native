package com.itheimatest.jswebview;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 该类的方法都是提供给js调用的
 */

public class JavaScriptMethods {

    private Context mContext;
    private WebView mWebView;

    private Handler mHandler = new Handler();

    public JavaScriptMethods(Context context, WebView webView) {
        this.mContext = context;
        this.mWebView = webView;
    }

    @JavascriptInterface /*android17以上，如果不添加该注解，js无法调用android方法*/
    public void showToast(String json){
        Toast.makeText(mContext, ""+json, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取酒店数据
     * @param  json:js传递过来的参数（核心参数回调方法名称）
     */

    @JavascriptInterface /*android17以上，如果不添加该注解，js无法调用android方法*/
    public void getHotelData(final String json){
        System.out.println("回调数据："+json);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL("http://120.77.241.119/Json/computer").openConnection();
                    InputStream is = conn.getInputStream();
                    //转成字符串
                    final String result = IoUtils.convertStreamToString(is);
                    System.out.println("安卓请求服务器数据="+result);
                    is.close();

                    //解析js回调方法
                    JSONObject backJson = new JSONObject(json);
                    System.out.println("backJson="+backJson);
                    final String callback = backJson.optString("callback");
                    callbackJavascript(result, callback);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 统一管理所有安卓callback js
     * @param result
     * @param callback
     */
    private void callbackJavascript(final String result, final String callback) {
        //安卓回传数据给js:调用js方法必须在主线程
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //mWebView.loadUrl("javascript:方法名(参数)");
                //mWebView.loadUrl("javascript:receiveHotelData("+result+")");
                mWebView.loadUrl("javascript:"+callback+"("+result+")");
            }
        });
    }
}
