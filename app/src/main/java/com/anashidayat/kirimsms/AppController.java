package com.anashidayat.kirimsms;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.util.Map;

/**
 * Created by anashidayat on 7/26/2016.
 */
public class AppController extends Application  {
    public static final String TAG = "JEMBUT";
    public static final String COOKIENAME="_androiduser";
    private static AppController ourInstance = new AppController();
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    public static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "PHPSESSID";
    public SharedPreferences _preferences;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    public static AppController getInstance(Context context) {
        mContext=context;
        return ourInstance;
    }
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        _preferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    public RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {
            /*
            CookieManager manager = new CookieManager();

            manager.getCookieStore().add(URI.create(
                    getResources().getString(R.string.url)
            ),new HttpCookie(COOKIENAME,String.valueOf(getIdUser())));

            CookieHandler.setDefault( manager  );
            */
            mRequestQueue = Volley.newRequestQueue(mContext);
        }


        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
       // if(mRequestQueue!=null){
        getRequestQueue().add(req);
         //   Log.e(TAG,"REquest queue not null");
        //}
        ///else{
           // Log.e(TAG,"REquest queue is null");
        //}


    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Checks the response headers for session cookie and saves it
     * if it finds it.
     * @param headers Response Headers.
     */
    public final void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPreferences.Editor prefEditor = _preferences.edit();
                prefEditor.putString(SESSION_COOKIE, cookie);
                prefEditor.commit();
            }
        }
    }

    /**
     * Adds session cookie to headers if exists.
     * @param headers
     */
    public final void addSessionCookie(Map<String, String> headers) {
        String sessionId = _preferences.getString(SESSION_COOKIE, "");
       if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }



    }
    public final SharedPreferences mySharedPref(){
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("info",MODE_PRIVATE);
        return sharedPreferences;
    }

    public final int getIdUser(){

       // return _preferences.getInt(COOKIENAME,0);
        return 0;
    }
    public final String getPassword(){
        return _preferences.getString("password","");
    }
}
