package com.iceberry.keysave.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iceberry.keysave.R;

public class CanNotAddDialog extends Dialog {
    private Button button_OK;//按钮
    private TextView textView;//标题文字

    /**
     * 构造方法
     *
     * @param context
     */
    public CanNotAddDialog(@NonNull Context context) {
        super(context, R.style.CannotAddDialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cannot_add_dialog, null);
        textView = view.findViewById(R.id.cannot_add_title);
        button_OK = view.findViewById(R.id.cannot_add_OK_button);
        setContentView(view);
    }

    public void setTitle(String content) {
        textView.setText(content);
    }

    /**
     * 取消监听
     *
     * @param listener
     */
    public void setOnCancelListener(View.OnClickListener listener) {
        button_OK.setOnClickListener(listener);
    }

}
