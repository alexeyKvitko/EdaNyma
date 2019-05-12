package com.edanyma.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.edanyma.AppConstants;
import com.edanyma.EdaNymaApp;

public class OwnSMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive( Context context, Intent intent ) {
        Bundle bundle = intent.getExtras();
        if( bundle != null ){
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++)
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            for (SmsMessage message : messages) {
                Integer msgIdx = message.getMessageBody().indexOf(AppConstants.SMS_CODE_MSG );
                if( msgIdx > AppConstants.FAKE_ID ){
                    String code = message.getMessageBody().substring( msgIdx+AppConstants.SMS_CODE_MSG.length() );
                    Intent codeIntent = new Intent( AppConstants.SHOW_SMS_CODE );
                    codeIntent.putExtra( AppConstants.SMS_CONFIRM_CODE, code );
                    EdaNymaApp.getAppContext().sendBroadcast( codeIntent );
                }
            }
        }
    }
}

