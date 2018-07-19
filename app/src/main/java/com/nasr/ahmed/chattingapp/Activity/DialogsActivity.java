package com.nasr.ahmed.chattingapp.Activity;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nasr.ahmed.chattingapp.Common.Common;
import com.nasr.ahmed.chattingapp.Fragment.DialogsFragment;
import com.nasr.ahmed.chattingapp.Fragment.SignInFragment;
import com.quickblox.users.model.QBUser;

public class DialogsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        String username = getIntent().getStringExtra(Common.EXTRA_CURRENT_USER_NAME);
        String password  = getIntent().getStringExtra(Common.EXTRA_CURRENT_USER_PASSWORD);
        return DialogsFragment.newInstance(username,password);
    }
}
