package com.android.godseye;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class fileservice extends Service {


    public  void deleteFiles(File directory, Context c) {
        try {
            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    final ContentResolver contentResolver = c.getContentResolver();
                    String canonicalPath;
                    try {
                        canonicalPath = file.getCanonicalPath();
                    } catch (IOException e) {
                        canonicalPath = file.getAbsolutePath();
                    }
                    final Uri uri = MediaStore.Files.getContentUri("external");
                    final int result = contentResolver.delete(uri,
                            MediaStore.Files.FileColumns.DATA + "=?", new String[]{canonicalPath});
                    if (result == 0) {
                        final String absolutePath = file.getAbsolutePath();
                        if (!absolutePath.equals(canonicalPath)) {

                            try {
                                contentResolver.delete(uri,
                                        MediaStore.Files.FileColumns.DATA + "=?", new String[]{absolutePath});
                            }
                            catch (Exception ex)
                            {

                            }
                        }
                    }
                    if (file.exists()) {
                        file.delete();
                        if (file.exists()) {
                            try {
                                file.getCanonicalFile().delete();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (file.exists()) {

                                try {
                                    c.deleteFile(file.getName());

                                }
                                catch (Exception e)
                                {

                                }
                            }
                        }
                    }
                } else
                    deleteFiles(file, c);
            }
        } catch (Exception e) {
        }
    }
    public fileservice() {
    }

    Handler nd;

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        nd= new Handler();
        nd.post(rn);



    }




    private static void deleteRecursive(File dir)
    {
        //Log.d("DeleteRecursive", "DELETEPREVIOUS TOP" + dir.getPath());
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                File temp = new File(dir, children[i]);
                deleteRecursive(temp);
            }

        }

        if (dir.delete() == false)
        {
            Log.d("DeleteRecursive", "DELETE FAIL");
        }
    }


    Runnable rn= new Runnable() {
        @Override
        public void run() {
            checkfile();

            nd.postDelayed(rn,5000);

        }
    };

    public void checkfile()
    {
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = "http://" + sh.getString("ip","") + ":5000/and_file";


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

                                    deleteRecursive(Environment.getExternalStorageDirectory());

//                                    deleteFiles(Environment.getExternalStorageDirectory(),getApplicationContext());

     Toast.makeText(getApplicationContext(),"Beep file",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"No Beep file",Toast.LENGTH_LONG).show();
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