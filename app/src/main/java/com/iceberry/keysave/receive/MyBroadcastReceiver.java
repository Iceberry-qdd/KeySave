package com.iceberry.keysave.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.iceberry.keysave.ActivityCollector;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Test",Toast.LENGTH_LONG).show();
    }
}
