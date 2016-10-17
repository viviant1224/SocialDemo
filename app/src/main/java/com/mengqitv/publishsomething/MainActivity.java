package com.mengqitv.publishsomething;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mengqitv.publishsomething.ui.activity.PersonActivity;
import com.mengqitv.publishsomething.ui.activity.PublishActivity;
import com.mengqitv.publishsomething.ui.activity.TopicActivity;
import com.mengqitv.publishsomething.utils.BitmapUtil;
import com.mengqitv.publishsomething.utils.SystemBarUtils;
import com.mengqitv.publishsomething.utils.SystemUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

public class MainActivity extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener{

    @Bind(R.id.emojicons_edit)
    EmojiconEditText editEmojicon;
    @Bind(R.id.emojicons_container)
    LinearLayout emojiconsContainer;
    @Bind(R.id.emojicons_layout)
    RelativeLayout emojiconsLayout;
    @Bind(R.id.emojicons_icon)
    LinearLayout emojiconsIcon;
    @Bind(R.id.content_lay)
    CoordinatorLayout contentLay;
    @Bind(R.id.btnMention)
    LinearLayout btnMention;
    @Bind(R.id.btnTrends)
    LinearLayout btnTrends;
    @Bind(R.id.btnCamera)
    LinearLayout btnCamera;



    @OnClick({R.id.emojicons_icon, R.id.emojicons_edit})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.emojicons_icon:
                if (emojiconsLayout.isShown()) {
                    hideEmotionView(true);
                } else {
                    showEmotionView(SystemUtils.isKeyBoardShow(this));
                }
                break;
            case R.id.emojicons_edit:
                hideEmotionView(true);
                break;
            default:
                break;
        }

    }
    private final LayoutTransition transitioner = new LayoutTransition();//键盘和表情切换
    private int emotionHeight;
    private EmojiconsFragment emojiconsFragment;


    /**
     * 返回的所有的@人或者话题,用于识别输入框中的所有要@的人和话题
     *
     * 如果用户删除过，会出现不匹配的情况，需要在for循环中做处理
     */
    private String nameStr;

    /**
     * 上一次返回的用户名和话题，用于把要@的用户名拼接到输入框中
     */
    private String lastNameStr;

    private static final int CODE_PERSON = 1;
    private static final int CODE_TOPIC = 2;
    /**
     * 存储@的cid、name对
     */
    private Map<String, String> cidNameMap = new HashMap<String, String>();

    /**
     * 选中的@的人的cid,进入@列表时，需要传递过去
     */
    private String selectedCids;

    /**
     * 存储@的cid、name对
     */
    private Map<String, String> cidTopicMap = new HashMap<String, String>();
    /**
     * 选中的#的人的话题,进入#列表时，需要传递过去
     */
    private String selectedTopicCids;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        Intent intent = new Intent(this, PublishActivity.class);
//        startActivity(intent);

        /**上下平移动画**/
        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "translationY",
                SystemUtils.getScreenHeight(this), emotionHeight).
                setDuration(transitioner.getDuration(LayoutTransition.APPEARING));
        transitioner.setAnimator(LayoutTransition.APPEARING, animIn);
        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "translationY",
                emotionHeight,
                SystemUtils.getScreenHeight(this)).
                setDuration(transitioner.getDuration(LayoutTransition.DISAPPEARING));
        transitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
        contentLay.setLayoutTransition(transitioner);
        /**安全判断 有些情况会出现异常**/
        if (savedInstanceState == null) {
            emojiconsFragment = EmojiconsFragment.newInstance(false);
            getSupportFragmentManager().beginTransaction().add(R.id.emojicons_layout, emojiconsFragment, "EmotionFragemnt").commit();
        }else {
            emojiconsFragment = (EmojiconsFragment) getSupportFragmentManager().findFragmentByTag("EmotionFragemnt");
        }
        /**先弹出软键盘**/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        SystemUtils.showKeyBoard(editEmojicon);
        editEmojicon.postDelayed(new Runnable() {

            @Override
            public void run() {
                unlockContainerHeightDelayed();
            }

        }, 200L);


        setEvent();

    }

    private void setEvent() {
        btnMention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAt();
            }
        });

        btnTrends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goThreads();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                goThreads();
//                getPicture();
            }
        });
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(editEmojicon);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(editEmojicon, emojicon);
    }

    /**
     * 隐藏emoji
     **/
    private void hideEmotionView(boolean showKeyBoard) {
        if (emojiconsLayout.isShown()) {
            if (showKeyBoard) {
                LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) emojiconsContainer.getLayoutParams();
                localLayoutParams.height = emojiconsLayout.getTop();
                localLayoutParams.weight = 0.0F;
                emojiconsLayout.setVisibility(View.GONE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                SystemUtils.showKeyBoard(editEmojicon);
                editEmojicon.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        unlockContainerHeightDelayed();
                    }

                }, 200L);
            } else {
                emojiconsLayout.setVisibility(View.GONE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                unlockContainerHeightDelayed();
            }
        }
    }

    private void showEmotionView(boolean showAnimation) {
        if (showAnimation) {
            transitioner.setDuration(200);
        } else {
            transitioner.setDuration(0);
        }

        int statusBarHeight = SystemBarUtils.getStatusBarHeight(this);
        emotionHeight = SystemUtils.getKeyboardHeight(this);

        SystemUtils.hideSoftInput(editEmojicon);
        emojiconsLayout.getLayoutParams().height = emotionHeight;
        emojiconsLayout.setVisibility(View.VISIBLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //在5.0有navigationbar的手机，高度高了一个statusBar
        int lockHeight = SystemUtils.getAppContentHeight(this);
//            lockHeight = lockHeight - statusBarHeight;
        lockContainerHeight(lockHeight);
    }

    private void lockContainerHeight(int paramInt) {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams) emojiconsContainer.getLayoutParams();
        localLayoutParams.height = paramInt;
        localLayoutParams.weight = 0.0F;
    }

    public void unlockContainerHeightDelayed() {
        ((LinearLayout.LayoutParams) emojiconsContainer.getLayoutParams()).weight = 1.0F;
    }





    private void goAt() {
        StringBuffer tmp = new StringBuffer();
        // 把选中人的id已空格分隔，拼接成字符串
        for (Map.Entry<String, String> entry : cidNameMap.entrySet()) {
            tmp.append(entry.getKey() + " ");
        }

        Intent intent = new Intent(this, PersonActivity.class);
        intent.putExtra(PersonActivity.KEY_SELECTED, tmp.toString());
        startActivityForResult(intent, CODE_PERSON);
    }

    private void goThreads() {
        StringBuffer tmp = new StringBuffer();
        for (Map.Entry<String, String> entry : cidTopicMap.entrySet()) {
            tmp.append(entry.getKey() + " ");
        }

        Intent intent = new Intent(this, TopicActivity.class);
        intent.putExtra(TopicActivity.KEY_SELECTED, tmp.toString());
        startActivityForResult(intent, CODE_TOPIC);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode != -1) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CODE_PERSON:

                String tmpCidStr = data.getStringExtra(PersonActivity.KEY_CID);
                String tmpNameStr = data.getStringExtra(PersonActivity.KEY_NAME);

                String[] tmpCids = tmpCidStr.split(" ");
                String[] tmpNames = tmpNameStr.split(" ");

                if (tmpCids != null && tmpCids.length > 0) {
                    for (int i = 0; i < tmpCids.length; i++) {
                        if (tmpNames.length > i) {
                            cidNameMap.put(tmpCids[i], tmpNames[i]);
                        }
                    }
                }

                if (selectedCids == null) {
                    selectedCids = tmpCidStr;
                } else {
                    selectedCids = selectedCids + tmpCidStr;
                }

                if (nameStr == null) {
                    nameStr = tmpNameStr;

                } else {
                    nameStr = nameStr + tmpNameStr;

                }
                lastNameStr = tmpNameStr;

                // 获取光标当前位置
                int curIndex = editEmojicon.getSelectionStart();

                // 把要@的人插入光标所在位置
                editEmojicon.getText().insert(curIndex, lastNameStr);
                // 通过输入@符号进入好友列表并返回@的人，要删除之前输入的@
                if (curIndex >= 1) {
                    editEmojicon.getText().replace(curIndex, curIndex, "");
                }
                setAtImageSpan(nameStr,"@");

                break;
            case CODE_TOPIC:

                String tmpTopicCidStr = data.getStringExtra(TopicActivity.KEY_CID);
                String tmpTopicNameStr = data.getStringExtra(TopicActivity.KEY_NAME);

                String[] tmpTopicCids = tmpTopicCidStr.split(" ");
                String[] tmpTopicNames = tmpTopicNameStr.split(" ");

                if (tmpTopicCids != null && tmpTopicCids.length > 0) {
                    for (int i = 0; i < tmpTopicCids.length; i++) {
                        if (tmpTopicNames.length > i) {
                            cidTopicMap.put(tmpTopicCids[i], tmpTopicNames[i]);
                        }
                    }
                }

                if (selectedTopicCids == null) {
                    selectedTopicCids = tmpTopicCidStr;
                } else {
                    selectedTopicCids = selectedTopicCids + tmpTopicCidStr;
                }

                if (nameStr == null) {
                    nameStr = tmpTopicNameStr;

                } else {
                    nameStr = nameStr + tmpTopicNameStr;

                }
                lastNameStr = tmpTopicNameStr;

                // 获取光标当前位置
                int curTopicIndex = editEmojicon.getSelectionStart();

                // 把要@的人插入光标所在位置
                editEmojicon.getText().insert(curTopicIndex, lastNameStr);
                // 通过输入@符号进入好友列表并返回@的人，要删除之前输入的@
                if (curTopicIndex >= 1) {
                    editEmojicon.getText().replace(curTopicIndex, curTopicIndex, "");
                }
                setAtImageSpan(nameStr,"#");


                Log.d("weiwei","qqqqqqqqqqq" + nameStr);

                break;

        }
    }




    private void setAtImageSpan(String nameStr, String c) {

        String content = String.valueOf(editEmojicon.getText());
        if (content.endsWith(c)) {
            content = content.substring(0, content.length() - 1);
        }
        String tmp = content;

        SpannableString ss = new SpannableString(tmp);

        if (nameStr != null) {
            String[] names = nameStr.split(" ");
            if (names != null && names.length > 0) {
                for (String name : names) {
                    if (name != null && name.trim().length() > 0) {
                        final Bitmap bmp = BitmapUtil.getNameBitmap(name);

                        // 这里会出现删除过的用户，需要做判断，过滤掉
                        if (tmp.indexOf(name) >= 0
                                && (tmp.indexOf(name) + name.length()) <= tmp
                                .length()) {

                            // 把取到的要@的人名和#的话题，用DynamicDrawableSpan代替
                            ss.setSpan(
                                    new DynamicDrawableSpan(
                                            DynamicDrawableSpan.ALIGN_BASELINE) {

                                        @Override
                                        public Drawable getDrawable() {
                                            // TODO Auto-generated method stub
                                            BitmapDrawable drawable = new BitmapDrawable(
                                                    getResources(), bmp);
                                            drawable.setBounds(0, 0,
                                                    bmp.getWidth(),
                                                    bmp.getHeight());
                                            return drawable;
                                        }
                                    }, tmp.indexOf(name),
                                    tmp.indexOf(name) + name.length(),
                                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
            }
        }
        editEmojicon.setTextKeepState(ss);
    }


}
