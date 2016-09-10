package com.zgh.smartdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zgh.smartdemo.fragment.CustomFragment;
import com.zgh.smartdemo.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    View tv_home, tv_custom;
    HomeFragment homeFragment;
    CustomFragment customFragment;
    Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_home = findViewById(R.id.tv_home);
        tv_custom = findViewById(R.id.tv_custom);
        tv_home.setSelected(true);
        tv_home.setOnClickListener(this);
        tv_custom.setOnClickListener(this);
        homeFragment = new HomeFragment();
        customFragment = new CustomFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.content,customFragment).add(R.id.content, homeFragment).hide(customFragment).commit();
        mFragment = homeFragment;
    }

    @Override
    public void onClick(View v) {
        Fragment newFragment = null;
        switch (v.getId()) {
            case R.id.tv_custom:
                if (mFragment != customFragment) {
                    newFragment = customFragment;
                    tv_home.setSelected(false);
                    tv_custom.setSelected(true);
                }
                break;
            case R.id.tv_home:
                if (mFragment != homeFragment) {
                    newFragment = homeFragment;
                    tv_home.setSelected(true);
                    tv_custom.setSelected(false);
                }
                break;
        }
        if (newFragment != null) {

            getSupportFragmentManager().beginTransaction().hide(mFragment).show(newFragment).commit();
            mFragment = newFragment;
        }
    }

}
