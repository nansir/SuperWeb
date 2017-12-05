package com.sir.app.webview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sir.app.base.BaseActivity;
import com.sir.app.webview.help.JavascriptInterfaceImpl;

/**
 *  HTML5 交互
 * Created by zhuyinan on 2017/12/5.
 * Contact by 445181052@qq.com
 */
public class WebHTMLActivity extends BaseActivity {

    WebView webview;

    @Override
    public int bindLayout() {
        return R.layout.activity_web_html5;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        webview = (WebView) findViewById(R.id.web_view);
        // 得到设置属性的对象
        WebSettings webSettings = webview.getSettings();
        // 使能JavaScript
        webSettings.setJavaScriptEnabled(true);
        // 支持中文，否则页面中中文显示乱码
        webSettings.setDefaultTextEncodingName("UTF-8");
    }

    @Override
    public void doBusiness(Context mContext) {

        // 传入一个Java对象和一个接口名,在JavaScript代码中用这个接口名代替这个对象,通过接口名调用Android接口的方法
        webview.addJavascriptInterface(new JavascriptInterfaceImpl(this, webview), "Android");

        // WebViewClient 主要帮助WebView处理各种通知、请求事件的
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        // WebChromeClient主要用来辅助WebView处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        // 载入页面：本地html资源文件
        webview.loadUrl("file:///android_asset/sample.html");

        // 这里用一个Android按钮按下后调用JS中的代码
        findViewById(R.id.button1).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 调用JavaScript同步方法
                        webview.loadUrl("javascript:myFunction()");
                    }
                });

        findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 调用JavaScript异步方法
                webview.loadUrl("javascript:asyncFun()");
            }
        });
    }
}
