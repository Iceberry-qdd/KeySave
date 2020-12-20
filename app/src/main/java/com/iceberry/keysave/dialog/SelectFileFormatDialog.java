package com.iceberry.keysave.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.iceberry.keysave.R;
import com.iceberry.keysave.utils.SharedPreferencesUtil;

/*
public class SelectFileFormatDialog extends Dialog {
    private Button button_OK;//按钮
    private Button button_cancel;
    private TextView textView;//标题文字
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private View view;
    /**
     * 构造方法
     *
     * @param context
     */
/*
    public SelectFileFormatDialog(@NonNull Context context) {
        super(context, R.style.SelectFileFormatDialog);

        view = LayoutInflater.from(getContext()).inflate(R.layout.select_file_format_dialog, null);
        radioGroup=view.findViewById(R.id.select_file_format_radio_group);
        button_OK = view.findViewById(R.id.select_file_format_OK_button);
        button_cancel=view.findViewById(R.id.select_file_format_cancel_button);
         radioButton1=view.findViewById(R.id.select_file_format_button_ks);
         radioButton2=view.findViewById(R.id.select_file_format_button_ksp);
         radioButton3=view.findViewById(R.id.select_file_format_button_xslx);
        setContentView(view);
    }


    public void setTitle(String content) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==radioButton1.getId()){
                    //Toast.makeText(this,"b1选中", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /*
    public void setOnCheckedChangedListener(){

    }
    public void setSingleChoiceItems(){

    }

     */


/**
 * 取消监听
 *
 * @param
 */
/*
    public void setOnCancelListener(View.OnClickListener listener) {
        button_OK.setOnClickListener(listener);
        button_cancel.setOnClickListener(listener);
    }

}
*/

/**
 * 自定义单选dialog
 * @author Murg
 * @date 2019/3/19
 * @Description: https://blog.csdn.net/weixin_41647530/article/details/88656022
 */


public class SelectFileFormatDialog implements View.OnClickListener {
    Dialog mRadioDialog;
    private TextView mBtnOk;
    private TextView mBtnCancel;
    private View.OnClickListener okOnClickListener;
    private View.OnClickListener cancelOnClickListener;
    private RadioButton format1;
    private RadioButton format2;
    private RadioButton format3;

    String item;//选择的选项值

    public void setOkOnClickListener(View.OnClickListener okOnClickListener) {
        this.okOnClickListener = okOnClickListener;
    }

    public void setCancelOnClickListener(View.OnClickListener cancelOnClickListener) {
        this.cancelOnClickListener = cancelOnClickListener;
    }

    public String getItem() {
        return item;
    }

    public SelectFileFormatDialog(Activity context, String prompt,
                                  String okName, String caName) {
        // 首先得到整个View
        final View view = LayoutInflater.from(context).inflate(
                R.layout.select_file_format_dialog, null);
        // 页面中显示文本
        TextView mPrompt = view.findViewById(R.id.select_file_format_dialog_title);
        //单选组
        RadioGroup radioGroup = view.findViewById(R.id.select_file_format_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //获取选择的选项，取值
                RadioButton rb = view.findViewById(radioGroup.getCheckedRadioButtonId());
                item = rb.getText().toString();
            }
        });

        format1=view.findViewById(R.id.select_file_format_button_ks);
        format2=view.findViewById(R.id.select_file_format_button_ksp);
        format3=view.findViewById(R.id.select_file_format_button_xslx);

        mBtnOk = view.findViewById(R.id.select_file_format_OK_button);
        mBtnCancel = view.findViewById(R.id.select_file_format_cancel_button);
        // 显示文本
        mPrompt.setText(prompt);
        mBtnOk.setText(okName);
        mBtnCancel.setText(caName);
        mBtnOk.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        // 创建自定义样式的Dialog
        mRadioDialog = new Dialog(context, R.style.SelectFileFormatDialog);
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        //view.setMinimumWidth(w * 4 / 5);//设置dialog的宽度
        view.setMinimumWidth(280);//设置dialog的宽度
        // 设置返回键无效
        mRadioDialog.setCancelable(true);
        mRadioDialog.setContentView(view, new ActionBar.LayoutParams(//设置dialog
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void show() {
        if (!mRadioDialog.isShowing()) {
//默认选中
            String fileFormat = (String) SharedPreferencesUtil.getData("fileFormat", ".ksp");
            if (fileFormat.equals(format1.getText()) ){
                format1.setChecked(true);
            }
            if (fileFormat.equals(format2.getText())){
                format2.setChecked(true);
            }
            if (fileFormat.equals(format3.getText())){
                format3.setChecked(true);
            }
            mRadioDialog.show();
        }
    }

    public void close() {
        if (mRadioDialog != null) {
            mRadioDialog.dismiss();
            mRadioDialog = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_file_format_cancel_button:
                if (cancelOnClickListener != null) {
                    cancelOnClickListener.onClick(view);
                }
                break;
            default:
                if (okOnClickListener != null) {
                    okOnClickListener.onClick(view);
                }
                break;
        }
    }
}