package com.iceberry.keysave.activity.main;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.iceberry.keysave.R;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import org.jetbrains.annotations.NotNull;

public class CustomCaptureActivity extends BaseActivity implements DecoratedBarcodeView.TorchListener, View.OnClickListener {

    //private Button switchLight;
    private ImageView switchLight;
    private ImageView back;
    private boolean isLightOn = false;
    /**
     * 条形码扫描管理器
     */
    private CaptureManager mCaptureManager;

    /**
     * 条形码扫描视图
     */
    private DecoratedBarcodeView mBarcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_scan);

        //switchLight = findViewById(R.id.btn_switch);
        switchLight = findViewById(R.id.switch_light);
        back = findViewById(R.id.scan_back);
        mBarcodeView = (DecoratedBarcodeView) findViewById(R.id.zxing_barcode_scanner);
        mBarcodeView.setTorchListener(this);
        if (!hasFlash()) {
            switchLight.setVisibility(View.GONE);
        }
        mCaptureManager = new CaptureManager(this, mBarcodeView);
        mCaptureManager.initializeFromIntent(getIntent(), savedInstanceState);
        mCaptureManager.decode();
        switchLight.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCaptureManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCaptureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCaptureManager.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mCaptureManager.onSaveInstanceState(outState);
    }

    /**
     * 权限处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mCaptureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 按键处理
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mBarcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTorchOn() {
        isLightOn = true;
    }

    @Override
    public void onTorchOff() {
        isLightOn = false;
    }

    // 判断是否有闪光灯功能
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_light:
                if (isLightOn) {
                    mBarcodeView.setTorchOff();
                    switchLight.setImageResource(R.drawable.ic_light_off);
                } else {
                    mBarcodeView.setTorchOn();
                    switchLight.setImageResource(R.drawable.ic_light_open);
                }
                break;
            case R.id.scan_back:
                finish();
                break;
        }
    }
}
