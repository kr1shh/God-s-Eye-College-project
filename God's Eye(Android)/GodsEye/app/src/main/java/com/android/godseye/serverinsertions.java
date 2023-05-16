package com.android.godseye;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.internal.PackageReference;

public class serverinsertions {
    Context c;
    SharedPreferences sh;
       public serverinsertions(Context c) {
        this.c=c;

    }


    public void inscall(String phone,String date,String time,String type,String duration) {
        sh=PreferenceManager.getDefaultSharedPreferences(c);
        String url = "http://" + sh.getString("ip","") + ":5000/call_insert_post";

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        // response
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                Toast.makeText(c, "Not found", Toast.LENGTH_LONG).show();
                            }


                            // }
                            else {
                                Toast.makeText(c, "Not found", Toast.LENGTH_LONG).show();
                            }

                        }    catch (Exception e) {
                            Toast.makeText(c, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(c, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(c);
                Map<String, String> params = new HashMap<String, String>();

                params.put("Phone_number",phone);
                params.put("Time",time);
                params.put("Date",date);
                params.put("Call_type",type);
                params.put("Duration",duration);
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


    public void insmsg(String phone,String date,String time,String msg,String type){
        sh=PreferenceManager.getDefaultSharedPreferences(c);

        String url = "http://" + sh.getString("ip","") + ":5000/msg_insert_post";



        RequestQueue requestQueue = Volley.newRequestQueue(c);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        // response
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                Toast.makeText(c, "Not found", Toast.LENGTH_LONG).show();


                            }


                            // }
                            else {
                                Toast.makeText(c, "Not found", Toast.LENGTH_LONG).show();
                            }

                        }    catch (Exception e) {
                            Toast.makeText(c, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(c, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(c);
                Map<String, String> params = new HashMap<String, String>();

                params.put("Phone",phone);
                params.put("Time",time);
                params.put("Date",date);
                params.put("Message",msg);
                params.put("type",type);
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
public void  touch(String ss)
{
    if (ss.equalsIgnoreCase("on"))
    {
        Intent iis=new Intent(c,GlobalTouchService.class);
        c.stopService(iis);
    }
    else{

        Intent iis=new Intent(c,GlobalTouchService.class);
        c.startService(iis);
    }

}

public void inslocation(String latitude,String longitude){

   SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(c);
    String url = "http://" + sh.getString("ip","") + ":5000/inslocation";


Toast.makeText(c,"ivideee",Toast.LENGTH_LONG).show();
    RequestQueue requestQueue = Volley.newRequestQueue(c);
    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                     Toast.makeText(c, response, Toast.LENGTH_LONG).show();

                    // response
                    try {
                        JSONObject jsonObj = new JSONObject(response);
                        if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                            SharedPreferences.Editor ed1 = sh.edit();

                            ed1.putString("touch", jsonObj.getString("touch"));

                            ed1.commit();
                            if(jsonObj.getString("touch").equalsIgnoreCase("Enabled"))
                            {touch("on");
                            }
                            else {
                               touch("off");
                            }
if(!jsonObj.getString("zone").equalsIgnoreCase(""))
{
    SharedPreferences.Editor ed = sh.edit();
    ed.putString("zone", jsonObj.getString("zone"));
    ed.putString("Phone_number", jsonObj.getString("Phone_number"));

    ed.commit();
}
                            Toast.makeText(c, "Location Updated", Toast.LENGTH_LONG).show();


                        }


                        // }
                        else {
                            Toast.makeText(c, "Errorrrrrrrrrrrrrrrrrrrrrrrr", Toast.LENGTH_LONG).show();

                        }

                    }    catch (Exception e) {
                        Toast.makeText(c, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // error
                    Toast.makeText(c, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }
    ) {
        @Override
        protected Map<String, String> getParams() {
            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(c);
            Map<String, String> params = new HashMap<String, String>();


            params.put("latitude",latitude);
            params.put("longitude",longitude);
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


    public void inszone(){
        sh=PreferenceManager.getDefaultSharedPreferences(c);

        String url = "http://" + sh.getString("ip","") + ":5000/zone_insert_post";



        RequestQueue requestQueue = Volley.newRequestQueue(c);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        // response
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                Toast.makeText(c, "Not found", Toast.LENGTH_LONG).show();


                            }


                            // }
                            else {
                                Toast.makeText(c, "Not found", Toast.LENGTH_LONG).show();
                            }

                        }    catch (Exception e) {
                            Toast.makeText(c, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(c, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(c);
                Map<String, String> params = new HashMap<String, String>();

//                params.put("zone",zone);
//                params.put("date",date);
//                params.put("time",time);
//                params.put("lid",lid);
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

    public void insbrow(String date,String time,String bro_hist){
        sh=PreferenceManager.getDefaultSharedPreferences(c);

        String url = "http://" + sh.getString("ip","") + ":5000/brow_insert_post";



        RequestQueue requestQueue = Volley.newRequestQueue(c);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        // response
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                Toast.makeText(c, "Not found", Toast.LENGTH_LONG).show();


                            }


                            // }
                            else {
                                Toast.makeText(c, "Not found", Toast.LENGTH_LONG).show();
                            }

                        }    catch (Exception e) {
                            Toast.makeText(c, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(c, "err" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(c);
                Map<String, String> params = new HashMap<String, String>();


                params.put("Date",date);
                params.put("Time",time);
                params.put("bro_hist",bro_hist);
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


    public void inszonealert(String zaid,String userlid,String date,String time){
        sh=PreferenceManager.getDefaultSharedPreferences(c);

        String url = "http://" + sh.getString("ip","") + ":5000/inszonealert";



        RequestQueue requestQueue = Volley.newRequestQueue(c);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        // response
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                Toast.makeText(c, "Not found", Toast.LENGTH_LONG).show();


                            }


                            // }
                            else {
                                Toast.makeText(c, "Not found", Toast.LENGTH_LONG).show();
                            }

                        }    catch (Exception e) {
                            Toast.makeText(c, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(c, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(c);
                Map<String, String> params = new HashMap<String, String>();

                params.put("zaid",zaid);
                params.put("USERLID",userlid);
                params.put("date",date);
                params.put("time",time);


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

//
//    public void inslocation(String date,String time,String latitude,String longitude){
//
//
//        String url = "http://" + sh.getString("ip","") + ":5000/inslocation";
//
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(c);
//        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
//
//                        // response
//                        try {
//                            JSONObject jsonObj = new JSONObject(response);
//                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
//
//                                Toast.makeText(c, "Not found", Toast.LENGTH_LONG).show();
//
//
//                            }
//
//
//                            // }
//                            else {
//                                Toast.makeText(c, "Not found", Toast.LENGTH_LONG).show();
//                            }
//
//                        }    catch (Exception e) {
//                            Toast.makeText(c, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // error
//                        Toast.makeText(c, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(c);
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("date",date);
//                params.put("time",time);
//                params.put("latitude",latitude);
//                params.put("longitude",longitude);
//
//
//                return params;
//            }
//        };
//
//        int MY_SOCKET_TIMEOUT_MS=100000;
//
//        postRequest.setRetryPolicy(new DefaultRetryPolicy(
//                MY_SOCKET_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(postRequest);
//









}

























