package com.anashidayat.kirimsms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;

public class KirimsmsService extends Service {
    private Notification.Builder mNotification;
    private static int notificationId = 1;
    private  NotificationManager manager;
    public KirimsmsService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
      //  throw new UnsupportedOperationException("Not yet implemented");
    return null;
    }
    public void onCreate(){
    super.onCreate();
        WifiManager wm = (WifiManager) this.getSystemService(WIFI_SERVICE);
        String ip="";
        if(wm!=null) {
            ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        }

        mNotification=
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_32bit)
                        .setContentTitle(this.getResources().getString(R.string.app_name))
                        .setContentText("IP android " + ip+":"+
                        MyStatic.getPort(this));



        Intent notificationIntent = new Intent(this, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentintent=PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mNotification.setContentIntent(contentintent);
       manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationId,mNotification.build());

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        try {
            MyServer.getInstance(this).start();
            startForeground(notificationId, mNotification.build());
        } catch (IOException e) {
            Toast.makeText(this,"Gagal start service "+e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return Service.START_STICKY;
    }
    @Override
    public void onDestroy() {
        manager.cancel(notificationId);
        MyServer.getInstance(this).stop();

    }
    public class MyBinder extends Binder {
        public KirimsmsService getService() {
            return KirimsmsService.this;
        }
    }
}
