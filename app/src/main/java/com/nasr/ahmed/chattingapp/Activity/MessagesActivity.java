package com.nasr.ahmed.chattingapp.Activity;

import android.support.v4.app.Fragment;

import com.nasr.ahmed.chattingapp.Common.Common;
import com.nasr.ahmed.chattingapp.Fragment.MessagesFragment;
import com.quickblox.chat.model.QBChatDialog;

public class MessagesActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        QBChatDialog qbChatDialog = (QBChatDialog) getIntent().getSerializableExtra(Common.EXTRA_CHOOSEN_CHAT_DIALOG);
        return MessagesFragment.newInstance(qbChatDialog);
    }
}
