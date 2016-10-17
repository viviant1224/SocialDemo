package com.mengqitv.publishsomething.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mengqitv.publishsomething.model.Person;
import com.mengqitv.publishsomething.model.Topic;

import java.util.List;

/**
 * Created by weiwei.huang on 2016/10/10.
 */

public class TopicAdapter extends BaseAdapter {

    private List<Topic> mList;
    private Context mContext;
    private LayoutParams lp;

    public TopicAdapter(List<Topic> list, Context ctx) {
        mList = list;
        mContext = ctx;
        lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new TextView(mContext);
        }

        TextView tv = (TextView) convertView;
        tv.setTextSize(25);
        tv.setTextColor(Color.BLACK);
        tv.setLayoutParams(lp);
        Topic p = (Topic) getItem(position);
        tv.setText(p.getName());
        return tv;
    }
}
