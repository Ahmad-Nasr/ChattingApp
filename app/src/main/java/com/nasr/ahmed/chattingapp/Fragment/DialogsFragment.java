package com.nasr.ahmed.chattingapp.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nasr.ahmed.chattingapp.Activity.UsersActivity;
import com.nasr.ahmed.chattingapp.Adapter.DialogsAdapter;
import com.nasr.ahmed.chattingapp.Common.Common;
import com.nasr.ahmed.chattingapp.R;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;


public class DialogsFragment extends Fragment {


    public static final String TAG = DialogsFragment.class.getSimpleName();

    private String username ;
    private String password ;


    @BindView(R.id.recycler_dialogs_list)
    RecyclerView mRecyclerViewDialogsList;
    @BindView(R.id.fab_add_dialog)
    FloatingActionButton mFabAddDialog;
    @BindView(R.id.txt_no_dialogs)
    TextView mTextViewNoDialogs;

    private FragmentActivity hostingActivity;
    private DialogsAdapter mDialogsAdapter;


    //hosting activity uses this method to instantiate and pass arguments to the Fragment
    public static DialogsFragment newInstance(String username, String password) {
        Bundle args = new Bundle();
        args.putString(Common.ARG_CURRENT_USER_NAME, username);
        args.putString(Common.ARG_CURRENT_USER_PASSWORD, password);

        DialogsFragment dialogsFragment = new DialogsFragment();
        dialogsFragment.setArguments(args);

        return dialogsFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getArguments().getString(Common.ARG_CURRENT_USER_NAME);
        password = getArguments().getString(Common.ARG_CURRENT_USER_PASSWORD);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dialogs, container, false);
        ButterKnife.bind(this, view);

        mRecyclerViewDialogsList.setHasFixedSize(true);
        mRecyclerViewDialogsList.setLayoutManager(new LinearLayoutManager(hostingActivity));
        mRecyclerViewDialogsList.addItemDecoration(new DividerItemDecoration(hostingActivity, LinearLayoutManager.VERTICAL));

        //previously we only signed in,but haven't opened a session
        //to use chat services API you should firstly open a session
        // (between aur app as a client and the chatting service) for single user
        createSessionForChatting();
        loadDialogs();

        mFabAddDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch usersActivity
                Intent intent = new Intent(hostingActivity, UsersActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void createSessionForChatting() {

        final QBUser qbUser = new QBUser(username,password);
        QBAuth.createSession(qbUser).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

                //Id of the user created the session
                //because the user must have his ID assigned
                //before try to login to chat srrvice
                qbUser.setId(qbSession.getUserId());
                //login to the chat service
                QBChatService.getInstance().login(qbUser, new QBEntityCallback() {
                    @Override
                    public void onSuccess(Object o, Bundle bundle) {

                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e(TAG, e.getMessage());

                    }
                });


            }

            @Override
            public void onError(QBResponseException e) {
                    Log.e(TAG,e.getMessage());
            }
        });

    }

    private void loadDialogs() {
        final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(hostingActivity).build();
        waitingDialog.setMessage("Loading dialogs ...");
        waitingDialog.setCanceledOnTouchOutside(false);
        waitingDialog.show();

        //get Dialogs list from QuickBlox
        QBRequestGetBuilder requestGetBuilder = new QBRequestGetBuilder();
        //API GET request ,max get 100 items i.e. here dialogs
        requestGetBuilder.setLimit(100);
        //send the request REST API
        QBRestChatService.getChatDialogs(null, requestGetBuilder)
                .performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
                    @Override
                    public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                        mDialogsAdapter = new DialogsAdapter(hostingActivity, qbChatDialogs);

                        if(mDialogsAdapter.getItemCount()==0){
                            mTextViewNoDialogs.setText(R.string.txt_no_dialogs);}
                        else {mTextViewNoDialogs.setText("");}

                        mRecyclerViewDialogsList.setAdapter(mDialogsAdapter);
                        waitingDialog.dismiss();
                    }
                    @Override
                    public void onError(QBResponseException e) {
                        waitingDialog.dismiss();
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        loadDialogs();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.hostingActivity = (FragmentActivity) context;
    }
}
