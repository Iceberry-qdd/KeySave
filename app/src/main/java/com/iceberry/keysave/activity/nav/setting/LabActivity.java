package com.iceberry.keysave.activity.nav.setting;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.iceberry.keysave.R;
import com.iceberry.keysave.activity.main.BaseActivity;
import com.iceberry.keysave.activity.main.FingerLockActivity;
import com.iceberry.keysave.activity.main.NumberLockActivity;
import com.iceberry.keysave.utils.SharedPreferencesUtil;
import com.iceberry.keysave.utils.StatusBarUtil;

public class LabActivity extends BaseActivity implements Switch.OnCheckedChangeListener,View.OnClickListener{

    private Switch enLab;
    private Switch enFingerprint;
    private Switch enRestoreBackup;
    private Switch enNumberLock;
    private Switch enSearch;
    private ImageView back;
    private RelativeLayout numberLockSurface;
    private RelativeLayout fingerprintSurface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab);
        //StatusBarUtil.setStatusColor(LabActivity.this, false, true, R.color.colorPrimary);//状态栏反色
        //SharedPreferencesUtil.getInstance(this,"setting_about");
        setToolbar();
        findView();
        setOnListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSwitchDefaultChecked();
        boolean isDarkMode=(boolean) SharedPreferencesUtil.getData("darkMode",false);
        if (isDarkMode){
            StatusBarUtil.setStatusColor(this, false, false, R.color.colorPrimary);//状态栏反色
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            StatusBarUtil.setStatusColor(this, false, true, R.color.colorPrimary);//状态栏反色
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void setSwitchDefaultChecked() {
        boolean isOpenLab = (boolean) SharedPreferencesUtil.getData("isOpenLab", false);
        enLab.setChecked(isOpenLab);
        boolean useFingerprint = (boolean) SharedPreferencesUtil.getData("useFingerprint", false);
        enFingerprint.setChecked(useFingerprint);
        boolean enableRestoreBackup = (boolean) SharedPreferencesUtil.getData("enableRestoreBackup", false);
        enRestoreBackup.setChecked(enableRestoreBackup);
        boolean enableNumberLock = (boolean) SharedPreferencesUtil.getData("enableNumberLock", false);
        enNumberLock.setChecked(enableNumberLock);
        boolean enableSearch = (boolean) SharedPreferencesUtil.getData("enableSearch", false);
        enSearch.setChecked(enableSearch);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);//自定义标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setOnListener() {
        enLab.setOnCheckedChangeListener(this);
        enFingerprint.setOnCheckedChangeListener(this);
        enRestoreBackup.setOnCheckedChangeListener(this);
        back.setOnClickListener(this);
        numberLockSurface.setOnClickListener(this);
        fingerprintSurface.setOnClickListener(this);
        enNumberLock.setOnCheckedChangeListener(this);
        enSearch.setOnCheckedChangeListener(this);
    }

    private void findView() {
        enLab=findViewById(R.id.settings_lab_op_switch);
        enFingerprint=findViewById(R.id.settings_lab_finger_switch);
        enRestoreBackup=findViewById(R.id.settings_lab_restore_backup_switch);
        back=findViewById(R.id.settings_laboratory_back);
        enNumberLock=findViewById(R.id.settings_lab_number_lock_switch);
        enSearch= findViewById(R.id.settings_lab_search_switch);
        numberLockSurface=findViewById(R.id.settings_lab_number_lock_start);
        fingerprintSurface=findViewById(R.id.settings_lab_fingerprint_start);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.settings_lab_op_switch:
                if (buttonView.isChecked()){
                    SharedPreferencesUtil.putData("isOpenLab",true);
                    //Toast.makeText(this, R.string.ghgh,Toast.LENGTH_SHORT).show();
                }
                if (!buttonView.isChecked()){
                    SharedPreferencesUtil.putData("isOpenLab",false);
                    enFingerprint.setChecked(false);
                    enRestoreBackup.setChecked(false);
                    enNumberLock.setChecked(false);
                    //Toast.makeText(this,"开关关闭",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.settings_lab_finger_switch:
                if (buttonView.isChecked()){
                    SharedPreferencesUtil.putData("useFingerprint",true);
                    //Toast.makeText(this,"开关2打开",Toast.LENGTH_SHORT).show();
                }
                if (!buttonView.isChecked()){
                    SharedPreferencesUtil.putData("useFingerprint",false);
                   // Toast.makeText(this,"开关2关闭",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.settings_lab_restore_backup_switch:
                if (buttonView.isChecked()){
                    SharedPreferencesUtil.putData("enableRestoreBackup",true);
                   // Toast.makeText(this,"开关3打开",Toast.LENGTH_SHORT).show();
                }
                if (!buttonView.isChecked()){
                    SharedPreferencesUtil.putData("enableRestoreBackup",false);
                   // Toast.makeText(this,"开关3关闭",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.settings_lab_number_lock_switch:
                if (buttonView.isChecked()){
                    SharedPreferencesUtil.putData("enableNumberLock",true);
                    //Toast.makeText(this,"开关3打开",Toast.LENGTH_SHORT).show();
                }
                if (!buttonView.isChecked()){
                    SharedPreferencesUtil.putData("enableNumberLock",false);
                    //Toast.makeText(this,"开关3关闭",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.settings_lab_search:
                if (buttonView.isChecked()){
                    SharedPreferencesUtil.putData("enableSearch",true);
                   // Toast.makeText(this,"开关3打开",Toast.LENGTH_SHORT).show();
                }
                if (!buttonView.isChecked()){
                    SharedPreferencesUtil.putData("enableSearch",false);
                   // Toast.makeText(this,"开关3关闭",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings_laboratory_back:
                finish();
                break;
            case R.id.settings_lab_number_lock_start:
                Intent intent1=new Intent(LabActivity.this, NumberLockActivity.class);
                startActivity(intent1);
                break;
            case R.id.settings_lab_fingerprint_start:
                Intent intent2=new Intent(LabActivity.this, FingerLockActivity.class);
                startActivity(intent2);
                break;
        }
    }
}