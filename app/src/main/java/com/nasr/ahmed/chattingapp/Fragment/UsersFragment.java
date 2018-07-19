package com.nasr.ahmed.chattingapp.Fragment;


import android.content.Context;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.nasr.ahmed.chattingapp.Adapter.UsersAdapter;
import com.nasr.ahmed.chattingapp.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBPagedRequestBuilder;
import com.quickblox.core.request.QBRequestBuilder;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersFragment extends Fragment {

    public static final String TAG = UsersFragment.class.getSimpleName();

    @BindView(R.id.recycler_users_list)
    RecyclerView mRecyclerViewUsersList;
    @BindView(R.id.txt_no_users)
    TextView mTextViewNoUsers;


    private FragmentActivity hostingActivity;
    private UsersAdapter mUsersAdapter;


    public static UsersFragment newInstance() {
        return new UsersFragment();
    }


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        ButterKnife.bind(this, view);

        mRecyclerViewUsersList.setHasFixedSize(true);
        mRecyclerViewUsersList.setLayoutManager(new LinearLayoutManager(hostingActivity));
        mRecyclerViewUsersList.addItemDecoration(new DividerItemDecoration(hostingActivity, LinearLayoutManager.VERTICAL));

        loadOtherUsers();



        return view;
    }


    private void loadOtherUsers() {

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {
                ArrayList<QBUser> usersListWithOutCurrentOne = new ArrayList<>();
                String currentUserLogin = QBChatService.getInstance().getUser().getLogin();

                for (QBUser user : qbUsers) {
                    if (!user.getLogin().equals(currentUserLogin)) {
                        usersListWithOutCurrentOne.add(user);
                    }
                }

                mUsersAdapter = new UsersAdapter(hostingActivity, usersListWithOutCurrentOne);
                if (mUsersAdapter.getItemCount() == 0) {
                    mTextViewNoUsers.setText(R.string.txt_no_dialogs);
                } else {
                    mTextViewNoUsers.setText("");
                }

                mRecyclerViewUsersList.setAdapter(mUsersAdapter);
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e(TAG,e.getMessage());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        loadOtherUsers();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.hostingActivity = (FragmentActivity) context;
    }

}
