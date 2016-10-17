package com.mengqitv.publishsomething.ui.fragment.publish;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mengqitv.publishsomething.R;
import com.mengqitv.publishsomething.ui.fragment.publish.base.PublishDynamicFragmentBase;

/**
 * Created by weiwei.huang on 2016/10/12.
 */

public class PublishDynamicFragment extends PublishDynamicFragmentBase{

    public static PublishDynamicFragment newInstance() {
        return new PublishDynamicFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
//        initGridImage();
    }
}
