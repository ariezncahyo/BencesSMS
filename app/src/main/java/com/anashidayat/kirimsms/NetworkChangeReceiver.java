package com.anashidayat.kirimsms;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static com.anashidayat.kirimsms.AppController.TAG;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mNotification;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i(TAG, "onReceive: NETWORK CHANGE");

        String ip=MyStatic.getInstance(context).getLocalIpAddress(context);
        String message="";
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connection = manager.getActiveNetworkInfo();
        if(null!=connection){
            if((connection.getType()==ConnectivityManager.TYPE_ETHERNET) || (connection.getType()==
            ConnectivityManager.TYPE_WIFI)){
               message= "IP android " + ip+":"+
                        MyStatic.getPort(context);

            }
        }
        else{
            message="terjadi masalah dengan sambungan";
        }

        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotification=
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_32bit)
                        .setContentTitle(context.getResources().getString(R.string.app_name))
                        .setContentText(message);
        mNotificationManager.notify(
                KirimsmsService.notificationId,
                mNotification.build());
    }
    private void stopservice(Context context){
        Intent myintent=new Intent(context,KirimsmsService.class);
        context.stopService(myintent);


    }
}
