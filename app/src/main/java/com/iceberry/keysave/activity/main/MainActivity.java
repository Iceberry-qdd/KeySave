package com.iceberry.keysave.activity.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.iceberry.keysave.Encrypt;
import com.iceberry.keysave.Key;
import com.iceberry.keysave.KeyAdapter;
import com.iceberry.keysave.MyItemTouchCallback;
import com.iceberry.keysave.R;
import com.iceberry.keysave.activity.ScanResultActivity;
import com.iceberry.keysave.activity.nav.SettingsActivity;
import com.iceberry.keysave.dialog.WelcomeDialog;
import com.iceberry.keysave.service.MyService;
import com.iceberry.keysave.utils.SharedPreferencesUtil;
import com.iceberry.keysave.utils.StatusBarUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;//抽屉布局
    private SwipeRefreshLayout swipeRefresh;//刷新功能
    private KeyAdapter adapter;
    private List<Key> keyList = new ArrayList<>();
    private NavigationView navView;
    private final static int REQUEST_MAIN=1;
    private MenuItem search;
    private SearchView mSearchView;
    //private String mCurrentSearchTip="搜索类别/账号/昵称";
    //private final static int MSG_UPDATE=300;
    //private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        keyList = DataSupport.order("id desc").find(Key.class);

        Intent startIntent=new Intent(this, MyService.class);
        startService(startIntent);
        //refreshRecyclerView();
        //emptyView=findViewById(R.id.empty);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        adapter = new KeyAdapter(keyList);
        recyclerView.setAdapter(adapter);
        //recyclerView.set
        ItemTouchHelper helper = new ItemTouchHelper(new MyItemTouchCallback(adapter));
        helper.attachToRecyclerView(recyclerView);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorSwipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshKeys();
            }
        });//触发刷新
        Toolbar toolbar = findViewById(R.id.main_toolbar);//自定义标题栏
        setSupportActionBar(toolbar);

        StatusBarUtil.setStatusColor(MainActivity.this, false, true, R.color.colorPrimary);//状态栏反色
        //setDarkStatusIcon(true);//状态栏反色

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        search=findViewById(R.id.search);
        navView.setCheckedItem(R.id.nav_home);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_setting:
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);

                        break;
                    case R.id.nav_feedback:
                        //TODO 反馈界面
                        //String[] addresses={};
                        composeEmail(new String[]{getString(R.string.MainActivityMailTo)}, getString(R.string.MainActivityMailSubject));
                        break;
                    case R.id.nav_scan:
                        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                        intentIntegrator.setCaptureActivity(CustomCaptureActivity.class)
                                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                                .setTimeout(300000)
                                .initiateScan();
                        break;
                        /*
                    case R.id.nav_sort:
                        //TODO 动态增加标签
                        break;

                         */
                }
                return true;
            }

        });



        FloatingActionButton fab = findViewById(R.id.new_item);
        fab.setSize(FloatingActionButton.SIZE_NORMAL);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewKeyActivity.class);
                //startActivity(intent);
                startActivityForResult(intent,REQUEST_MAIN);
            }
        });
        if (displayWel()){
            SharedPreferencesUtil.putData("isFirstLn",false);
            SharedPreferencesUtil.putData("startKey",false);
        }
        setNumberKeySurface();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent=new Intent(this,MyService.class);
        stopService(intent);
    }

    //    private ScheduledExecutorService mScheduledExecutor = Executors.newScheduledThreadPool(10);
//    public ScheduledFuture<?> schedule(Runnable command, long delayTimeMills) {
//        return mScheduledExecutor.schedule(command, delayTimeMills, TimeUnit.MILLISECONDS);
//    }
    private void setNumberKeySurface() {
        boolean startKey = (boolean) SharedPreferencesUtil.getData("startKey", false);
        if (startKey){
            Intent intent=new Intent(MainActivity.this,NumberLockActivity.class);
            startActivity(intent);
        }
    }

    private boolean displayWel() {
        boolean flag=false;
        SharedPreferencesUtil.getInstance(this,"first_pref");
        Boolean isFirstLn = (Boolean) SharedPreferencesUtil.getData("isFirstLn", true);
        if (isFirstLn){
            showWelDialog();

            flag=true;
        }
        return flag;
    }

    private void showWelDialog() {
        final WelcomeDialog dialog=new WelcomeDialog(MainActivity.this);
        dialog.setImageResId(R.drawable.ic_launcher_round);
        dialog.setOnClickBottomListener(new WelcomeDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 调用系统邮件应用发送电子邮件
     *
     * @param addresses 收件人地址
     * @param subject   邮件主题
     */
    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //navView.setCheckedItem(R.id.nav_sort_default);
        navView.setCheckedItem(R.id.nav_home);
        //SharedPreferencesUtil.getInstance(this,"settings_darkMode");
        boolean isDarkMode=(boolean) SharedPreferencesUtil.getData("darkMode",false);
        if (isDarkMode){
            StatusBarUtil.setStatusColor(MainActivity.this, false, false, R.color.colorPrimary);//状态栏反色
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            StatusBarUtil.setStatusColor(MainActivity.this, false, true, R.color.colorPrimary);//状态栏反色
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
/*
        boolean enSearch = (boolean) SharedPreferencesUtil.getData("enableSearch", false);
        if (enSearch){
            search.setVisible(false);
        }else {
            search.setVisible(true);
        }

 */


    }

    /**
     * 刷新RecyclerView
     */
    public void refreshRecyclerView() {
        //keyList.clear();

        //keyList = DataSupport.order("id desc").find(Key.class);

        //adapter = new KeyAdapter(keyList);

        //adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_MAIN:
                if (resultCode == RESULT_OK) {
                    refreshKeys();
                }
                break;
            default:
        }
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            try {
                dealScanResult(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理扫描结果
     *
     * @param result 结果
     */
    private void dealScanResult(IntentResult result) throws Exception {
        String resultStr = result.getContents();
        Encrypt encrypt = new Encrypt();
        Pattern httpPattern;
        httpPattern = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        if (resultStr == null) {
            Toast.makeText(this, R.string.MainActivityScanbar_cancelScan, Toast.LENGTH_LONG).show();
        } else {

            if (resultStr.startsWith("key://")) {
                String scanResult = encrypt.decrypt(resultStr.substring(6));
                Intent intent = new Intent(MainActivity.this, NewKeyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("scanResult", scanResult);
                intent.putExtras(bundle);
                startActivity(intent);
            } /*else {
                Toast.makeText(this, "扫描内容:" + result.getContents(), Toast.LENGTH_LONG).show();
            }
            */
            if (httpPattern.matcher(resultStr).matches()) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(resultStr));
                    //intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!resultStr.startsWith("key://") && !httpPattern.matcher(resultStr).matches()) {
                Intent intent = new Intent(MainActivity.this, ScanResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("scanResult", resultStr);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }
    }

    /**
     * 手动刷新列表
     */
    private void refreshKeys() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        keyList.clear();
                        keyList.addAll(DataSupport.order("id desc").find(Key.class));
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    /**
     * 设置Toolbar新建菜单
     *
     * @param menu 要使用的菜单布局
     * @return true代表启用
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menus, menu);
        MenuItem menuItem=menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        //initSearchView();
        return true;
    }
/*
    private void initSearchView() {
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return true;
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    return false;
                }
                showSearchTip(newText);
                return true;
            }
        });
    }

 */
    /*
    public void showSearchTip(String newText){
        schedule(new SearchTipThread(newText), 100);
    }

     */
    /*
    class SearchTipThread implements Runnable{
        String newText;
        public SearchTipThread(String newText){
            this.newText=newText;
        }
        @Override
        public void run() {
            if (newText.equals(mCurrentSearchTip)) {
                return;
            }
            mCurrentSearchTip = newText;
            List<Key> mData=DataSupport.select().where("sort=?",newText,"nickname=?",newText,"account=?",newText).find(Key.class);
            keyList.addAll(mData);
            //mSchedualDatas = xxxDBHelper.getxxxByWord(newText, xxx);
            mUpdateHandler.sendMessage(mUpdateHandler.obtainMessage(MSG_UPDATE));
        }
    }

     */

    /**
     * 重写菜单选择逻辑
     *
     * @param item 要使用的菜单布局
     * @return true代表启用
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:

                //Intent intent = new Intent(MainActivity.this, NewKeyActivity.class);
                //startActivityForResult(intent, 1);
                break;
            default:
        }
        return true;
    }

}
