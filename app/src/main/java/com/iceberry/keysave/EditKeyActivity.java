/*
package com.iceberry.keysave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.util.List;

public class EditKeyActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String KEY_SORT="item_sort";
    public static final String KEY_ACCOUNT="item_accountName";
    public static final String KEY_IMAGE="item_image";

    private Key originKey=new Key();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_key);
        setDarkStatusIcon(true);
//TODO 点击进入编辑界面
        Intent intent=getIntent();

        String keySort=intent.getStringExtra(KEY_SORT);
        String keyImage=intent.getStringExtra(KEY_IMAGE);
        String keyAccountName=intent.getStringExtra(KEY_ACCOUNT);

        ImageView keyEditImage=findViewById(R.id.editKey_icon);
        EditText keyEditSort=findViewById(R.id.editKey_sort);
        EditText keyEditNickname=findViewById(R.id.editKey_nickname);
        EditText keyEditAccountName=findViewById(R.id.editKey_accountName);
        EditText keyEditPassword=findViewById(R.id.editKey_password);
        TextView cancel=findViewById(R.id.editKey_cancel);
        TextView commit=findViewById(R.id.editKey_commit);

        Glide.with(this).load(keyImage).into(keyEditImage);
        keyEditSort.setText(keySort);
        keyEditAccountName.setText(keyAccountName);

        cancel.setOnClickListener(this);
        commit.setOnClickListener(this);

        List<Key> keys=DataSupport.select("id","nickname","sort","accountName","iconPath","password")
                .where("sort=? and accountName=?",keySort,keyAccountName)
                .limit(1)
                .find(Key.class);
        for (Key key : keys) {
            if (key!=null){
                keyEditNickname.setText(key.getNickname());
                keyEditPassword.setText(key.getPassword());
            }
            originKey=key;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editKey_cancel:
                finish();
                break;
            case R.id.editKey_commit://修改数据
                /*
                ImageView keyEditImage=findViewById(R.id.editKey_icon);
                EditText keyEditSort=findViewById(R.id.editKey_sort);
                EditText keyEditNickname=findViewById(R.id.editKey_nickname);
                EditText keyEditAccountName=findViewById(R.id.editKey_accountName);
                EditText keyEditPassword=findViewById(R.id.editKey_password);

                Key newKey=new Key();
                newKey.setSort(keyEditSort.getText().toString());
                newKey.setNickname(keyEditNickname.getText().toString());
                newKey.setAccountName(keyEditAccountName.getText().toString());
                newKey.setPassword(keyEditPassword.getText().toString());
                newKey.update(originKey.getId());
                finish();
                //Glide.with(this).load().into(keyEditImage);*/
//break;
        /*}
    }*/
    /*
    /**
     * 说明：Android 6.0+ 状态栏图标原生反色操作
     */
    /*
    protected void setDarkStatusIcon(boolean dark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView == null) return;

            int vis = decorView.getSystemUiVisibility();
            if (dark) {
                vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(vis);
        }
    }
}*/