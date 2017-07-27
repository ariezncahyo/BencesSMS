package com.anashidayat.kirimsms;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by anashidayat on 25/07/2017.
 */

public class KirimSmsConnection {
    private KirimsmsService m_service;
    private Boolean misBound;
    private ServiceConnection m_serviceConnection;
    private static KirimSmsConnection kirimSmsConnection;
    private Context context;
    public KirimSmsConnection(Context context){
        this.context=context;
        misBound=false;
        m_serviceConnection= new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                m_service = ((KirimsmsService.MyBinder)service).getService();
            }

            public void onServiceDisconnected(ComponentName className) {
               // m_service = null;
            }};
    }
    public static KirimSmsConnection getInstance(Context context){
        if(kirimSmsConnection==null){
            kirimSmsConnection=new KirimSmsConnection(context);
        }
        return kirimSmsConnection;
    }
    public void doUnbindService() {

            // Detach our existing connection.

        if(misBound) {
            context.unbindService(m_serviceConnection);
            misBound=false;
        }

    }
    public void doBindService(){
        context.bindService(new Intent(context,KirimsmsService.class),m_serviceConnection,BIND_AUTO_CREATE);
        misBound=true;
    }
}
