package com.nasr.ahmed.chattingapp.Fragment;


import android.content.Context;
import android.icu.lang.UScript;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
    @BindView(R.id.toolbar_user_fragment)
    Toolbar mToolbar;


    private FragmentActivity hostingActivity;
    private UsersAdapter mUsersAdapter;

    private ArrayList<QBUser> mUsersListWithOutCurrentOne;



    public static UsersFragment newInstance() {
        return new UsersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsersListWithOutCurrentOne= new ArrayList<>();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        ButterKnife.bind(this, view);

        mToolbar.setTitle("Choose a friend,");
        ((AppCompatActivity)hostingActivity).setSupportActionBar(mToolbar);

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

                mUsersListWithOutCurrentOne.clear();
                String currentUserLogin = QBChatService.getInstance().getUser().getLogin();

                for (QBUser user : qbUsers) {
                    if (! user.getLogin() .equals( currentUserLogin)) {
                        mUsersListWithOutCurrentOne.add(user);
                    }
                }

                mUsersAdapter = new UsersAdapter(hostingActivity, mUsersListWithOutCurrentOne);
                if (mUsersAdapter.getItemCount() == 0) {
                    mTextViewNoUsers.setText(R.string.txt_no_users);
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
