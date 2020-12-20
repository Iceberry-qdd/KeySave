package com.iceberry.keysave.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.iceberry.keysave.R;

public class FingerLockActivity extends BaseActivity implements View.OnClickListener {

    private TextView fingerToNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_FullScreenTheme);
        setContentView(R.layout.activity_finger_lock);
        findView();
        setOnClickListener();
    }

    private void setOnClickListener() {
        fingerToNumber.setOnClickListener(this);
    }

    private void findView() {
        fingerToNumber=findViewById(R.id.finger_to_number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finger_to_number:
                Intent intent=new Intent(FingerLockActivity.this,NumberLockActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }
}