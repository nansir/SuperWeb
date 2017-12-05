package com.sir.app.tbs;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.sir.app.base.BaseActivity;
import com.sir.app.tbs.help.LoadFileModel;
import com.sir.app.tbs.help.Md5Tool;
import com.sir.app.tbs.help.SuperFileView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 腾讯X5 文档阅读
 * Created by zhuyinan on 2017/8/5.
 * Contact by 445181052@qq.com
 */
public class FileDisplayActivity extends BaseActivity {

    SuperFileView mSuperFileView;
    String filePath;

    @Override
    public int bindLayout() {
        return R.layout.activity_file_display;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mSuperFileView = (SuperFileView) findViewById(R.id.super_file_view);
        filePath = getIntent().getStringExtra("path");
    }

    @Override
    public void doBusiness(Context mContext) {
        mSuperFileView.setOnGetFilePathListener(new SuperFileView.OnGetFilePathListener() {
            @Override
            public void onGetFilePath(SuperFileView mSuperFileView) {
                if (filePath.contains("http")) {//网络地址要先下载
                    downLoadFromNet(filePath, mSuperFileView);
                } else {
                    mSuperFileView.displayFile(new File(filePath));
                }
            }
        });
        mSuperFileView.show();
    }

    private void downLoadFromNet(final String url, final SuperFileView mSuperFileView) {
        //1.网络下载、存储路径、
        File cacheFile = getCacheFile(url);
        if (cacheFile.exists()) {
            if (cacheFile.length() <= 0) {
                cacheFile.delete();
                return;
            }
        }

        LoadFileModel.loadPdfFile(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean flag;
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    ResponseBody responseBody = response.body();
                    is = responseBody.byteStream();
                    long total = responseBody.contentLength();
                    File file1 = getCacheDir(url);
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    File fileN = getCacheFile(url);
                    if (!fileN.exists()) {
                        fileN.createNewFile();
                    }
                    fos = new FileOutputStream(fileN);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                    }
                    fos.flush();
                    //2.ACache记录文件的有效期
                    mSuperFileView.displayFile(fileN);
                } catch (Exception e) {
                    Log.d("TAG", "文件下载异常 = " + e.toString());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                File file = getCacheFile(url);
                if (!file.exists()) {
                    file.delete();
                }
            }
        });
    }

    /***
     * 获取缓存目录
     *
     * @param url
     * @return
     */
    private File getCacheDir(String url) {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/");
    }

    /***
     * 绝对路径获取缓存文件
     *
     * @param url
     * @return
     */
    private File getCacheFile(String url) {
        File cacheFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/10086/"
                + getFileName(url));
        return cacheFile;
    }

    /***
     * 根据链接获取文件名（带类型的），具有唯一性
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
        String fileName = Md5Tool.hashKey(url) + "." + getFileType(url);
        return fileName;
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";
        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = paramString.substring(i + 1);
        return str;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 一定要销毁,否则下次会加载错误
        if (mSuperFileView != null) {
            mSuperFileView.onStopDisplay();
        }
    }
}
