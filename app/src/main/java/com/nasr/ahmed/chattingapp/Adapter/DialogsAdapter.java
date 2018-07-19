package com.nasr.ahmed.chattingapp.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nasr.ahmed.chattingapp.Fragment.DialogsFragment;
import com.nasr.ahmed.chattingapp.R;
import com.nasr.ahmed.chattingapp.ViewHolder.DialogHolder;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;

public class DialogsAdapter extends RecyclerView.Adapter<DialogHolder> {

    //because we are writing this class outside an activity/fragment
    //we can't reach a suitable context instance from here ,so we let it to
    //the user activity/fragment to pass it
    private Context context;
    private ArrayList<QBChatDialog> mDialogs;

    public DialogsAdapter(Context context, ArrayList<QBChatDialog> dialogs) {
        this.context = context;
        this.mDialogs = dialogs;
    }


    @NonNull
    @Override
    public DialogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_dialogs,parent,false);
        return new DialogHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DialogHolder dialogHolder, int position) {
        QBChatDialog dialogItemData = mDialogs.get(position);
        dialogHolder.bindDialogItem(dialogItemData);
    }

    @Override
    public int getItemCount() {
        return mDialogs.size();
    }
}
