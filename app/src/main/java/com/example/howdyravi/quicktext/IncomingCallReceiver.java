package com.example.howdyravi.quicktext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;


public class IncomingCallReceiver extends BroadcastReceiver {

    static int isIncomingCallEnded=-1;
    String msg="Default msg";
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Log.d("onReceive", "Phone state Changed");
            String state=intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String mobno=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            SharedPreferences sharedPreferences=context.getSharedPreferences("msgFileKey", Context.MODE_PRIVATE);
            msg=sharedPreferences.getString("msgKey","0");
            String service=sharedPreferences.getString("service","0");


            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
            {
                isIncomingCallEnded++;
                Log.d("IsIncomingCallEnded",isIncomingCallEnded+"");
                Log.d("state","Ringing");
                Log.d("Number Ringing",mobno);


            }
            if(state.equals(TelephonyManager.EXTRA_STATE_IDLE))
            {
                isIncomingCallEnded++;
                Log.d("state","IDLE");
                Log.d("IsIncomingCallEnded",isIncomingCallEnded+"");
                if(isIncomingCallEnded==1)
                {
                    Log.d("IsIncomingCallEnded",isIncomingCallEnded+"");
                    Log.d("state:","Incoming call Stopped ringing");

                    if(service.equals("on")) {
                        Log.d("message", msg);

                    SmsManager smsManager= SmsManager.getDefault();
                    smsManager.sendTextMessage(mobno,null,msg,null,null);
                    }
                    isIncomingCallEnded=-1;
                }
            }

            if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
            {
                Log.d("state","OOFHOOK");
                isIncomingCallEnded=-2;
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
