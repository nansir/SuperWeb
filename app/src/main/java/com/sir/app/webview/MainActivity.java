package com.sir.app.webview;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.sir.app.base.BaseActivity;
import com.sir.app.tbs.FileDisplayActivity;
import com.sir.app.tbs.WebViewActivity;

import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @OnClick({R.id.btn_webview, R.id.btn_file_a, R.id.btn_file_b,
            R.id.btn_file_c, R.id.btn_file_d, R.id.btn_file_e,
            R.id.btn_file_f, R.id.btn_javascript})
    public void onViewClicked(View view) {
        String path = null;
        switch (view.getId()) {
            case R.id.btn_javascript:
                getOperation().forward(WebHTMLActivity.class);
                break;
            case R.id.btn_webview:
                getOperation().addParameter("url", "https://www.baidu.com/")
                        .forward(WebViewActivity.class);
                break;
            case R.id.btn_file_a:
                path = "/storage/emulated/0/test.docx";
                break;
            case R.id.btn_file_b:
                path = "/storage/emulated/0/test.txt";
                break;
            case R.id.btn_file_c:
                path = "/storage/emulated/0/test.xlsx";
                break;
            case R.id.btn_file_d:
                path = "/storage/emulated/0/test.pptx";
                break;
            case R.id.btn_file_e:
                path = "/storage/emulated/0/test.pdf";
                break;
            case R.id.btn_file_f:
                path = "http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc";
                break;
        }

        String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (!EasyPermissions.hasPermissions(MainActivity.this, perms)) {
            EasyPermissions.requestPermissions(MainActivity.this, "需要访问手机存储权限！", 10086, perms);
        } else {
            if (path != null) {
                getOperation().addParameter("path", path)
                        .forward(FileDisplayActivity.class);
            }
        }
    }
}
