package com.mengqitv.publishsomething.ui.fragment.publish.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.mengqitv.publishsomething.R;
import com.mengqitv.publishsomething.ui.fragment.base.BaseFragment;

import butterknife.Bind;
import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

/**
 * Created by weiwei.huang on 2016/10/12.
 */

public class PublishDynamicFragmentBase extends BaseFragment implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener{

    @Bind(R.id.editContent)
    EmojiconEditText editContent;

    @Override
    protected int getLayoutId() {
        return R.layout.ui_publish_status;
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(editContent);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(editContent, emojicon);
    }


}
