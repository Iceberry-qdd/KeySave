package com.iceberry.keysave.activity.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.iceberry.keysave.ActivityCollector;
import com.iceberry.keysave.R;
import com.iceberry.keysave.utils.SharedPreferencesUtil;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class NumberLockActivity extends BaseActivity implements View.OnClickListener {
    private TextView[] input = new TextView[4];
    private TextView passwordError;

    private Button[] keyboard = new Button[10];
    private Button keyboardForget;
    private Button keyboardCancel;

    private String inputPassword = "";
    private String password;

    private int INPUT_TYPE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_FullScreenTheme);
        setContentView(R.layout.activity_number_lock);
        findView();
        setOnClickListener();
        String numberLockKey = (String) SharedPreferencesUtil.getData("numberLockKey", "");
        password=numberLockKey;
    }

    private void setOnClickListener() {
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
        passwordError = findViewById(R.id.password_error);
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
            case R.id.password_keyboard_forget:
                //TODO
                break;
            case R.id.password_keyboard_cancel:
                dealCancel();
                break;

        }
    }

    private void dealCancel() {
        if (input[0].getText() == "_") {
            ActivityCollector.finishAll();
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
    }

    private void dealInput(int num) {
        if (INPUT_TYPE != 0) {
            passwordError.setText("");
        }
        if (inputPassword.length() < 4) {
            inputPassword += String.valueOf(num);
            input[inputPassword.length() - 1].setText("*");
        }
        if (inputPassword.length() == 4) {
            input[inputPassword.length() - 1].setText("*");
            passwordVerify(password, inputPassword);
            inputPassword = "";
        }
    }

    private void passwordVerify(@NotNull final String password, final String inputPassword) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (password.equals(md5(inputPassword))) {
                    finish();
                } else {
                    passwordError.setText(R.string.NumberLockActivity_passwordError);
                    INPUT_TYPE = 1;
                    for (int i = 0; i < 4; i++) {
                        input[i].setText("_");
                    }
                }
            }
        }).start();

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
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode()==KeyEvent.KEYCODE_BACK){
            ActivityCollector.finishAll();
            return true;
        }else {
            return super.dispatchKeyEvent(event);
        }

    }
}