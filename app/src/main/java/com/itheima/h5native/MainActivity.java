package com.itheima.h5native;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.itheimatest.jswebview.JSWebView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private JSWebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //UC：在线模板（方便调试，）
        setWebClient();
        mWebview.loadUrl("http://10.0.3.2:8080/html38/index.html");

        //应用上线：改成本地模板，网页存储到资产目录
    }

    private void setWebClient() {

        //设置2个Web Client(设置浏览器内核)
        mWebview.setWebViewClient(new WebViewClient(){

            //页面加载完成回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // 页面加载完成：调用js方法 webview.loadUrl("javascript:方法名(参数)")
                try {
                    JSONObject json = new JSONObject();
                    json.put("msg", "你好，我是安卓，想加你为蚝友！重构后的代码");
                    mWebview.loadUrl("javascript:showMessage("+json.toString()+")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initView() {
        mWebview = (JSWebView) findViewById(R.id.webview);
    }
}
