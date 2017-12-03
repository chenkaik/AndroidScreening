package com.example.study.android.androidscreening.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.study.android.androidscreening.R;
import com.example.study.android.androidscreening.main.MainActivity;


public class ScreeningActivity extends AppCompatActivity implements View.OnClickListener{
//http://www.jianshu.com/p/e4c972ca6c94#
    private DrawerLayout drawer;
    private LinearLayout navigationView;
    private RightSideslipLay menuHeaderView;
    private TextView mFrameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screening);
        findViewById(R.id.main_ch).setOnClickListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (LinearLayout) findViewById(R.id.nav_view);
        mFrameTv = (TextView) findViewById(R.id.screenTv);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

        menuHeaderView = new RightSideslipLay(ScreeningActivity.this);
        navigationView.addView(menuHeaderView);
        mFrameTv.setOnClickListener(new OnClickListenerWrapper() {
            @Override
            protected void onSingleClick(View v) {
                openMenu();
            }
        });
        menuHeaderView.setCloseMenuCallBack(new RightSideslipLay.CloseMenuCallBack() {
            @Override
            public void setupCloseMean() {
                closeMenu();
            }
        });

    }

    public void closeMenu() {
        drawer.closeDrawer(GravityCompat.END);
    }

    public void openMenu() {
        drawer.openDrawer(GravityCompat.END);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_ch:
                startActivity(new Intent(this, MainActivity.class));
                break;
            default:
                break;
        }
    }
}
