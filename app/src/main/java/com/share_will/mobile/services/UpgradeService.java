package com.share_will.mobile.services;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.share_will.mobile.App;
import com.share_will.mobile.MessageEvent;
import com.share_will.mobile.presenter.UpgradeServicePresenter;
import com.share_will.mobile.ui.dialog.DialogActivity;
import com.share_will.mobile.ui.views.UpgradeServiceView;
import com.share_will.mobile.utils.MD5Util;
import com.share_will.mobile.utils.Utils;
import com.ubock.library.base.BaseService;
import com.ubock.library.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;
import java.util.Map;

public class UpgradeService extends BaseService<UpgradeServicePresenter> implements UpgradeServiceView {
    private DownloadManager mDownloadManager;
    private DownloadReceiver mDownloadReceiver;
    private long mDownloadId;
    private int mTryCount;
    //下载失败后最大重试次数
    private int MaxTryCount = 3;
    private String mUrl;
    private String mFilePath;
    public UpgradeService() {
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                download(mUrl);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadReceiver = new DownloadReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mDownloadReceiver, filter);
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("cgd","check new version");
        checkVersion();
        return Service.START_STICKY;
    }

    @Override
    public void onLoadVersion(Map<String, String> data) {
        if (data != null){
            String url = data.get("downloadUrl");
            String description = data.get("description");
            if (!TextUtils.isEmpty(url)){
                mUrl = url;
                Log.d("cgd", url);
                showDialog(description==null?"":description);
                return;
            }
        }
    }

    private void checkVersion(){
        String versionName = Utils.getVersionName(this);
        int versionCode = Utils.getVersionCode(this);
        String channel = App.getChannel();
        String userId = App.getInstance().getUserId();
        getPresenter().checkVersion(versionName, versionCode, 1, channel, userId, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent.DialogActivityEvent event) {
        if (event.ok) {
            download(mUrl);
        }
    }

    private void showDialog(String desc){
        Intent intent = new Intent(this, DialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(DialogActivity.PARAM_TITLE, "发现新版本");
        intent.putExtra(DialogActivity.PARAM_MESSAGE, desc);
        intent.putExtra(DialogActivity.PARAM_NEED_PERMISSION, true);
        intent.putExtra(DialogActivity.PARAM_OK, "升级");
        intent.putExtra(DialogActivity.PARAM_CANCEL, "取消");
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("cgd", "upgrade service destroy");
        unregisterReceiver(mDownloadReceiver);
        mHandler.removeCallbacksAndMessages(null);
        delete();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    private void delete(){
        if (mDownloadId != 0){
            mDownloadManager.remove(mDownloadId);
            mDownloadId = 0;
        }
    }

    private void download(String url) {
        delete();
//        String fileName = MD5Util.MD5Encode(url, "utf-8")+".apk";
        String fileName = url.substring(url.lastIndexOf(File.separator)+1);
        mFilePath = String.format("%s%s%s",
//                getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),
                File.separator,
                fileName);
        Log.d("cgd", "file:"+mFilePath);
        File f = new File(mFilePath);
        if (f.exists() && f.isFile()){
            if (!f.delete()){
                return;
            }
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, fileName);//也可以自己制定下载路径
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Log.d("cgd", "starting download file.");
        mDownloadId = mDownloadManager.enqueue(request);
    }

    class DownloadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleDownload();
        }

        private void handleDownload() {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(mDownloadId);
            Cursor c = mDownloadManager.query(query);
            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch (status) {
                    case DownloadManager.STATUS_PAUSED:
                    case DownloadManager.STATUS_PENDING:
                    case DownloadManager.STATUS_RUNNING:
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        mDownloadId = 0;
                        Log.d("cgd", "新版本下载完成");
                        installAPK(new File(mFilePath));
                        break;
                    case DownloadManager.STATUS_FAILED:
                        Log.d("cgd", "新版本下载失败");
                        if (++mTryCount < MaxTryCount){
                            mHandler.sendEmptyMessageDelayed(0, 5000);
                        } else {
                            mTryCount = 0;
                            stopSelf();
                        }
                        break;
                }
            }
        }

    }

    private void installAPK(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            String packageName = getPackageName(intent);
            LogUtils.d("cgd", "PackageName:"+packageName);
            if (packageName == null) {
                grantUriPermission("com.miui.packageinstaller", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                grantUriPermission("com.google.android.packageinstaller", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                grantUriPermission("com.android.packageinstaller", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } else {
            uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        stopSelf();
    }

    /**
     * 获取应用包名
     * @param intent
     * @return
     */
    public String getPackageName(Intent intent){
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        String packageName;
        for (ResolveInfo item: list) {
            packageName = item.activityInfo.packageName;
            ApplicationInfo info;
            try {
                info = getPackageManager().getApplicationInfo(packageName, 0);
                if ((info.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                    return packageName;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
