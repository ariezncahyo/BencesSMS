package com.anashidayat.kirimsms;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by anashidayat on 24/07/2017.
 */

public class MyStatic {
    public static String TAG="JEMBUT";
    public static String mypref="anaspref";

    public static String MYINTENT="com.anashidayat.RECEIVE_HTTP";
    private static SharedPreferences sharedPreferences;
    private static MyStatic myStatic;
    public static Context Mcontext;
    public static MyStatic getInstance(Context context){
        if(myStatic==null){
            myStatic=new MyStatic();
        }
        Mcontext=context;
        sharedPreferences=context.getSharedPreferences(mypref,Context.MODE_PRIVATE);
        return myStatic;
    }
    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }
    public String getServer(){

        return sharedPreferences.getString("urlkomputer","");
    }
    public static int getPort(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences(MyStatic.mypref,MODE_PRIVATE);
        return Integer.parseInt(sharedPreferences.getString("portandroid","8000"));
    }
    public String getUrlServer(){
        return Mcontext.getResources().getString(R.string.protocol)+"://"+getServer();
    }
}
