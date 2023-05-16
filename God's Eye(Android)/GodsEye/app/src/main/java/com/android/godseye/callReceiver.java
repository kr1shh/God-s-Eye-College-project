package com.android.godseye;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class callReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context,"call",Toast.LENGTH_LONG).show();
//
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        MyPhoneStateListener customPhoneListener = new MyPhoneStateListener();

        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
//

//        String num=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        String num=  intent.getStringExtra("incoming_number");
        Toast.makeText(context, num+".............lik", Toast.LENGTH_LONG).show();


//        serverinsertions ci=new serverinsertions(context);
//        ci.inscall(num,"","","incoming","0");



        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
    }
}


class MyPhoneStateListener extends PhoneStateListener {
    public void onCallStateChange(int state, String incomingNumber){
        Log.d("Lik",incomingNumber);

        switch(state){
            case TelephonyManager.CALL_STATE_RINGING:
                System.out.println("PHONE RINGING………TAKE IT………");
                Log.d("Lik","PHONE RINGING………TAKE IT………");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                System.out.println("CALL_STATE_OFFHOOK………..");
                Log.d("Lik","PHONE STATE OFFHOOK");
                break;
        }

    }
}