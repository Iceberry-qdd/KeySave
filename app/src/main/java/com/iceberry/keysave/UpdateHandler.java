package com.iceberry.keysave;

import android.os.Handler;
import android.os.Message;

public class UpdateHandler extends Handler {
    private static final int MSG_UPDATE = 300;
    private KeyAdapter mAdapter;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPDATE:
               // mAdapter.setDatas(mDatas);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }
}

