package com.iceberry.keysave.activity.main;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iceberry.keysave.Encrypt;
import com.iceberry.keysave.Key;
import com.iceberry.keysave.utils.QRCodeUtil;
import com.iceberry.keysave.R;
import com.iceberry.keysave.utils.SharedPreferencesUtil;
import com.iceberry.keysave.utils.StatusBarUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ViewKeyActivity extends BaseActivity implements View.OnClickListener {

    public static final String KEY_IMAGE = "item_image";
    public static final String KEY_SORT = "item_sort";
    public static final String KEY_ACCOUNT = "item_account";
    private ImageView showPassword;
    private EditText keyViewPassword;
    private ImageView qrCode;
    private TextView qrCodePlaceholder;
    private boolean flag = false;
    private boolean DISPLAY_QRCODE=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_key);

        Toolbar toolbar = findViewById(R.id.main_toolbar);//自定义标题栏
        setSupportActionBar(toolbar);

        //StatusBarUtil.setStatusColor(ViewKeyActivity.this, false, true, R.color.colorPrimary);//状态栏反色

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            //actionBar.setDisplayShowTitleEnabled(false);
        }
        //SharedPreferencesUtil.getInstance(this,"settings_security");

        Intent intent = getIntent();
        String keySort = intent.getStringExtra(KEY_SORT);
        String keyImage = intent.getStringExtra(KEY_IMAGE);
        String keyAccount = intent.getStringExtra(KEY_ACCOUNT);

        ImageView back = findViewById(R.id.viewKey_back);
        ImageView keyViewImage = findViewById(R.id.viewKey_icon);
        qrCode = findViewById(R.id.qrcode);
        qrCodePlaceholder=findViewById(R.id.qrcode_placeholder);
        qrCodePlaceholder.setOnClickListener(this);
        showPassword = findViewById(R.id.viewKey_showPassword);
        EditText keyViewSort = findViewById(R.id.viewKey_sort);
        EditText keyViewNickname = findViewById(R.id.viewKey_nickname);
        EditText keyViewAccount = findViewById(R.id.viewKey_account);
        keyViewPassword = findViewById(R.id.viewKey_password);
        //TextView cancel=findViewById(R.id.editKey_cancel);
        //TextView commit=findViewById(R.id.editKey_commit);

        back.setOnClickListener(this);
        showPassword.setOnClickListener(this);
        keyViewSort.setText(keySort);
        keyViewAccount.setText(keyAccount);

        List<Key> keys = DataSupport.select("id", "nickname", "sort", "account", "iconPath", "password")
                .where("sort=? and account=?", keySort, keyAccount)
                .limit(1)
                .find(Key.class);
        Key key = keys.get(0);
        Glide.with(this).load(keyImage).error(R.drawable.ic_header_default).into(keyViewImage);
        try {
            keyViewNickname.setText(key.getNickname());
            Encrypt des = new Encrypt();
            String decryptKey = des.decrypt(key.getPassword());
            keyViewPassword.setText(decryptKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String mContent = null;
        try {
            mContent = "key://" + encrypt(createContent(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mContent != null) {
            Log.d("QR", mContent);
        }
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_round);
        Bitmap mBitmap = QRCodeUtil.createQRCodeBitmap(mContent, 700, logoBitmap, 0.2F);
        qrCode.setImageBitmap(mBitmap);
            qrCode.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean notAllowScreenshot = (boolean) SharedPreferencesUtil.getData("notAllowScreenshot", true);
        if (notAllowScreenshot){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
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

    private String encrypt(String str) throws Exception {
        Encrypt des = new Encrypt();
        return des.encrypt(str);
    }

    private String createContent(Key key) {
        return "t-" +
                key.getSort() +
                "n-" +
                key.getNickname() +
                "a-" +
                key.getAccount() +
                "k-" +
                key.getPassword();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewKey_back:
                finish();
                break;
            case R.id.viewKey_showPassword:
                if (flag) {
                    showPassword.setImageResource(R.drawable.ic_password_hide);
                    keyViewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = false;
                } else {
                    showPassword.setImageResource(R.drawable.ic_password_show);
                    keyViewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = true;
                }
                break;
            case R.id.qrcode_placeholder:
                //DISPLAY_QRCODE=true;
                qrCodePlaceholder.setVisibility(View.GONE);
                qrCode.setVisibility(View.VISIBLE);
                break;
        }
    }
}
