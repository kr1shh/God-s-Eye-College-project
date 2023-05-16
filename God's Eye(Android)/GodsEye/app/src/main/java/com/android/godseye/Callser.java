package com.android.godseye;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
//
//import com.android.internal.telephony.ITelephony;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Callser extends Service {

    String opn;
    String dt = "", tm = "";
    long diffinmin, diffinhr;
    TelephonyManager telephonyManager;
    TelephonyManager telman;

    public static int flg = 0;
    String phnop = "";

    SharedPreferences sh;


    String url = "";
    String imei;


    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(), "service started", Toast.LENGTH_SHORT).show();

        // TODO Auto-generated method stub
        super.onCreate();


        try {

            if (Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        TelephonyManager tel = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            }
        }
//        imei = tel.getDeviceId().toString();
        imei="12345678";

        SimpleDateFormat tet = new SimpleDateFormat("hh:mm:ss");
        tm = tet.format(new Date());
        telman = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        //imei=telman.getDeviceId().toString();

        telman.listen(phlist, PhoneStateListener.LISTEN_CALL_STATE);
        Log.d("....old...", ".....00");

    }

    public PhoneStateListener phlist = new PhoneStateListener() {
        public void onCallStateChanged(int state, String inNum) {

            switch (state) {


                case TelephonyManager.CALL_STATE_IDLE:

                    SimpleDateFormat dd = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat tt = new SimpleDateFormat("hh:mm:ss");
                    String d = dd.format(new Date());
                    String t = tt.format(new Date());
                    String duration = "";
                    long tmdiff = 0;
                    //	Log.d("....old...", ".....3");
                    try {
                        Date dt1 = tt.parse(t);
                        Date dt2 = tt.parse(tm);
                        tmdiff = dt1.getTime() - dt2.getTime();
                        tmdiff = TimeUnit.MILLISECONDS.toSeconds(tmdiff);
                        diffinmin = tmdiff / (60);
                        diffinhr = diffinmin / (60);
                        tmdiff -= (diffinmin * 60);
                        duration = diffinhr + ":" + diffinmin + ":" + tmdiff;
                        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        Editor edit = shp.edit();
                        edit.putString("duration", duration);
                        edit.commit();
                        Toast.makeText(getApplicationContext(), "call duration" +duration, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(), "error1 in call:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("error1", e.getMessage());
                    }
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    String name = preferences.getString("callstatus", "hi");
                    if (name.equalsIgnoreCase("incoming")) {
                        Log.d("....1....", "..incall..");
                        SharedPreferences shpr = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        Editor edit1 = shpr.edit();
                        edit1.commit();
                        try {
                            String ss = shpr.getString("incoming number", "");
                            serverinsertions ci=new serverinsertions(getBaseContext());
                            ci.inscall(ss,"","","incoming",duration);

                            //call(MainActivity.phoneid,phnop,"incoming",duration,d,t);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getApplicationContext(), "error2 in call:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("error2", e.getMessage());
                        }
                        SharedPreferences sh1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String ss = sh1.getString("incoming number", "");
                        String dur = sh1.getString("duration", "");



                        Toast.makeText(getApplicationContext(), "hellloooooo"+ss + "\n" + "incoming call" + "\n" + dur, Toast.LENGTH_LONG).show();
                        flg = 0;
                    } else if (flg == 1) {
                        Log.d("....1....", "..outcall..");
                        try {
                            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            opn = sh.getString("num", "");
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getApplicationContext(), "error3 in call:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("error3", e.getMessage());
                        }
                        SharedPreferences sh2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                        String dur = sh2.getString("duration", "");
                        Toast.makeText(getApplicationContext(), opn + "\n" + "Outgoing call" + "\n" + dur, Toast.LENGTH_LONG).show();

                        serverinsertions a=new serverinsertions(getApplicationContext());
                        a.inscall(opn," "," ","Outgoing",dur);




                        flg = 0;
                    }


                    Editor editor = preferences.edit();
                    editor.putString("callstatus", "idle");
                    editor.commit();

                    break;


                case TelephonyManager.CALL_STATE_OFFHOOK:

                    SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sn = new SimpleDateFormat("hh:mm:ss");

                    flg = 1;

                    dt = sm.format(new Date());
                    tm = sn.format(new Date());
                    Toast.makeText(getApplicationContext(), dt + "  " + tm, Toast.LENGTH_LONG).show();

                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String opn = pref.getString("num", "");




                    if (opn.equalsIgnoreCase("")) {
                        opn = phnop;
                    }

                    SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String blknum = sh.getString("block", "");

                    int xy = 0;
                    Log.d("...outn..", blknum + "..outn.." + opn);
                    if (!blknum.equalsIgnoreCase("#")) {

                        String b[] = blknum.split("#");
                        for (int i = 0; i < b.length; i++) {
                            if (b[i].length() >= 10 && opn.length() >= 10) {
                                b[i] = b[i].substring(b[i].length() - 10, b[i].length());
                                opn = opn.substring(opn.length() - 10, opn.length());
                                Log.d("....outnum....", b[i] + "..b[i]..outnum.." + opn);
                                //

                            }
                            if (b[i].equals(opn)) {
                                xy = 1;
                            }
                        }
                    }

                    if (xy == 1) {
                        ////call reject
                        try {

                            Log.d("...rnggg..", "cutng........");
//
//                            telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//                            Class c = Class.forName(telephonyManager.getClass().getName());
//                            Method m = c.getDeclaredMethod("getITelephony");
//                            m.setAccessible(true);
//                            ITelephony telephonyService = (ITelephony) m.invoke(telephonyManager);
//                            telephonyService.endCall();

                        } catch (Exception e) {
                            // TODO: handle exception
                            Toast.makeText(getApplicationContext(), "error4 in call:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("error4", e.getMessage());
                        }
                    }
                    break;


                case TelephonyManager.CALL_STATE_RINGING:

                    phnop = inNum;
                    Toast.makeText(getApplicationContext(), phnop, Toast.LENGTH_LONG).show();
                    sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Editor edd = sh.edit();
                    edd.putString("incoming number", phnop);
                    edd.commit();

                    blknum = sh.getString("block", "");

                    // saving the incoming number

                    Editor ed = sh.edit();
                    ed.putString("callstatus", "incoming");
                    ed.putString("num", inNum);
                    ed.commit();
                    ///ends
                    int xyz = 0;
                    break;
            }

        }


    };



        public IBinder onBind(Intent arg0) {

            return null;
        }}



