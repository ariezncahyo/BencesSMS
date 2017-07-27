package com.anashidayat.kirimsms;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.telephony.SmsMessage.createFromPdu;

public class SmsReceiver extends BroadcastReceiver {
    public SmsReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Bundle extras = intent.getExtras();

        String strMessage = "";

        if ( extras != null )
        {
            Object[] smsextras = (Object[]) extras.get( "pdus" );
            String format = extras.getString("format");
            SmsMessage smsmsg;
            String strMsgBody="";
            String strMsgSrc="";
            for ( int i = 0; i < smsextras.length; i++ )

            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                smsmsg=createFromPdu((byte[]) smsextras[i], format);

            }
                else{
                smsmsg = createFromPdu((byte[])smsextras[i]);
            }

                strMsgBody = smsmsg.getMessageBody().toString();
                strMsgSrc = smsmsg.getOriginatingAddress();

                strMessage += strMsgSrc + " : " + strMsgBody;


            }
            String server=MyStatic.getInstance(context).getUrlServer();
            final String finalStrMsgSrc = strMsgSrc;
            final String finalStrMsgBody = strMsgBody;
            StringRequest postRequest = new StringRequest(Request.Method.POST, server,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            try {
                                JSONObject jsonobj=new JSONObject(response);
                                if(jsonobj.getString("succeed").equals("1")){
                                    Toast.makeText(context,"kirim ke server berhasil",Toast.LENGTH_LONG)
                                            .show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(context,"kirim ke server gagal"+e.getMessage(),Toast.LENGTH_LONG)
                                        .show();
                            }


                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Toast.makeText(context,"Error kirim ke server "+error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
            ) {
                protected Response<String> parseNetworkResponse(NetworkResponse response) {

                    return super.parseNetworkResponse(response);
                }
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("sender", finalStrMsgSrc);
                    params.put("message", finalStrMsgBody);
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

    }
    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}
