package com.iceberry.keysave.activity.nav.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.iceberry.keysave.R;
import com.iceberry.keysave.activity.main.BaseActivity;
import com.iceberry.keysave.dialog.SelectFileFormatDialog;
import com.iceberry.keysave.utils.SharedPreferencesUtil;
import com.iceberry.keysave.utils.StatusBarUtil;
import com.iceberry.keysave.utils.ZipUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

public class BackupActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout backupFileFormat;
    private RelativeLayout restoreBackup;
    private RelativeLayout backupFileManager;
    private TextView backupFileFormatText;
    private RelativeLayout startBackup;
    private Context context;
    private RadioGroup radioGroup;


    private String zipPath;
    private String savePath;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_backup);
        //StatusBarUtil.setStatusColor(BackupActivity.this, false, true, R.color.colorPrimary);//状态栏反色
        Toolbar toolbar = findViewById(R.id.setting_backup_toolbar);//自定义标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        backupFileFormat = findViewById(R.id.setting_backup_format);
        backupFileFormatText = findViewById(R.id.setting_backup_format_text);
        startBackup = findViewById(R.id.start_backup);
        radioGroup=findViewById(R.id.select_file_format_radio_group);
        restoreBackup=findViewById(R.id.restore_backup);
        backupFileManager=findViewById(R.id.settings_backup_file_manager);
        back=findViewById(R.id.settings_backup_back);
        back.setOnClickListener(this);

        //测试SharePreferenceUtil
        //SharedPreferencesUtil.getInstance(this,"settings_backup");

        //editor = getSharedPreferences("settings_backup", MODE_PRIVATE).edit();
        //settings_backup = getSharedPreferences("settings_backup", MODE_PRIVATE);


        startBackup.setOnClickListener(this);
        backupFileFormat.setOnClickListener(this);


    }

    private void backup() {
        context = BackupActivity.this;
        zipPath = context.getDatabasePath("KeyStore.db").getPath();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        Date date=new Date(System.currentTimeMillis());
        String Date = format.format(date);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            savePath = Objects.requireNonNull(context.getExternalFilesDir(DIRECTORY_DOCUMENTS)).getAbsolutePath() + "/backup" +"_"+Date+ backupFileFormatText.getText().toString();

            Thread thread = new Thread() {
                @Override
                public void run() {
                    Handler mainHandler = new Handler(Looper.getMainLooper());
                    try {
                        if(ZipUtils.ZipFolder(zipPath, savePath)){

                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, R.string.BackupActivityToast_backupSucceed, Toast.LENGTH_LONG).show();
                                }
                            });
                        }else {
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, R.string.BackupActivityToast_existed, Toast.LENGTH_LONG).show();
                                }
                            });

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("zipPath", zipPath);
                    Log.d("savePath", savePath);

                }
            };
            thread.start();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String fileFormat = (String) SharedPreferencesUtil.getData("fileFormat", ".ksp");
        //String fileFormat = settings_backup.getString("fileFormat", ".ksp");
        backupFileFormatText.setText(fileFormat);

        boolean enableRestoreBackup = (boolean) SharedPreferencesUtil.getData("enableRestoreBackup", false);
        if (!enableRestoreBackup){
            restoreBackup.setVisibility(View.GONE);
            backupFileManager.setVisibility(View.GONE);
        }else {
            restoreBackup.setVisibility(View.VISIBLE);
            backupFileManager.setVisibility(View.VISIBLE);
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
            case R.id.setting_backup_format:

                showDialog();
                break;
            case R.id.start_backup:
                backup();
                break;
            case R.id.settings_backup_back:
                finish();
                break;
        }
    }


    /**
     * 显示对话框
     */
    private void showDialog() {
        final SelectFileFormatDialog selectFileFormatDialog =
                new SelectFileFormatDialog(this,getString(R.string.selectFileFormatDialog_title),
                        getString(R.string.selectFileFormatDialog_okBtn),
                        getString(R.string.selectFileFormatDialog_cancelBtn));
        selectFileFormatDialog.setCancelOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFileFormatDialog.close();
            }
        });
        selectFileFormatDialog.setOkOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = selectFileFormatDialog.getItem();
                backupFileFormatText.setText(item);
                SharedPreferencesUtil.putData("fileFormat",item);
                //editor.putString("fileFormat", item);
                //editor.apply();
                //Toast.makeText(BackupActivity.this, item, Toast.LENGTH_SHORT).show();
                selectFileFormatDialog.close();
            }
        });
        selectFileFormatDialog.show();
    }
}
