package com.android.godseye;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class alarmservice extends Service {
    public alarmservice() {
    }

    Handler nd;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        nd= new Handler();
        nd.post(rn);



    }


    Runnable rn= new Runnable() {
        @Override
        public void run() {
            checkalaram();

            nd.postDelayed(rn,5000);

        }
    };

    public void checkalaram()
    {
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = "http://" + sh.getString("ip","") + ":5000/and_alarm";


        Toast.makeText(getApplicationContext(),"ivideee",Toast.LENGTH_LONG).show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        // response
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                String res= jsonObj.getString("res");
                                if(res.equalsIgnoreCase("enabled"))
                                {


                                    int resID=getResources().getIdentifier("beep", "raw", getPackageName());

                                    MediaPlayer mediaPlayer=MediaPlayer.create(getApplicationContext(),resID);
                                    mediaPlayer.start();
                                    Toast.makeText(getApplicationContext(),"Beep",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"No Beep",Toast.LENGTH_LONG).show();
                                }


                            }


                            // }
                            else {
                                Toast.makeText(getApplicationContext(), "Errorrrrrrrrrrrrrrrrrrrrrrrr", Toast.LENGTH_LONG).show();

                            }

                        }    catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();


                params.put("imei",sh.getString("imei",""));


                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS=100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}