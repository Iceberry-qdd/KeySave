package com.iceberry.keysave.activity.nav.setting;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iceberry.keysave.R;
import com.iceberry.keysave.activity.main.BaseActivity;
import com.iceberry.keysave.utils.SharedPreferencesUtil;
import com.iceberry.keysave.utils.StatusBarUtil;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private int CLICK_TIMES=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_about);
        //StatusBarUtil.setStatusColor(AboutActivity.this, false, true, R.color.colorPrimary);//状态栏反色
        Toolbar toolbar = findViewById(R.id.main_toolbar);//自定义标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            //actionBar.setDisplayShowTitleEnabled(false);
        }
        ImageView back = findViewById(R.id.about_back);
        back.setOnClickListener(this);
        TextView versionName = findViewById(R.id.about_version);
        versionName.setText(packageName(versionName.getContext()));
        ImageView icon=findViewById(R.id.about_icon);
        icon.setOnClickListener(this);
        //SharedPreferencesUtil.getInstance(this,"setting_about");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_back:
                finish();
                break;
            case R.id.about_icon:
                enterLaboratory();
        }
    }

    private void enterLaboratory() {
        boolean isOpenLab = (boolean) SharedPreferencesUtil.getData("isOpenLab", false);
        if (CLICK_TIMES==5){
            if (!isOpenLab)
            SharedPreferencesUtil.putData("isOpenLab",true);
        }

        if (CLICK_TIMES<5){
            //boolean isOpenedLaboratory = (boolean) SharedPreferencesUtil.getData("isOpenedLaboratory", false);

            if (!isOpenLab){
                CLICK_TIMES++;
                Toast.makeText(this,"Click "+(5-CLICK_TIMES)+" times to enter the laboratory",Toast.LENGTH_SHORT).show();
            }
        }
        if (isOpenLab)
            Toast.makeText(this,"You have opened the laboratory",Toast.LENGTH_LONG).show();
    }

    public static String packageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    protected void onStart() {
        super.onStart();
        CLICK_TIMES=0;
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
