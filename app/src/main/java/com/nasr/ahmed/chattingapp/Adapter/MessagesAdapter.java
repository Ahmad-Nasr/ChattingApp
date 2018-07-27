package com.nasr.ahmed.chattingapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nasr.ahmed.chattingapp.R;
import com.nasr.ahmed.chattingapp.ViewHolder.ReceivedMessageHolder;
import com.nasr.ahmed.chattingapp.ViewHolder.SentMessageHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int SENT_TYPE = 0;
    private static final int RECEIVED_TYPE = 1;


    //because we are writing this class outside an activity/fragment
    //we can't reach a suitable context instance from here ,so we let it to
    //the user activity/fragment to pass it
    private Context context;
    private ArrayList<QBChatMessage> mQBChatMessages;


    public MessagesAdapter(Context context, ArrayList<QBChatMessage> messages) {
        this.context = context;
        this.mQBChatMessages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view;

        switch (viewType) {
            case SENT_TYPE:
                view = layoutInflater.inflate(R.layout.item_msg_sent, parent, false);
                return new SentMessageHolder(view);
            //no need for break because return exists
            case RECEIVED_TYPE:
                view = layoutInflater.inflate(R.layout.item_msg_received, parent, false);
                return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case SENT_TYPE:
                ((SentMessageHolder) holder).bindSentMessageItem(mQBChatMessages.get(position));
            break;
            case RECEIVED_TYPE:
                ((ReceivedMessageHolder) holder).bindReceivedMessageItem(mQBChatMessages.get(position));
            break;
        }
    }

    @Override
    public int getItemCount() {
        return mQBChatMessages.size();
    }


    //A recyclerView method
    //know item type from the data item in the data list
    //this type will be used later to determine which item layout to inflate
    @Override
    public int getItemViewType(int position) {
        int senderId = mQBChatMessages.get(position).getSenderId();
        int userId = QBChatService.getInstance().getUser().getId();

        if (senderId == userId) {
            return SENT_TYPE;
        }
        return RECEIVED_TYPE;
    }
}
