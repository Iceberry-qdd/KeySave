package com.iceberry.keysave.activity.nav;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iceberry.keysave.activity.main.BaseActivity;
import com.iceberry.keysave.activity.main.MainActivity;
import com.iceberry.keysave.activity.nav.setting.AboutActivity;
import com.iceberry.keysave.R;
import com.iceberry.keysave.activity.nav.setting.BackupActivity;
import com.iceberry.keysave.activity.nav.setting.LabActivity;
import com.iceberry.keysave.activity.nav.setting.SecurityActivity;
import com.iceberry.keysave.utils.SharedPreferencesUtil;
import com.iceberry.keysave.utils.StatusBarUtil;

//TODO 完善设置界面
public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout lab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //StatusBarUtil.setStatusColor(SettingsActivity.this, false, true, R.color.colorPrimary);//状态栏反色
        Toolbar toolbar = findViewById(R.id.main_toolbar);//自定义标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            //actionBar.setDisplayShowTitleEnabled(false);
        }
        //SharedPreferencesUtil.getInstance(this,"settings_darkMode");
        boolean isDarkMode=(boolean) SharedPreferencesUtil.getData("darkMode",false);
        ImageView back = findViewById(R.id.settings_back);
        back.setOnClickListener(this);
        RelativeLayout about = findViewById(R.id.settings_about);
        about.setOnClickListener(this);
        RelativeLayout backup = findViewById(R.id.settings_backup);
        backup.setOnClickListener(this);
        RelativeLayout security = findViewById(R.id.settings_security);
        security.setOnClickListener(this);
        lab=findViewById(R.id.settings_lab);
        lab.setOnClickListener(this);
        Switch darkTheme=findViewById(R.id.settings_darkTheme_switch);
        darkTheme.setChecked(isDarkMode);
        darkTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()){
                    SharedPreferencesUtil.putData("darkMode",true);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else {
                    SharedPreferencesUtil.putData("darkMode",false);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //SharedPreferencesUtil.getInstance(this,"");
        boolean isOpenLab = (boolean) SharedPreferencesUtil.getData("isOpenLab", false);
        if (!isOpenLab){
            lab.setVisibility(View.GONE);
        }else {
            lab.setVisibility(View.VISIBLE);
        }
        boolean isDarkMode=(boolean) SharedPreferencesUtil.getData("darkMode",false);
        if (isDarkMode){
            StatusBarUtil.setStatusColor(this, false, false, R.color.colorPrimary);//状态栏反色
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            StatusBarUtil.setStatusColor(this, false, true, R.color.colorPrimary);//状态栏反色
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_back:
                finish();
                break;
            case R.id.settings_about:
                Intent aboutIntent = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.settings_security:
                Intent securityIntent = new Intent(SettingsActivity.this, SecurityActivity.class);
                startActivity(securityIntent);
                break;
            case R.id.settings_backup:
                Intent backupIntent = new Intent(SettingsActivity.this, BackupActivity.class);
                startActivity(backupIntent);
                break;
            case R.id.settings_lab:
                Intent intent = new Intent(SettingsActivity.this, LabActivity.class);
                startActivity(intent);
                //Toast.makeText(this,"Lab",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
