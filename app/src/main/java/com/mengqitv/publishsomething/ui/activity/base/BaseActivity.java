package com.mengqitv.publishsomething.ui.activity.base;

import com.mengqitv.publishsomething.R;

/**
 * Created by weiwei.huang on 2016/10/12.
 */

public abstract class BaseActivity extends AppActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.activity_base;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }
}
