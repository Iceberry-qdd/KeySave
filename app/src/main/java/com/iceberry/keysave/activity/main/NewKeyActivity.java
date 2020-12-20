package com.iceberry.keysave.activity.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iceberry.keysave.Encrypt;
import com.iceberry.keysave.Key;
import com.iceberry.keysave.dialog.CanNotAddDialog;
import com.iceberry.keysave.R;
import com.iceberry.keysave.utils.SharedPreferencesUtil;
import com.iceberry.keysave.utils.StatusBarUtil;

import org.litepal.tablemanager.Connector;

import java.io.File;

import static android.os.Environment.DIRECTORY_DOCUMENTS;
import static android.os.Environment.DIRECTORY_PICTURES;

public class NewKeyActivity extends BaseActivity implements View.OnClickListener {

    public static final int CHOOSE_PHOTO = 2;
    private ImageView picture;
    private String ImagePath;

    private EditText sort;
    private EditText nickname;
    private EditText account;
    private EditText password;
    //private File cacheFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_key);
        Connector.getDatabase();
        //StatusBarUtil.setStatusColor(NewKeyActivity.this, false, true, R.color.colorPrimary);//状态栏反色
        Toolbar toolbar = findViewById(R.id.main_toolbar);//自定义标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            //actionBar.setDisplayShowTitleEnabled(false);
        }
        picture = findViewById(R.id.newKey_icon);
        TextView commit = findViewById(R.id.newKey_commit);
        TextView cancel = findViewById(R.id.newKey_cancel);

        sort = findViewById(R.id.newKey_sort);
        nickname = findViewById(R.id.newKey_nickname);
        account = findViewById(R.id.newKey_account);
        password = findViewById(R.id.newKey_password);

        picture.setOnClickListener(this);
        commit.setOnClickListener(this);
        cancel.setOnClickListener(this);

        scanToCommit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean isDarkMode=(boolean) SharedPreferencesUtil.getData("darkMode",false);
        if (isDarkMode){
            StatusBarUtil.setStatusColor(NewKeyActivity.this, false, false, R.color.colorPrimary);//状态栏反色
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            StatusBarUtil.setStatusColor(NewKeyActivity.this, false, true, R.color.colorPrimary);//状态栏反色
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.newKey_icon://从相册选择图片
                if (ContextCompat.checkSelfPermission(NewKeyActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewKeyActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
                break;
            case R.id.newKey_commit://添加数据
                if (sort.getText().toString().isEmpty() ||
                        nickname.getText().toString().isEmpty() ||
                        account.getText().toString().isEmpty() ||
                        password.getText().toString().isEmpty()) {
                    showDialog();
                    //Snackbar.make(v, "有字段为空！", Snackbar.LENGTH_LONG).show();
                } else {
                    commit(sort, nickname, account, password, intent);
                }
                break;
            case R.id.newKey_cancel://取消新建
                finish();
                break;
            default:
                break;
        }
    }

    private void scanToCommit() {
        Bundle bundle = this.getIntent().getExtras();
        String scanResultText = null;
        if (bundle != null) {
            scanResultText = bundle.getString("scanResult");
            String sort = scanResultText.substring(2, scanResultText.indexOf("n-"));
            String nickname = scanResultText.substring(scanResultText.indexOf("n-") + 2, scanResultText.indexOf("a-"));
            String account = scanResultText.substring(scanResultText.indexOf("a-") + 2, scanResultText.indexOf("k-"));
            this.sort.setText(sort);
            this.nickname.setText(nickname);
            this.account.setText(account);

            try {
                Encrypt encrypt = new Encrypt();
                String password = encrypt.decrypt(scanResultText.substring(scanResultText.indexOf("k-") + 2));
                this.password.setText(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 显示对话框
     */
    private void showDialog() {
        final CanNotAddDialog myDialog = new CanNotAddDialog(NewKeyActivity.this);
        myDialog.setOnCancelListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
            }
        });
        myDialog.show();
    }

    /**
     * 提交数据
     *
     * @param sort     类型
     * @param nickname 昵称
     * @param account  账户
     * @param password 密码
     * @param intent
     */
    private void commit(EditText sort, EditText nickname, EditText account, EditText password, Intent intent) {
        try {
            Key key = new Key();
            key.setIconPath(ImagePath);
            key.setSort(sort.getText().toString());
            key.setNickname(nickname.getText().toString());
            key.setAccount(account.getText().toString());

            String beforeDESKey = password.getText().toString();
            Encrypt des = new Encrypt();
            String afterDESKey = des.encrypt(beforeDESKey);
            key.setPassword(afterDESKey);

            key.save();
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAlbum() {
        //Context context=NewKeyActivity.this;
        //cacheFile = File.separator + "cache_" + System.currentTimeMillis() + ".jpg";
        //cacheFile = new File(context.getExternalFilesDir(DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "cache_" + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        //intent.putExtra("crop","circle");
        //intent.putExtra("return-data",true);
        //intent.putExtra("aspectX", 1);
        //intent.putExtra("aspectY", 1);

        //intent.putExtra("output", Uri.fromFile(new File(String.valueOf(cacheFile))));
       // intent.putExtra("outputFormat", "JPEG");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, R.string.NewActivitySnakbar_info, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        ImagePath = imagePath;
        displayImage(ImagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            /*
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inSampleSize = 4;
            picture.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(cacheFile), opt));

             */

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


}
