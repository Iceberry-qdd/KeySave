package com.iceberry.keysave.activity.nav.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.iceberry.keysave.R;
import com.iceberry.keysave.activity.main.BaseActivity;
import com.iceberry.keysave.activity.main.FingerLockActivity;
import com.iceberry.keysave.activity.main.NumberLockActivity;
import com.iceberry.keysave.utils.SharedPreferencesUtil;
import com.iceberry.keysave.utils.StatusBarUtil;

public class SecurityActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout fingerOn;
    private RelativeLayout fingerLook;
    private RelativeLayout fingerManger;
    private RelativeLayout changeKey;
    private RelativeLayout useNumberLock;
    private RelativeLayout numberLockOn;
    private boolean INPUT_STATUS=false;
    private Switch startKey;
    private ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_security);
        //StatusBarUtil.setStatusColor(SecurityActivity.this, false, true, R.color.colorPrimary);//状态栏反色
        Toolbar toolbar = findViewById(R.id.setting_security_toolbar);//自定义标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        startKey = findViewById(R.id.settings_security_key_on_switch);
        Switch noScreenshots=findViewById(R.id.settings_security_No_screenshots_switch);
        Switch startFinger=findViewById(R.id.settings_security_fingerprint_on_switch);
        fingerOn=findViewById(R.id.settings_security_fingerprint_on);
        fingerLook=findViewById(R.id.settings_security_fingerprint_look);
        fingerManger=findViewById(R.id.settings_security_fingerprint_manage);
        changeKey=findViewById(R.id.settings_security_key_change);
        changeKey.setOnClickListener(this);
        useNumberLock=findViewById(R.id.settings_security_number_on);
        useNumberLock.setOnClickListener(this);
        numberLockOn=findViewById(R.id.settings_security_number_look);
        back=findViewById(R.id.settings_security_back);
        back.setOnClickListener(this);
        //final Switch finger = findViewById(R.id.settings_security_fingerprint_on_switch);
        //测试sharedPreferenceUtil
        //SharedPreferencesUtil.getInstance(this,"settings_security");
        boolean startKeyStatus = (boolean) SharedPreferencesUtil.getData("startKey", false);
        boolean noScreenshotsStatus=(boolean) SharedPreferencesUtil.getData("notAllowScreenshot",true);

        noScreenshots.setChecked(noScreenshotsStatus);
        noScreenshots.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    SharedPreferencesUtil.putData("notAllowScreenshot",true);
                }else {
                    SharedPreferencesUtil.putData("notAllowScreenshot",false);
                }
            }
        });
        //以下32-50测试SharePreferences功能
        /*
        final SharedPreferences.Editor editor = getSharedPreferences("settings_security", MODE_PRIVATE).edit();
        SharedPreferences settings_security = getSharedPreferences("settings_security", MODE_PRIVATE);
        boolean startKey1 = settings_security.getBoolean("startKey", false);

         */
        startKey.setChecked(startKeyStatus);
        startKey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    //SharedPreferencesUtil.putData("startKey",true);
                    //editor.putBoolean("startKey", true);
                    //editor.apply();

                    //finger.setChecked(false);
                    //finger.setEnabled(false);
                    Intent intent=new Intent(SecurityActivity.this, KeyCreateActivity.class);
                    startActivity(intent);
                } else {
                    SharedPreferencesUtil.putData("numberLockKey","");
                    SharedPreferencesUtil.putData("startKey",false);
                    //SharedPreferencesUtil.putData("startKey",false);
                    //editor.putBoolean("startKey", false);
                    //editor.apply();
                    //finger.setEnabled(true);
                    //Toast.makeText(SecurityActivity.this, "开关关闭", Toast.LENGTH_LONG).show();
                }
            }
        });
        startFinger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    Intent intent=new Intent(SecurityActivity.this, FingerLockActivity.class);
                    startActivityForResult(intent,200);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings_security_key_change:
                break;
            case R.id.settings_security_back:
                finish();
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
        boolean useFingerprint = (boolean) SharedPreferencesUtil.getData("useFingerprint", false);
        if (!useFingerprint){
            fingerOn.setVisibility(View.GONE);
            fingerLook.setVisibility(View.GONE);
            fingerManger.setVisibility(View.GONE);
        }else {
            fingerOn.setVisibility(View.VISIBLE);
            fingerLook.setVisibility(View.VISIBLE);
            fingerManger.setVisibility(View.VISIBLE);
        }
        String numberLockKey = (String) SharedPreferencesUtil.getData("numberLockKey", "");
        if (numberLockKey.equals("")){
            changeKey.setVisibility(View.GONE);
        }else {
            changeKey.setVisibility(View.VISIBLE);
        }
        boolean enableNumberLock = (boolean) SharedPreferencesUtil.getData("enableNumberLock", false);
        if (!enableNumberLock){
            changeKey.setVisibility(View.GONE);
            //useNumberLock.setVisibility(View.GONE);
            numberLockOn.setVisibility(View.GONE);
        }else {
            changeKey.setVisibility(View.VISIBLE);
            //useNumberLock.setVisibility(View.VISIBLE);
            numberLockOn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 200:
                if (resultCode == 200) {
                    startKey.setChecked(true);
                }
                break;
            default:
        }
    }
}
