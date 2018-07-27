package com.nasr.ahmed.chattingapp.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.nasr.ahmed.chattingapp.R;
import com.quickblox.chat.model.QBChatMessage;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SentMessageHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.txt_sent_msg)
    TextView mTextViewSentMsg;

    //passing the View to hold
    public SentMessageHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    //binding data to the held view
    public void bindSentMessageItem(QBChatMessage qbChatMessage) {
        mTextViewSentMsg.setText(qbChatMessage.getBody());
    }
}
