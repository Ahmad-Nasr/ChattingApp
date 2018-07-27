package com.nasr.ahmed.chattingapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nasr.ahmed.chattingapp.R;
import com.quickblox.chat.model.QBChatMessage;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReceivedMessageHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txt_received_msg)
    TextView mTextViewReceivedMsg;


    public ReceivedMessageHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void bindReceivedMessageItem(QBChatMessage qbChatMessage) {
        mTextViewReceivedMsg.setText(qbChatMessage.getBody());
    }
}
