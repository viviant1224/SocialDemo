package com.mengqitv.publishsomething.ui.fragment.base;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by weiwei.huang on 2016/10/12.
 */

public abstract class BaseFragment extends AppFragment{

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void initActionBar(ActionBar actionBar) {
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }


}
