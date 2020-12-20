package com.iceberry.keysave.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.iceberry.keysave.R;

public class WelcomeDialog extends Dialog {
    private ImageView welImg;
    private Button welBtn;
    private String position;
    private int imageResId=-1;

    public WelcomeDialog(@NonNull Context context) {
        super(context, R.style.WelcomeDialog);
    }

    public interface OnClickBottomListener{

        //点击按钮的回调
        public void onPositiveClick();
    }
    private OnClickBottomListener onClickBottomListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_dialog);
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        //初始化界面数据
        refreshView();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        welBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener!=null){
                    //点击按钮后，向外界提供监听
                    onClickBottomListener.onPositiveClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {
//        if (!TextUtils.isEmpty(position)){
//            welBtn.setText(position);
//        }else {
//            welBtn.setText("开始使用");
//        }
        if (imageResId!=-1){
            welImg.setImageResource(R.drawable.ic_launcher_round);
            welImg.setVisibility(View.VISIBLE);
        }else {
            welImg.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        welImg=(ImageView)findViewById(R.id.welcome_image);
        welBtn=(Button)findViewById(R.id.welcome_button);
    }

    /**
     * 设置确定取消按钮的回调
     * @param onClickBottomListener
     * @return
     */
    public WelcomeDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
