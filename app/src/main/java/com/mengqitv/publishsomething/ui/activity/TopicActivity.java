package com.mengqitv.publishsomething.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mengqitv.publishsomething.R;
import com.mengqitv.publishsomething.adapter.PersonAdapter;
import com.mengqitv.publishsomething.adapter.TopicAdapter;
import com.mengqitv.publishsomething.model.Person;
import com.mengqitv.publishsomething.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiwei.huang on 2016/10/10.
 */

public class TopicActivity extends Activity {

    /**
     * 保存选中的人对应的id的字符串 id以空格分隔
     */
    private String mIniSelectedCids;

    public static final String KEY_CID = "cid";
    public static final String KEY_NAME = "name";
    public static final String KEY_SELECTED = "selected";
    private ListView mListView;
    private TopicAdapter mAdapter;
    private List<Topic> mTopics = new ArrayList<Topic>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at);
        Intent intent = getIntent();
        mIniSelectedCids = intent.getStringExtra(KEY_SELECTED);
        findViewById();
        mAdapter = new TopicAdapter(mTopics, this);
        mListView.setAdapter(mAdapter);
        inflateListView();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Topic topic = (Topic) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra(KEY_CID, topic.getId()+" ");
                intent.putExtra(KEY_NAME, "#"+topic.getName()+" ");

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void findViewById() {
        mListView = (ListView) findViewById(R.id.lv);
    }

    private void inflateListView() {
        mTopics.clear();
        for (int i = 0; i < 30; i++) {
            Topic topic = new Topic();
            topic.setId(String.valueOf(i));
            topic.setName("话题"+i);
            mTopics.add(topic);
        }
        removeSelectedCids();
        mAdapter.notifyDataSetChanged();
    }

    private void removeSelectedCids() {
        Topic entity = null;
        List<Topic> tmp = new ArrayList<Topic>();
        for (int i = 0; i < mTopics.size(); i++) {
            entity = mTopics.get(i);
            if (mIniSelectedCids != null) {
                if (mIniSelectedCids.contains(entity.getId())) {
                    tmp.add(entity);
                }
            }
        }

        mTopics.removeAll(tmp);
    }

}
