package com.sir.app.webview.help;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * 自定义的Android代码和JavaScript代码之间的桥梁类
 * Created by zhuyinan on 2017/12/5.
 * Contact by 445181052@qq.com
 */
public class JavascriptInterfaceImpl {

    Context mContext;
    WebView mWebView;
    Handler mHandler;

    /**
     * Instantiate the interface and set the context
     */
    public JavascriptInterfaceImpl(Context context, WebView webView) {
        this.mContext = context;
        this.mWebView = webView;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Show a toast from the web page
     */
    // 如果target 大于等于API 17，则需要加上如下注解
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_LONG).show();
    }

    /**
     * 同步方法
     *
     * @return
     */
    @JavascriptInterface
    public String syncExec() {
        return "hello android";
    }

    /**
     * 异步方法
     *
     * @param msg
     * @param callbackId
     */
    @JavascriptInterface
    public void asyncExec(final String msg, final String callbackId) {
        new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(5 * 1000);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String url = "javascript:" + callbackId + "('" + msg + " from android " + "')";
                        mWebView.loadUrl(url);
                    }
                });
            }
        }.start();
    }

    /**
     * 接收JavaScript的异步消息
     *
     * @param msg
     */
    @JavascriptInterface
    public void callBack(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }
}
