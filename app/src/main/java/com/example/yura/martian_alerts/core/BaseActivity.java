package com.example.yura.martian_alerts.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.yura.martian_alerts.R;
import com.example.yura.martian_alerts.libs.ExceptionHandler;


public class BaseActivity extends AppCompatActivity {

    public FragmentManager mFragmentManager;
    protected Context mContext;
    public String LOG_TAG ;
    protected final int SDK_VERSION = Build.VERSION.SDK_INT;
    private BaseFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //防當機處理
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        mContext = this.getApplicationContext();
        mFragmentManager = getSupportFragmentManager();
        LOG_TAG = this.getClass().getSimpleName();
    }


    @Override
    public void onBackPressed() {
        if(currentFragment == null || currentFragment.onBackPress())
            back();
    }

    public void back(){
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            finish();

        } else {
            mFragmentManager.popBackStack();
        }
    }

    public void setCurrentFragment(BaseFragment f){
        currentFragment = f ;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setStatusBarStyle(){
        if(SDK_VERSION >=21 ) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void changeStatusBarStyle(int res_id){
        if(SDK_VERSION >=21 ) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, res_id));
        }
    }


    public void showActionBar(){
        //findViewById(R.id.actionbar).setVisibility(View.VISIBLE);
        //findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
    }

    public void hideActionBar(){
        //findViewById(R.id.actionbar).setVisibility(View.GONE);
        //findViewById(R.id.toolbar).setVisibility(View.GONE);
    }

    protected void showLoadingView(){
        /*
        View view = findViewById(R.id.layout_loading_view);
        if(view != null){
            view.setVisibility(View.VISIBLE);
            ImageView img_loading = (ImageView) findViewById(R.id.img_loading);

            img_loading.setBackgroundResource(R.drawable.animation_loading);
            AnimationDrawable anim = ((AnimationDrawable) img_loading.getBackground());
            anim.start();
        }
        */
    }

    protected void hideLoadingView(){
        /*
        View view = findViewById(R.id.layout_loading_view);
        if(view != null){
            view.setVisibility(View.GONE);
            ImageView img_loading = (ImageView) findViewById(R.id.img_loading);
            ((AnimationDrawable) img_loading.getBackground()).stop();
        }
        */
    }

    protected void showToast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }


    // Switch Fragment
    public void changeFragment(Fragment f) {
        changeFragment(f, false, null);
    }

    // Switch Fragment
    public void changeFragment(Fragment f, Bundle b) {
        changeFragment(f, false, b);
    }

    // Init Fragment(calling i nFragmentActivity)
    public void initFragment(Fragment f) {
        changeFragment(f, true, null);
    }


    // Init Fragment(call in FragmentActivity)
    public void initFragment(Fragment f, Bundle b) {
        changeFragment(f, true, b);
    }

    protected void changeFragment(Fragment f, boolean init, Bundle b) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
//		ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        if (b != null)
            f.setArguments(b);

        if (init) {
            clearAllFragment();
            ft.replace(R.id.container, f, LOG_TAG);
        } else {
            ft.replace(R.id.container, f);
            ft.addToBackStack(null);
        }

        ft.commitAllowingStateLoss();
    }

    public void clearAllFragment() {
        for(int i = 0; i < mFragmentManager.getBackStackEntryCount(); ++i) {
            mFragmentManager.popBackStack();
        }
    }
}
