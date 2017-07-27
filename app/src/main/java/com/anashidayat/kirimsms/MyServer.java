package com.anashidayat.kirimsms;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.BoolRes;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.InetAddress;
import java.util.prefs.Preferences;

import fi.iki.elonen.NanoHTTPD;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WIFI_SERVICE;
import static com.anashidayat.kirimsms.AppController.TAG;


/**
 * Created by anashidayat on 23/07/2017.
 */

public class MyServer extends NanoHTTPD {
    private static MyServer myServer;
    private Context context;
    private String jsonresult;
    private Boolean issucceed;
    protected MyServer(Context context,int port) {
        super(port);
       this.context=context;

    }
    public void start() throws IOException {

            if (wasStarted()){
                super.stop();
            }
            super.start();



    }
    public static MyServer getInstance(Context context){
        if(myServer==null){
            myServer=new MyServer(context,MyStatic.getPort(context));
        }
        return myServer;
    }
    private void sendSMS(String id, final String phoneNumber, final String message)
    {
        SmsManager sms = SmsManager.getDefault();
        issucceed=false;
        String myphoneNumber=phoneNumber.startsWith("0")?phoneNumber.replaceFirst("0","+62"):phoneNumber;
        Log.i(MyStatic.TAG, "sendSMS: "+myphoneNumber);
        String sent = "SMS_SENT";
        Intent smsintent=new Intent(sent);
        smsintent.putExtra("id",id);
        TelephonyManager  tm=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String androidphonenumber=tm.getLine1Number()!=null?tm.getLine1Number():"";


        PendingIntent smsPI=PendingIntent.getBroadcast(context,0,smsintent,0);

        BroadcastReceiver smsrec=new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                int result=getResultCode();

                String resultstr="";
                if(result== Activity.RESULT_OK){
                    issucceed=true;
                    Log.i(TAG, "onReceive: "+"Kirim sms berhasil");
                   resultstr="RESULT_OK";

                }
                else{
                    issucceed=false;
                    switch (result)
                    {
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                            resultstr="RESULT_ERROR_GENERIC_FAILURE";
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:

                            resultstr="RESULT_ERROR_NO_SERVICE";
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:

                            resultstr="RESULT_ERROR_NULL_PDU";
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            resultstr="RESULT_ERROR_RADIO_OFF";
                            break;
                        default:
                            resultstr="UNKOWN ERROR";
                            break;
                    }

                }
            SharedPreferences pref=MyStatic.getInstance(context).getSharedPreferences();
            String urlsendreport=context.getResources().getString(R.string.protocol)+"://"+pref.getString("urlsendreport","");
                final String finalResultstr = resultstr;
                StringRequest postRequest = new StringRequest(Request.Method.POST, urlsendreport,
                        new com.android.volley.Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                try {
                                    JSONObject jsonobj=new JSONObject(response);
                                    if(jsonobj.getString("succeed").equals("1")){
                                        //RESPONSE BERHASIL
                                    }
                                } catch (JSONException e) {
                                   e.printStackTrace();
                                }


                            }
                        },
                        new com.android.volley.Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(context,"Error kirim ke server "+error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                ) {
                    protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {

                        return super.parseNetworkResponse(response);
                    }
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("result",issucceed.equals(true)?"true":"false");
                        params.put("resultcode", finalResultstr);
                        params.put("id",intent.getStringExtra("id"));
                        params.put("receiver",phoneNumber);
                        params.put("message",message);
                        return params;
                    }
                    @Override


                    public Map<String, String> getHeaders ()throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        // int Iduser=AppController.getInstance(context).getIdUser();
                        //params.put(AppController.COOKIE_KEY, AppController.COOKIENAME+'='+String.valueOf(Iduser)+";");
                        //AppController.getInstance(context).addSessionCookie(params);
                        return params;
                    }

                };
                postRequest.setRetryPolicy(
                        new DefaultRetryPolicy(50000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                );



                AppController.getInstance(context).addToRequestQueue(postRequest);

            }
        };
        context.registerReceiver(smsrec,new IntentFilter(sent));


        JSONObject jsonObject=new JSONObject();
        try {


            String datesent=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
            Log.i(TAG, "sendSMS: "+androidphonenumber+" terkirim tanggal "+datesent);
            jsonObject.put("androidphonenumber",androidphonenumber );
            jsonObject.put("datesent",datesent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonresult=jsonObject.toString();
        sms.sendTextMessage(myphoneNumber, null, message, smsPI, null);

    }
    public Response serve(IHTTPSession session) {
        String message="";
        String sender="";
        String id="";
        try {
            session.parseBody(new HashMap<String, String>());
            message=session.getParms().get("message");
            sender=session.getParms().get("receiver");
            id=session.getParms().get("id");
            sendSMS(id,sender,message);

        } catch (IOException e) {
            Log.e(MyStatic.TAG, "serve: "+e.getMessage() );
        } catch (ResponseException e) {
            Log.e(MyStatic.TAG, "serve: "+e.getMessage());
        }
        Log.i("JEMBUT", jsonresult);
        return newFixedLengthResponse(jsonresult);
    }
}
