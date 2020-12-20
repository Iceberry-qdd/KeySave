package com.iceberry.keysave.activity.nav.setting;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.iceberry.keysave.R;
import com.iceberry.keysave.activity.main.BaseActivity;
import com.iceberry.keysave.utils.SharedPreferencesUtil;
import com.iceberry.keysave.utils.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KeyCreateActivity extends BaseActivity implements View.OnClickListener {
    private TextView[] input = new TextView[4];
    private TextView passwordNotify;

    private Button[] keyboard = new Button[10];
    private Button keyboardForget;
    private Button keyboardCancel;

    private String inputPassword1 = "";
    private String inputPassword2="";

    private int INPUT_TIME = 1;
    private boolean INPUT_STATUS=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_FullScreenTheme);
        setContentView(R.layout.activity_number_lock);
        //StatusBarUtil.setStatusColor(KeyCreateActivity.this, false, true, R.color.colorPrimary);//状态栏反色
        //setToolbar();
        findView();
        setOnListener();
        setDefaultVisible();
    }

    private void setDefaultVisible() {
        keyboardForget.setVisibility(View.INVISIBLE);
    }

    private void setOnListener() {
        for (int i = 0; i < 10; i++) {
            keyboard[i].setOnClickListener(this);
        }
        keyboardCancel.setOnClickListener(this);
        keyboardForget.setOnClickListener(this);
    }

    private void findView() {
        input[0] = findViewById(R.id.password_input1);
        input[1] = findViewById(R.id.password_input2);
        input[2] = findViewById(R.id.password_input3);
        input[3] = findViewById(R.id.password_input4);
        keyboard[0] = findViewById(R.id.password_keyboard0);
        keyboard[1] = findViewById(R.id.password_keyboard1);
        keyboard[2] = findViewById(R.id.password_keyboard2);
        keyboard[3] = findViewById(R.id.password_keyboard3);
        keyboard[4] = findViewById(R.id.password_keyboard4);
        keyboard[5] = findViewById(R.id.password_keyboard5);
        keyboard[6] = findViewById(R.id.password_keyboard6);
        keyboard[7] = findViewById(R.id.password_keyboard7);
        keyboard[8] = findViewById(R.id.password_keyboard8);
        keyboard[9] = findViewById(R.id.password_keyboard9);
        keyboardForget = findViewById(R.id.password_keyboard_forget);
        keyboardCancel = findViewById(R.id.password_keyboard_cancel);
        passwordNotify = findViewById(R.id.password_error);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.password_keyboard0:
                dealInput(0);
                break;
            case R.id.password_keyboard1:
                dealInput(1);
                break;
            case R.id.password_keyboard2:
                dealInput(2);
                break;
            case R.id.password_keyboard3:
                dealInput(3);
                break;
            case R.id.password_keyboard4:
                dealInput(4);
                break;
            case R.id.password_keyboard5:
                dealInput(5);
                break;
            case R.id.password_keyboard6:
                dealInput(6);
                break;
            case R.id.password_keyboard7:
                dealInput(7);
                break;
            case R.id.password_keyboard8:
                dealInput(8);
                break;
            case R.id.password_keyboard9:
                dealInput(9);
                break;
            case R.id.password_keyboard_cancel:
                dealCancel();
                break;

        }
    }
    private void dealCancel() {
        finish();
        /*
        if (input[0].getText() == "_") {
            finish();
        }
        for (int i = 2; i >= 0; i--) {
            if (i == 0) {
                input[i].setText("_");
                inputPassword = "";
                break;
            }
            if (input[i].getText() == "*") {
                input[i].setText("_");
                inputPassword = inputPassword.substring(0, i - 1);
                break;
            }
        }

         */
    }

    private void dealInput(int num) {
        switch (INPUT_TIME){
            case 1:
                //passwordNotify.setText("");
                if (inputPassword1.length() < 4) {
                    inputPassword1 += String.valueOf(num);
                    input[inputPassword1.length() - 1].setText("*");
                }
                if (inputPassword1.length() == 4) {
                    input[inputPassword1.length() - 1].setText("*");
                    for (int i = 0; i < 4; i++) {
                        input[i].setText("_");
                    }
                    passwordNotify.setTextColor(Color.GRAY);
                    passwordNotify.setText(R.string.KeyCreateActivity_Info1);
                    INPUT_TIME=2;
                    //passwordVerify(password, inputPassword);
                    //inputPassword = "";
                }
                break;
            case 2:
                if (inputPassword2.length()<4){
                    inputPassword2+=String.valueOf(num);
                    input[inputPassword2.length() - 1].setText("*");
                }
                if (inputPassword2.length()==4){
                    input[inputPassword2.length() - 1].setText("*");
                    createPasswordVerify();
                }
                break;
        }
    }

    private void createPasswordVerify() {
        Intent intent=new Intent();
        if (inputPassword1.equals(inputPassword2)){

            SharedPreferencesUtil.putData("numberLockKey",md5(inputPassword1));
            INPUT_STATUS=true;
            setResult(200,intent);
            SharedPreferencesUtil.putData("startKey",true);
            finish();
        }else {
            passwordNotify.setTextColor(Color.RED);
            passwordNotify.setText(R.string.KeyCreateActivity_info2);
            for (int i = 0; i < 4; i++) {
                input[i].setText("_");
            }
            INPUT_TIME=1;
            inputPassword1="";
            inputPassword2="";
        }
    }
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (INPUT_STATUS){

        }else {

        }
    }
    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.setting_security_key_change_toolbar);//自定义标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

}