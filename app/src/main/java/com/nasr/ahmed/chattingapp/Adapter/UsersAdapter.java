package com.nasr.ahmed.chattingapp.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nasr.ahmed.chattingapp.R;
import com.nasr.ahmed.chattingapp.ViewHolder.DialogHolder;
import com.nasr.ahmed.chattingapp.ViewHolder.UserHolder;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UserHolder> {

    //because we are writing this class outside an activity/fragment
    //we can't reach a suitable context instance from here ,so we let it to
    //the user activity/fragment to pass it
    private Context context;
    private ArrayList<QBUser> mqbUsersList  ;

    public UsersAdapter(Context context,ArrayList<QBUser> usersList) {
        this.context = context;
        mqbUsersList = usersList;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_users,parent,false);
        return new UserHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder userHolder, int position) {
        QBUser qbUser = mqbUsersList.get(position);
        userHolder.bindDialogItem(qbUser);
    }

    @Override
    public int getItemCount() {
        return mqbUsersList.size();
    }
}
