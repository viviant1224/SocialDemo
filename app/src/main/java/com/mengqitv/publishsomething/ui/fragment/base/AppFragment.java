package com.mengqitv.publishsomething.ui.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mengqitv.publishsomething.ui.activity.base.BaseActivity;

/**
 * Created by weiwei.huang on 2016/10/12.
 */

public abstract class AppFragment extends Fragment {
    protected abstract int getLayoutId();

    protected abstract void initView(View view, Bundle savedInstanceState);
    protected void releaseView(){

    };
    protected abstract void initActionBar(ActionBar actionBar);

    protected BaseActivity getHoldingActivity(){
        if (getActivity() instanceof BaseActivity){
            return (BaseActivity)getActivity();
        }else {
            throw new ClassCastException("activity must extends BaseActivity");
        }
    }

    protected void pushFragment(BaseFragment fragment){
        getHoldingActivity().pushFragment(fragment);
    }

    protected void popFragment(BaseFragment fragment){
        getHoldingActivity().popFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ActionBar actionBar = getHoldingActivity().getSupportActionBar();
        if (actionBar != null){
            initActionBar(actionBar);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releaseView();
    }
}
