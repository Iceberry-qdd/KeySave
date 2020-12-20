package com.iceberry.keysave.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.iceberry.keysave.R;
import com.iceberry.keysave.activity.main.BaseActivity;
import com.iceberry.keysave.utils.SharedPreferencesUtil;
import com.iceberry.keysave.utils.StatusBarUtil;

public class ScanResultActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_result);

        //StatusBarUtil.setStatusColor(ScanResultActivity.this, false, true, R.color.colorPrimary);//状态栏反色

        Toolbar toolbar = findViewById(R.id.scan_result_toolbar);//自定义标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        TextView scanResult = findViewById(R.id.scan_result_content);
        ImageView back = findViewById(R.id.scan_result_back);

        back.setOnClickListener(this);

        Bundle bundle = this.getIntent().getExtras();
        String scanResultText = bundle.getString("scanResult");
        scanResult.setText(scanResultText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_result_back:
                onBackPressed();
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean isDarkMode=(boolean) SharedPreferencesUtil.getData("darkMode",false);
        if (isDarkMode){
            StatusBarUtil.setStatusColor(this, false, false, R.color.colorPrimary);//状态栏反色
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            StatusBarUtil.setStatusColor(this, false, true, R.color.colorPrimary);//状态栏反色
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
