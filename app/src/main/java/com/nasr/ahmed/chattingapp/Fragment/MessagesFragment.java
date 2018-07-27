package com.nasr.ahmed.chattingapp.Fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nasr.ahmed.chattingapp.Adapter.MessagesAdapter;
import com.nasr.ahmed.chattingapp.Common.Common;
import com.nasr.ahmed.chattingapp.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MessagesFragment extends Fragment {

    private static final String TAG = MessagesFragment.class.getSimpleName();

    @BindView(R.id.recycler_messages_list)
    RecyclerView mRecyclerViewMessagesList;
    @BindView(R.id.imgbtn_send)
    ImageButton mIngBtnSend;
    @BindView(R.id.edt_message)
    EditText mEditTextMsg;
    @BindView(R.id.toolbar_message_fragment)
    Toolbar mToolbar;


    private FragmentActivity hostingActivity;
    private MessagesAdapter mMessagesAdapter;
    private QBChatDialog qbCurrentChatDialog;
    private ArrayList<QBChatMessage> dialogMessages;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qbCurrentChatDialog = (QBChatDialog) getArguments().getSerializable(Common.ARG_CHOOSEN_CHAT_DIALOG);
        dialogMessages = new ArrayList<>();
    }

    public static MessagesFragment newInstance(QBChatDialog qbChatDialog) {
        Bundle args = new Bundle();
        args.putSerializable(Common.ARG_CHOOSEN_CHAT_DIALOG, qbChatDialog);

        MessagesFragment messagesFragment = new MessagesFragment();
        messagesFragment.setArguments(args);

        return messagesFragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        ButterKnife.bind(this, view);

        mToolbar.setTitle(qbCurrentChatDialog.getName());
        ((AppCompatActivity) hostingActivity).setSupportActionBar(mToolbar);

        mRecyclerViewMessagesList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(hostingActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerViewMessagesList.setLayoutManager(linearLayoutManager);

        //Initialization
        //and adding the incoming message listener
        initChatDialogs();

        retrieveAllMessagesFromServer();


        mIngBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setBody(mEditTextMsg.getText().toString());
                qbChatMessage.setSenderId(QBChatService.getInstance().getUser().getId());
                qbChatMessage.setSaveToHistory(true);

                if (!qbChatMessage.getBody().equals("") ) {
                    if (Common.isConnectedToInternet(hostingActivity)) {

                        try {
                            qbCurrentChatDialog.sendMessage(qbChatMessage);
                        } catch (SmackException.NotConnectedException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.getMessage());
                        }

                        addTheMostRecentMessage(qbChatMessage);
                        mEditTextMsg.setText("");
                        mEditTextMsg.setFocusable(true);

                    } else {
                        Toast.makeText(getActivity(), "Check your Internet connectivity", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Empty message", Toast.LENGTH_SHORT).show();
                }


            }
        });

        return view;
    }


    private void initChatDialogs() {
        QBChatService chatService = QBChatService.getInstance();
        int id = chatService.getUser().getId();
        Log.e(TAG, "" + id);

        qbCurrentChatDialog.initForChat(QBChatService.getInstance());
        //Register incoming message listener
        QBIncomingMessagesManager incomingMessagesManager = QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessagesManager.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbIncomingChatMessage, Integer integer) {
                //One incoming message arrived to the application
                //checking if it belongs to current dialog
                String currentDialogID = qbCurrentChatDialog.getDialogId();
                if (qbIncomingChatMessage.getDialogId().equals(currentDialogID)) {
                    addTheMostRecentMessage(qbIncomingChatMessage);
                }

            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(hostingActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    //get dialog messages from the server
    private void retrieveAllMessagesFromServer() {

        QBMessageGetBuilder qbMessageGetBuilder = new QBMessageGetBuilder();
        qbMessageGetBuilder.setLimit(250);
        if (qbCurrentChatDialog != null) {
            QBRestChatService.getDialogMessages(qbCurrentChatDialog, qbMessageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    dialogMessages = qbChatMessages;

                    mMessagesAdapter = new MessagesAdapter(hostingActivity, dialogMessages);
                    mRecyclerViewMessagesList.setAdapter(mMessagesAdapter);
                }

                @Override
                public void onError(QBResponseException e) {

                }
            });

        }


    }


    private void addTheMostRecentMessage(QBChatMessage qbChatMessage) {
        dialogMessages.add(qbChatMessage);
        mMessagesAdapter = new MessagesAdapter(hostingActivity, dialogMessages);
        mRecyclerViewMessagesList.setAdapter(mMessagesAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        retrieveAllMessagesFromServer();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.hostingActivity = (FragmentActivity) context;
    }


}
