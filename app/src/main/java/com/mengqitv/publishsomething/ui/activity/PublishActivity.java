package com.mengqitv.publishsomething.ui.activity;

import com.mengqitv.publishsomething.ui.activity.base.BaseActivity;
import com.mengqitv.publishsomething.ui.fragment.publish.PublishDynamicFragment;
import com.mengqitv.publishsomething.ui.fragment.base.BaseFragment;

/**
 * Created by weiwei.huang on 2016/10/12.
 */

public class PublishActivity extends BaseActivity {

    @Override
    protected BaseFragment getFirstFragment() {
        return PublishDynamicFragment.newInstance();
    }
}
