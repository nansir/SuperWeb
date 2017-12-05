package com.sir.app.webview;

import com.sir.app.base.BaseApplication;
import com.tencent.smtt.sdk.TbsDownloader;

/**
 * Created by zhuyinan on 2017/12/5.
 * Contact by 445181052@qq.com
 */
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核
        //TbsDownloader.needDownload(getApplicationContext(), false);
    }
}
