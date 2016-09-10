package com.zgh.smartdemo.fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zgh.smartdemo.R;
/**
 * Created by zhuguohui on 2016/9/10 0010.
 */
public class CustomFragment extends HomeFragment implements View.OnClickListener {
    @Override
    protected int getLayoutID() {
        return R.layout.layout_fragment_custom;
    }
    @Override
    protected void onViewInit() {
        findViewById(R.id.tv_head1).setOnClickListener(this);
        findViewById(R.id.tv_head2).setOnClickListener(this);
        findViewById(R.id.tv_footer1).setOnClickListener(this);
        findViewById(R.id.tv_fixed_head).setOnClickListener(this);
        findViewById(R.id.tv_fload_view).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            Toast.makeText(getActivity(), tv.getText() + "被点击了", Toast.LENGTH_SHORT).show();
        }
    }
}
