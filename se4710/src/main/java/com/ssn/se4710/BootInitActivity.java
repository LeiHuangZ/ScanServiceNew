package com.ssn.se4710;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import static android.support.v4.app.NotificationCompat.FLAG_NO_CLEAR;

/**
 * @author HuangLei 1252065297@qq.com
 * @CreateDate 2019/1/18 10:15
 * @UpdateUser 更新者
 * @UpdateDate 2019/1/18 10:15
 * 开机启动当前透明界面，以初始化码制及另外的设置信息
 */
public class BootInitActivity extends Activity {
    private static final String TAG = BootInitActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(android.R.id.content, new ConfigFragment());
        fragmentTransaction.commit();
        LogUtils.i(TAG, "onCreate");
//        boolean isBoot = this.getIntent().getBooleanExtra("isBoot", false);
//        if (isBoot) {
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            Intent toService = new Intent(this.getApplicationContext(), SoftScanService.class);
//            toService.putExtra("fromBoot", true);
            startService(toService);
            setNotification();
//            if (mIsOpen) {
//                if (Util.mIsPad) {
//                    createFloatWindow();
//                }
//            }
            finish();
//        }
    }

    private void setNotification() {
        boolean isOpen = PreferenceManager.getDefaultSharedPreferences(this.getBaseContext()).getBoolean(ConfigFragment.KEY_SWITCH_SCAN, false);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(getString(R.string.notification_title));
        if (isOpen){
            builder.setContentText(getString(R.string.notification_content));
        }else {
            builder.setContentText(this.getResources().getString(R.string.scan_service_stop));
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //添加事件
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(this.getApplicationContext(), ConfigActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this.getApplicationContext(), 0, intent, flags);
        builder.setContentIntent(pi);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.scan));
        Notification notification = builder.build();
        notification.flags |= FLAG_NO_CLEAR;
        assert notificationManager != null;
        notificationManager.notify(R.string.app_name, notification);
    }

    private void createFloatWindow(){
        Intent intent = new Intent(BootInitActivity.this, FloatService.class);
        startService(intent);
    }

}
